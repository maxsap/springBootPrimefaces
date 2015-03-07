package com.boot.pf.config;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dasanderl on 12.09.14.
 */
@Configuration
public class ExternalPropsConfig {

    static {
        LoggerFactory.getLogger(ExternalPropsConfig.class).debug("loading external properties");
    }
}