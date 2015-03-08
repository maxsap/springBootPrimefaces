package com.boot.pf.config;

import com.boot.pf.domain.User;
import com.boot.pf.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.inject.Inject;


/**
 * Created by dasanderl on 11.09.14.
 */
@Configuration
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

    @Inject
    private UserRepository userRepository;
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(
                        "/resources/**"
                        , "/templates/**"
                        , "/api/**"
                        , "/401.html"
                        , "/404.html"
                        , "/500.html"
                );
    }

    @Override
    @Order(1)
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/templates/**").permitAll()
                .antMatchers("/401.html").permitAll()
                .antMatchers("/404.html").permitAll()
                .antMatchers("/500.html").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/ui/admin.xhtml").hasAnyAuthority("admin", "ADMIN")
                .antMatchers("/thymeleaf").hasAnyAuthority("admin", "ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/ui/index.xhtml", true)
                .failureUrl("/login?error=1")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .rememberMe()
                .and().exceptionHandling().accessDeniedPage("/error/403");
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
        }).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();

    }
}
