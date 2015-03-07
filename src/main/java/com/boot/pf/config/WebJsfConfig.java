package com.boot.pf.config;

import com.boot.pf.helper.UserAgentListener;
import com.boot.pf.helper.ViewScope;
import com.google.common.collect.Maps;
import com.sun.faces.config.ConfigureListener;
import org.atmosphere.cache.UUIDBroadcasterCache;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereFramework;
import org.atmosphere.cpr.AtmosphereServlet;
import org.atmosphere.cpr.MetaBroadcaster;
import org.primefaces.push.annotation.PushEndpoint;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.faces.webapp.FacesServlet;
import javax.servlet.*;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by dasanderl on 12.09.14.
 */
@Configuration
public class WebJsfConfig implements ServletContextAware {

    @Bean
    public static CustomScopeConfigurer getViewScopeConfigurer() {
        CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
        Map<String, Object> view = Maps.newHashMap();
        view.put("view", viewScope());
        customScopeConfigurer.setScopes(view);
        return customScopeConfigurer;
    }

    @Bean
    public static ViewScope viewScope() {
        return new ViewScope();
    }

    @Bean
    public Filter openEntityManagerInViewFilter() {
        return new OpenEntityManagerInViewFilter();
    }

    @Bean
    public Filter userAgentListenerFilter() {
        return new UserAgentListener();
    }

    @Bean
    public FilterRegistrationBean registration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(userAgentListenerFilter());
        registration.setUrlPatterns(Arrays.asList("/primepush/browser"));
        registration.setEnabled(true);
        return registration;
    }

    @Bean
    public FacesServlet facesServlet() {
        return new FacesServlet();
    }


    @Bean
    public ServletRegistrationBean facesServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(
                facesServlet(), "*.xhtml", "*.jsf");
        registration.setLoadOnStartup(1);

        return registration;
    }

    @Bean
    public ConfigureListener configureListener() {
        return new ConfigureListener();
    }

    @Bean
    public ViewResolver getJsfViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/");
        resolver.setSuffix(".jsf ");
        return resolver;
    }

    @Bean
    public ServletListenerRegistrationBean<ConfigureListener> jsfConfigureListener() {
        return new ServletListenerRegistrationBean<>(new ConfigureListener());
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
        servletContext.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", Boolean.TRUE.toString());
        servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Development");
        servletContext.setInitParameter("com.sun.faces.expressionFactory", "com.sun.el.ExpressionFactoryImpl");
        servletContext.setInitParameter("javax.faces.FACELETS_LIBRARIES", "/WEB-INF/springsecurity.taglib.xml");

        /*servletContext.addListener(new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent httpSessionEvent) {
                httpSessionEvent.getSession().setMaxInactiveInterval(60);
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
            }
        });*/
        configureAthmosphere(atmosphereServlet(), servletContext);
    }



    /**
     * <p>
     * Creates simple atmosphere servlet.
     * </p>
     *
     * @return the servlet
     */
    @Bean
    public AtmosphereServlet atmosphereServlet() {
        return new AtmosphereServlet();
    }

    /**
     * <p>
     * Gets framework from servlet.
     * </p>
     *
     * @return the framework
     */
    @Bean
    public AtmosphereFramework atmosphereFramework() {
        return atmosphereServlet().framework();
    }

    /**
     * Gets the broadcaster that push messages to all resources.
     *
     * @return the broadcaster
     */
    @Bean
    public MetaBroadcaster metaBroadcaster() {
        AtmosphereFramework framework = atmosphereFramework();
        return framework.metaBroadcaster();
    }


    /**
     * <p>
     * Configures atmosphere.
     * </p>
     *
     * @param servlet the servlet
     * @param servletContext the context
     */
    private void configureAthmosphere(final AtmosphereServlet servlet, final ServletContext servletContext) {
        final ServletRegistration.Dynamic atmosphereServlet = servletContext.addServlet("atmosphereServlet", servlet);
        atmosphereServlet.setInitParameter(ApplicationConfig.ANNOTATION_PACKAGE, PushEndpoint.class.getPackage().getName());
        atmosphereServlet.setInitParameter(ApplicationConfig.ATMOSPHERE_INTERCEPTORS, "com.boot.pf.helper.RecoverSecurityContextAtmosphereInterceptor");
        atmosphereServlet.setInitParameter(ApplicationConfig.BROADCASTER_CACHE, UUIDBroadcasterCache.class.getName());
        atmosphereServlet.setInitParameter(ApplicationConfig.BROADCASTER_SHARABLE_THREAD_POOLS, "true");
        atmosphereServlet.setInitParameter(ApplicationConfig.BROADCASTER_MESSAGE_PROCESSING_THREADPOOL_MAXSIZE, "10");
        atmosphereServlet.setInitParameter(ApplicationConfig.BROADCASTER_ASYNC_WRITE_THREADPOOL_MAXSIZE, "10");
        servletContext.addListener(new org.atmosphere.cpr.SessionSupport());

        atmosphereServlet.setInitParameter(ApplicationConfig.WEBSOCKET_SUPPORT, "true");
        atmosphereServlet.setInitParameter(ApplicationConfig.WEBSOCKET_SUPPORT_SERVLET3, "true");

        atmosphereServlet.addMapping("/primepush/*");
        atmosphereServlet.setLoadOnStartup(0);
        atmosphereServlet.setAsyncSupported(true);
    }

}