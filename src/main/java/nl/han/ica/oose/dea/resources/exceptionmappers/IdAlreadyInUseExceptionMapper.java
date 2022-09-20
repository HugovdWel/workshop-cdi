package nl.han.ica.oose.dea.resources.exceptionmappers;


import nl.han.ica.oose.dea.services.exceptions.IdAlreadyInUseException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class IdAlreadyInUseExceptionMapper implements ExceptionMapper<IdAlreadyInUseException> {
    @Override
    public Response toResponse(IdAlreadyInUseException e) {
        return Response.status(Response.Status.CONFLICT).build();
    }
}
