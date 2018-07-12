package de.uniba.dsg.jaxrs.exceptions;

import de.uniba.dsg.models.ErrorMessage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ResourceNotFoundException extends WebApplicationException {
    public ResourceNotFoundException(ErrorMessage message) { super(Response.status(404).entity(message).build());
        System.out.println("Response code:" +getResponse().getStatus() +"\t Message:" + message.getMessage());
    }
}
