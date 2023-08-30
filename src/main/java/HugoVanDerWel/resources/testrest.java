package HugoVanDerWel.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/test")
public class testrest {


    public testrest() {
    }

    @GET
    public Response test(){
        return Response.ok().build();
    }
}
