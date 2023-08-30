package HugoVanDerWel.resources;

import HugoVanDerWel.models.PlaylistModel;
import HugoVanDerWel.models.TrackModel;
import HugoVanDerWel.services.AuthenticationService;
import HugoVanDerWel.services.PlaylistService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.Response;

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
    public Response getPlaylists(@QueryParam("token") String inputToken) {
        return Response.status(200).entity(
                this.playlistService.getAllPlaylists(authenticationService.getUsernameForToken(inputToken))
        ).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePlaylist(@QueryParam("token") String inputToken, @PathParam("id") int id) {
        this.playlistService.deletePlaylist(id);
        return Response.status(200).entity(
                this.playlistService.getAllPlaylists(authenticationService.getUsernameForToken(inputToken))
        ).build();
    }

    @POST
    public Response createPlaylist(@QueryParam("token") String inputToken, PlaylistModel newPlaylist) {
        this.playlistService.createPlaylist(newPlaylist);
        return Response.status(200).entity(
                this.playlistService.getAllPlaylists(authenticationService.getUsernameForToken(inputToken))
        ).build();
    }

    @PUT
    @Path("/{id}")
    public Response editPlaylist(@QueryParam("token") String inputToken, PlaylistModel newPlaylist, @PathParam("id") int id) {
        this.playlistService.replacePlaylist(newPlaylist, id);
        return Response.status(200).entity(
                this.playlistService.getAllPlaylists(authenticationService.getUsernameForToken(inputToken))
        ).build();
    }

    @GET
    @Path("/{id}/tracks")
    public Response getAllTracksInPlaylist(@QueryParam("token") String inputToken, @PathParam("id") int id) {
        return Response.status(200).entity(
                this.playlistService.getTracksInPlaylist(id)
        ).build();
    }

    @POST
    @Path("/{id}/tracks")
    public Response addTrackToPlaylist(@QueryParam("token") String inputToken, @PathParam("id") int id, TrackModel track) {
        this.playlistService.addTrackToPlaylist(id, track);
        return Response.status(200).entity(
                this.playlistService.getTracksInPlaylist(id)
        ).build();
    }

    @DELETE
    @Path("/{playlistId}/tracks/{trackId}")
    public Response removeTrackFromPlaylist(@QueryParam("token") String inputToken, @PathParam("playlistId") int playlistId, @PathParam("trackId") int trackId) {
        this.playlistService.removeTrackFromPlaylist(playlistId, trackId);
        return Response.status(200).entity(
                this.playlistService.getTracksInPlaylist(playlistId)
        ).build();
    }
}
