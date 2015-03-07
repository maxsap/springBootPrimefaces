package com.boot.pf.helper;

import org.atmosphere.cpr.Action;
import org.atmosphere.cpr.AtmosphereConfig;
import org.atmosphere.cpr.AtmosphereInterceptor;
import org.atmosphere.cpr.AtmosphereResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

/**
 * Atmosphere interceptor in order to support Spring security.
 * @see <a href="https://github.com/orachid/school">https://github.com/orachid/school</a>
 *
 * @author Maximos Sapranidis
 */
public class RecoverSecurityContextAtmosphereInterceptor implements AtmosphereInterceptor {
    private Logger logger = LoggerFactory.getLogger(RecoverSecurityContextAtmosphereInterceptor.class);

    @Override
    public void configure(AtmosphereConfig atmosphereConfig) {
        System.out.println(atmosphereConfig);
    }

    @Override
    public Action inspect(AtmosphereResource atmosphereResource) {
        logger.debug("Recover SecurityContext in SecurityContextHolder");
        SecurityContext context = (SecurityContext) atmosphereResource.getRequest().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        if(context!=null){
            SecurityContextHolder.setContext(context);
        }
        return Action.CONTINUE;
    }

    @Override
    public void postInspect(AtmosphereResource atmosphereResource) {
        System.out.println(atmosphereResource);
    }

}