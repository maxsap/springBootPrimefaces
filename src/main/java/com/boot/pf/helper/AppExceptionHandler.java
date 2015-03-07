package com.boot.pf.helper;

/**
 * Created by Maximos on 3/4/2015.
 */
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.util.Iterator;

public class AppExceptionHandler extends ExceptionHandlerWrapper {

    private javax.faces.context.ExceptionHandler wrapped;

    public AppExceptionHandler(javax.faces.context.ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public javax.faces.context.ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
        Iterable<ExceptionQueuedEvent> events = this.wrapped.getUnhandledExceptionQueuedEvents();
        for (Iterator<ExceptionQueuedEvent> it = events.iterator(); it.hasNext(); ) {
            //try {
                ExceptionQueuedEvent event = it.next();
                ExceptionQueuedEventContext eqec = event.getContext();

                eqec.getException().printStackTrace();

                if (eqec.getException() instanceof ViewExpiredException) {
                    FacesContext context = eqec.getContext();
                    if (!context.isReleased()) {
                        NavigationHandler navHandler = context.getApplication().getNavigationHandler();
                        navHandler.handleNavigation(context, null, "home?faces-redirect=true&expired=true");
                    }

                }// else {
                //    setMessage("Ooops Unhandled error please try again later", "Reason: "+eqec.getException().getMessage());
               // }
            /*} finally {
                it.remove();
            }*/
        }

        this.wrapped.handle();
    }

    private void setMessage(String head, String summary) {
        FacesMessage msg = new FacesMessage(head, summary);
        msg.setSeverity(FacesMessage.SEVERITY_FATAL);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}

