package com.boot.pf.helper;

/**
 * Created by Maximos on 3/4/2015.
 */
public class ExceptionHandlerFactory extends javax.faces.context.ExceptionHandlerFactory {
    private javax.faces.context.ExceptionHandlerFactory base;

    private AppExceptionHandler cached;

    public ExceptionHandlerFactory(javax.faces.context.ExceptionHandlerFactory base) {
        this.base = base;
    }

    @Override
    public javax.faces.context.ExceptionHandler getExceptionHandler() {
        if (cached == null) {
            cached = new AppExceptionHandler(base.getExceptionHandler());
        }

        return cached;
    }
}
