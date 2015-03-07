package com.boot.pf.controller;

import org.atmosphere.config.service.Disconnect;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Message;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONDecoder;
import org.primefaces.push.impl.JSONEncoder;
import org.springframework.stereotype.Component;

import javax.websocket.OnOpen;
import java.util.Map;

/**
 * Created by Maximos on 3/5/2015.
 * In this case @PushEndpoint will not work, that is why we need to
 * use Atmosphere's @ManagedService.
 */
@Component
@ManagedService(path = "/browser")
public class BrowserStatsResource {

    @Ready
    public void onReady(final AtmosphereResource resource) {
        System.out.println("Connected "+resource.uuid());
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent event) {
        System.out.println("Client {} disconnected [{}] "+event.getResource().uuid() +" "+
                (event.isCancelled() ? "cancelled" : "closed"));
    }

    @Message(encoders = {JSONEncoder.class}, decoders = JSONDecoder.class)
    public String onMessage(final String m) {
        //return "{ \"MSIE\": \"0\", \"Firefox\": \"0\", \"Chrome\":\"63\", \"Safari\":\"0\", \"Other\":\"0\"}";
        return m;
    }

    @Message(encoders = {JSONEncoder.class}, decoders = JSONDecoder.class)
    public Map onMessage(final Map m) {
        return m;
    }

    /*@Message(encoders = {JSONEncoder.class}, decoders = JSONDecoder.class)
    public Object onMessage(final Object m) {
        return m;
    }*/

}