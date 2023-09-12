package HugoVanDerWel.resources;

import HugoVanDerWel.services.AuthenticationService;
import HugoVanDerWel.services.PlaylistService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/tracks")
public class TrackResource {
    private PlaylistService playlistService;
    private AuthenticationService authenticationService;

    public TrackResource() {
    }

    @Inject
    public TrackResource(PlaylistService playlistService, AuthenticationService authenticationService) {
        this.playlistService = playlistService;
        this.authenticationService = authenticationService;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Response getAvailableTracksForPlaylist(@QueryParam("token") String token, @QueryParam("forPlaylist") int playlistId ){
        if(!authenticationService.isTokenActive(token)){
            return Response.status(403).build();
        }return Response.status(200).entity(playlistService.getTracksNotInPlaylist(playlistId)).build();
    }
}
