package HugoVanDerWel.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/tracks")
public class TrackResource {

    public TrackResource() {
    }
    @GET
    public Response getAvailableTracksForPlaylist(@QueryParam("token") int token, @QueryParam("forPlaylist") int playlistId ){
        return Response.status(418).build();
    }
}
