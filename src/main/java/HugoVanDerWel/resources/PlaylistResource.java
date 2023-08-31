package HugoVanDerWel.resources;

import HugoVanDerWel.dataTransferObjects.PlayListsDTO;
import HugoVanDerWel.exceptions.UnauthorizedException;
import HugoVanDerWel.models.PlaylistModel;
import HugoVanDerWel.models.TrackModel;
import HugoVanDerWel.models.UserModel;
import HugoVanDerWel.services.AuthenticationService;
import HugoVanDerWel.services.PlaylistService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/playlists")
public class PlaylistResource {
    private PlaylistService playlistService;
    private AuthenticationService authenticationService;

    public PlaylistResource() {
    }

    @Inject
    public PlaylistResource(PlaylistService playlistService, AuthenticationService authenticationService) {
        this.playlistService = playlistService;
        this.authenticationService = authenticationService;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Response getPlaylists(@QueryParam("token") String inputToken) {
        try {
            UserModel owner = authenticationService.getUsernameForToken(inputToken);
            PlayListsDTO playListsDTO = this.playlistService.getAllPlaylists(owner);
            if(playListsDTO.length == 0){
                return Response.status(204).build();
            }
            return Response.status(200).entity(playListsDTO).build();
        } catch (UnauthorizedException e) {
            return Response.status(403).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    public Response deletePlaylist(@QueryParam("token") String inputToken, @PathParam("id") int id) {
        try {
            UserModel owner = authenticationService.getUsernameForToken(inputToken);
            this.playlistService.deletePlaylist(id);
            return Response.status(200).entity(this.playlistService.getAllPlaylists(owner)).build();
        } catch (UnauthorizedException e) {
            return Response.status(403).build();
        }
    }

    @POST
    @Produces(APPLICATION_JSON)
    public Response createPlaylist(@QueryParam("token") String inputToken, PlaylistModel newPlaylist) {
        try {
            UserModel owner = authenticationService.getUsernameForToken(inputToken);
            this.playlistService.createPlaylist(newPlaylist, owner);
            return Response.status(200).entity(this.playlistService.getAllPlaylists(owner)).build();
        } catch (UnauthorizedException e) {
            return Response.status(403).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    public Response editPlaylist(@QueryParam("token") String inputToken, PlaylistModel newPlaylist, @PathParam("id") int id) {
        try {
            UserModel owner = authenticationService.getUsernameForToken(inputToken);
            this.playlistService.replacePlaylist(newPlaylist, id, owner);
            return Response.status(200).entity(this.playlistService.getAllPlaylists(owner)).build();
        } catch (UnauthorizedException e) {
            return Response.status(403).build();
        }
    }

    @GET
    @Path("/{id}/tracks")
    @Produces(APPLICATION_JSON)
    public Response getAllTracksInPlaylist(@QueryParam("token") String inputToken, @PathParam("id") int id) {
        return Response.status(200).entity(this.playlistService.getTracksInPlaylist(id)).build();
    }

    @POST
    @Path("/{id}/tracks")
    @Produces(APPLICATION_JSON)
    public Response addTrackToPlaylist(@QueryParam("token") String inputToken, @PathParam("id") int id, TrackModel track) {
        this.playlistService.addTrackToPlaylist(id, track);
        return Response.status(200).entity(this.playlistService.getTracksInPlaylist(id)).build();
    }

    @DELETE
    @Path("/{playlistId}/tracks/{trackId}")
    @Produces(APPLICATION_JSON)
    public Response removeTrackFromPlaylist(@QueryParam("token") String inputToken, @PathParam("playlistId") int playlistId, @PathParam("trackId") int trackId) {
        this.playlistService.removeTrackFromPlaylist(playlistId, trackId);
        return Response.status(200).entity(this.playlistService.getTracksInPlaylist(playlistId)).build();
    }
}
