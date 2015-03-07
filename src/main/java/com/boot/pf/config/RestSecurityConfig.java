package com.boot.pf.config;

import com.boot.pf.domain.User;
import com.boot.pf.repositories.UserRepository;
import com.boot.pf.security.AccessDeniedExceptionHandler;
import com.boot.pf.security.AuthenticationLoggerListener;
import com.boot.pf.security.UnauthorizedEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.access.event.AbstractAuthorizationEvent;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import javax.inject.Inject;

/**
 * Created by Maximos on 3/6/2015.
 */
@Configuration
@EnableResourceServer
@Order(2)
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {


    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationListener<AbstractAuthorizationEvent> loggerBean() {
        return new AuthenticationLoggerListener();
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedExceptionHandler();
    }

    @Bean
    AuthenticationEntryPoint entryPointBean() {
        return new UnauthorizedEntryPoint();
    }

    /*Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(
                        "/resources/**"
                        , "/templates/**"
                        , "/login"
                        , "/logout"
                        , "/ui/**"
                        , "/401.html"
                        , "/404.html"
                        , "/500.html"
                );
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ContentNegotiationStrategy contentNegotiationStrategy = http.getSharedObject(ContentNegotiationStrategy.class);
        if (contentNegotiationStrategy == null) {
            contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
        }
        MediaTypeRequestMatcher preferredMatcher = new MediaTypeRequestMatcher(contentNegotiationStrategy,
                MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_JSON,
                MediaType.MULTIPART_FORM_DATA);

        http.authorizeRequests()
                .antMatchers("/ui/**").permitAll()
                .and()
                .anonymous().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().httpBasic()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler()) // handle access denied in general (for example comming from @PreAuthorization
                .authenticationEntryPoint(entryPointBean()) // handle authentication exceptions for unauthorized calls.
                .defaultAuthenticationEntryPointFor(entryPointBean(), preferredMatcher)
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").fullyAuthenticated();

    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
                User user = userRepository.findOneByUsername(s);

                if (null == user) {
                    // leave that to be handled by log listener
                    throw new UsernameNotFoundException("The user with email " + s + " was not found");
                }

                return (UserDetails) user;
            }
        }).passwordEncoder(passwordEncoder);
    }


    @Configuration
    @EnableAuthorizationServer
    protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {

        private static final String API_RESOURCE = "API-RESOURCE";

        @Value("${web.token.expiry}")
        private static int WEB_EXPIRY;
        @Value("${iphone.token.expiry}")
        private static int IPHONE_EXPIRY;
        @Value("${android.token.expiry}")
        private static int ANDROID_EXPIRY;

        @Autowired
        private AuthenticationManager authenticationManager;


        @Bean
        public JwtAccessTokenConverter accessTokenConverter() {
            return new JwtAccessTokenConverter();
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            oauthServer.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_TRUSTED_CLIENT')").checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.authenticationManager(authenticationManager).accessTokenConverter(accessTokenConverter());
        }


        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("web_application")
                    .resourceIds(API_RESOURCE)
                    .authorizedGrantTypes("password")
                    .authorities("ROLE_CLIENT")
                    .scopes("read", "write", "trust")
                    .accessTokenValiditySeconds(WEB_EXPIRY)
                    .autoApprove(false)
                    .secret("secret")
                    .and()
                    .withClient("mobile-iphone")
                    .resourceIds(API_RESOURCE)
                    .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
                    .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
                    .scopes("read", "write", "trust", "update")
                    .accessTokenValiditySeconds(IPHONE_EXPIRY)
                    .refreshTokenValiditySeconds(IPHONE_EXPIRY)
                    .secret("secret")
                    .and()
                    .withClient("mobile-android")
                    .resourceIds(API_RESOURCE)
                    .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
                    .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
                    .scopes("read", "write", "trust", "update")
                    .accessTokenValiditySeconds(ANDROID_EXPIRY)
                    .refreshTokenValiditySeconds(ANDROID_EXPIRY)
                    .secret("secret");

        }
    }
}
