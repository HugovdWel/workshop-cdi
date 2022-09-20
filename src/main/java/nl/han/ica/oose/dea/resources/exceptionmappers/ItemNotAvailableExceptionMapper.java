package nl.han.ica.oose.dea.resources.exceptionmappers;

import nl.han.ica.oose.dea.services.exceptions.ItemNotAvailableException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ItemNotAvailableExceptionMapper implements ExceptionMapper<ItemNotAvailableException> {
    @Override
    public Response toResponse(ItemNotAvailableException e) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
