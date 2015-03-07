package com.boot.pf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableJpaAuditing(auditorAwareRef = "auditingAware")
@EnableConfigurationProperties //use this to register other properties sources e.g. property files
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    /*@Bean
    public ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
        registration.addUrlMappings(h2ConsoleUrlMapping);
        return registration;
    }*/
}