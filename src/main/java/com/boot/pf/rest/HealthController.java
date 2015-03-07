package com.boot.pf.rest;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Created by Maximos on 3/6/2015.
 */
@Component
@Path("/health")
public class HealthController {

    @GET
    @Path("/unprotected")
    @Produces("application/json")
    public Response unprotectedResource() {
        return Response.ok().entity("Jersey unprotected resource").build();
    }

    @GET
    @Path("/protected")
    @Produces("application/json")
    public Response protectedResource() {
        return Response.ok().entity("Jersey protected resource").build();
    }
}