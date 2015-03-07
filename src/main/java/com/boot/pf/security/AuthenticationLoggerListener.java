package com.boot.pf.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.event.AbstractAuthorizationEvent;

/**
 * Created by maxsap on 9/1/15.
 * Callback to handle authentication events in general.
 * Here we should integrate post authentication logic like brute force protection.
 */
public class AuthenticationLoggerListener implements
        ApplicationListener<AbstractAuthorizationEvent>, ApplicationEventPublisherAware {

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory
            .getLogger(AuthenticationLoggerListener.class);

    //@Autowired
    // private Reactor reactor;


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        /*try {
            System.out.println(Serializer.toJson(applicationEventPublisher));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onApplicationEvent(AbstractAuthorizationEvent abstractAuthorizationEvent) {
        /*try {
            System.out.println(Serializer.toJson(abstractAuthorizationEvent));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}