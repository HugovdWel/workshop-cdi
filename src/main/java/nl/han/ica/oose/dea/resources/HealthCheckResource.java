package nl.han.ica.oose.dea.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/health")
public class HealthCheckResource {

    @GET
    public String healthy() {
        return "Up & Running";
    }
}
