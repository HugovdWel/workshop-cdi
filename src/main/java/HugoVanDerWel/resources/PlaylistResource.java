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
//            if(playListsDTO.length == 0){             Unnecessary?
//                return Response.status(204).build();
//            }
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
            playlistService.checkIfUserMayEditPlaylist(owner, id);
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
    public Response editPlaylistName(@QueryParam("token") String inputToken, PlaylistModel newPlaylist, @PathParam("id") int id) {
        try {
            UserModel owner = authenticationService.getUsernameForToken(inputToken);
            this.playlistService.editPlaylistName(newPlaylist.name, id);
            return Response.status(200).entity(this.playlistService.getAllPlaylists(owner)).build();
        } catch (UnauthorizedException e) {
            return Response.status(403).build();
        }
    }

    @GET
    @Path("/{id}/tracks")
    @Produces(APPLICATION_JSON)
    public Response getAllTracksInPlaylist(@QueryParam("token") String inputToken, @PathParam("id") int id) {
        if(!authenticationService.isTokenActive(inputToken)){
            return Response.status(403).build();
        }
        return Response.status(200).entity(this.playlistService.getTracksInPlaylist(id)).build();
    }

    @POST
    @Path("/{id}/tracks")
    @Produces(APPLICATION_JSON)
    public Response addTrackToPlaylist(@QueryParam("token") String inputToken, @PathParam("id") int playlistId, TrackModel track) {
        if(!authenticationService.isTokenActive(inputToken)){
            return Response.status(403).build();
        }
        this.playlistService.addTrackToPlaylist(playlistId, track);
        return Response.status(200).entity(this.playlistService.getTracksInPlaylist(playlistId)).build();
    }

    @DELETE
    @Path("/{playlistId}/tracks/{trackId}")
    @Produces(APPLICATION_JSON)
    public Response removeTrackFromPlaylist(@QueryParam("token") String inputToken, @PathParam("playlistId") int playlistId, @PathParam("trackId") int trackId) {
        if(!authenticationService.isTokenActive(inputToken)){
            return Response.status(403).build();
        }
        this.playlistService.removeTrackFromPlaylist(playlistId, trackId);
        return Response.status(200).entity(this.playlistService.getTracksInPlaylist(playlistId)).build();
    }
}
