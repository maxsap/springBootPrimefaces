package com.boot.pf.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Maximos on 3/6/2015.
 */
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException arg2) throws IOException, ServletException {
        if(!response.isCommitted())
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, arg2.getMessage());
    }


}
