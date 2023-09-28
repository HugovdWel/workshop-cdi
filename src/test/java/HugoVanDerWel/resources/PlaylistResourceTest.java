package HugoVanDerWel.resources;

import HugoVanDerWel.presentation.dataTransferObjects.PlayListsDTO;
import HugoVanDerWel.presentation.dataTransferObjects.TracksDTO;
import HugoVanDerWel.presentation.resources.PlaylistResource;
import HugoVanDerWel.service.exceptions.UnauthorizedException;
import HugoVanDerWel.service.models.PlaylistModel;
import HugoVanDerWel.service.models.TrackModel;
import HugoVanDerWel.service.models.UserModel;
import HugoVanDerWel.service.services.AuthenticationService;
import HugoVanDerWel.service.services.PlaylistService;
import HugoVanDerWel.support.TestAuthenticationSupport;
import jakarta.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PlaylistResourceTest {

    private PlaylistResource sut;
    private PlaylistService playlistService;
    private AuthenticationService successfulAuthenticationService;
    private AuthenticationService unauthorizedAuthenticationService;

    private TracksDTO tracksDTO = new TracksDTO() {{
        tracks = new TrackModel[]{new TrackModel() {{
            title = "Kevins song";
        }}};
    }};

    private PlayListsDTO playListsDTO = new PlayListsDTO() {{
        PlaylistModel[] testPlaylistModel = new PlaylistModel[]{new PlaylistModel() {{
            id = 1;
            name = "Gert";
            owner = false;
            tracks = new TrackModel[]{};
        }}, new PlaylistModel() {{
            id = 2;
            name = "bas";
            owner = true;
            tracks = new TrackModel[]{};
        }}};
        length = 2;
    }};
    private UserModel owner = new UserModel() {{
        token = "123123";
    }};

    @Before
    public void setup() {
        this.playlistService = Mockito.mock(PlaylistService.class);
        this.successfulAuthenticationService = TestAuthenticationSupport.getSuccessfullLoginAuthenticationService();
        this.unauthorizedAuthenticationService = TestAuthenticationSupport.getIncorrectLoginAuthenticationService();
        Mockito.when(playlistService.getAllPlaylists(Mockito.any())).thenReturn(playListsDTO);
    }

    @Test
    public void getPlaylistsReturnsCorrectDataOnGoodRequest() {
        //setup
        this.sut = new PlaylistResource(playlistService, successfulAuthenticationService);

        //act
        Response response = sut.getPlaylists(owner.token);
        PlayListsDTO responseEntity = (PlayListsDTO) response.getEntity();

        //assert
        Assert.assertEquals(playListsDTO, responseEntity);
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void deletePlaylistReturnsCorrectDataOnGoodRequest() {
        //setup
        this.sut = new PlaylistResource(playlistService, successfulAuthenticationService);

        //act
        Response response = sut.deletePlaylist(owner.token, 1);
        PlayListsDTO responseEntity = (PlayListsDTO) response.getEntity();

        //assert
        Assert.assertEquals(playListsDTO, responseEntity);
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void createPlaylistReturnsCorrectDataOnGoodRequest() {
        //setup
        PlaylistModel newPlaylist = new PlaylistModel() {{
            name = "MUSICA";
        }};
        this.sut = new PlaylistResource(playlistService, successfulAuthenticationService);

        //act
        Response response = sut.createPlaylist(owner.token, newPlaylist);
        PlayListsDTO responseEntity = (PlayListsDTO) response.getEntity();

        //assert
        Assert.assertEquals(playListsDTO, responseEntity);
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void editPlaylistNameReturnsCorrectDataOnGoodRequest() {
        //setup
        PlaylistModel newPlaylist = new PlaylistModel() {{
            name = "MUSICA";
        }};
        this.sut = new PlaylistResource(playlistService, successfulAuthenticationService);

        //act
        Response response = sut.editPlaylistName(owner.token, newPlaylist, 1);
        PlayListsDTO responseEntity = (PlayListsDTO) response.getEntity();

        //assert
        Assert.assertEquals(playListsDTO, responseEntity);
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void getAllTracksInPlaylistReturnsCorrectDataOnGoodRequest() {
        //setup
        Mockito.when(playlistService.getTracksInPlaylist(Mockito.anyInt())).thenReturn(tracksDTO);
        this.sut = new PlaylistResource(playlistService, successfulAuthenticationService);

        //act
        Response response = sut.getAllTracksInPlaylist(owner.token, 1);
        TracksDTO responseEntity = (TracksDTO) response.getEntity();

        //assert
        Assert.assertEquals(tracksDTO, responseEntity);
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void addTrackToPlaylistReturnsCorrectDataOnGoodRequest() {
        //setup
        Mockito.when(playlistService.getTracksInPlaylist(Mockito.anyInt())).thenReturn(tracksDTO);
        TrackModel newTrack = new TrackModel() {{
            title = "spagetti!";
            offlineAvailable = true;
        }};
        this.sut = new PlaylistResource(playlistService, successfulAuthenticationService);

        //act
        Response response = sut.addTrackToPlaylist(owner.token, 1, newTrack);
        TracksDTO responseEntity = (TracksDTO) response.getEntity();

        //assert
        Assert.assertEquals(tracksDTO, responseEntity);
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void removeTrackFromPlaylistReturnsCorrectDataOnGoodRequest() {
        //setup
        Mockito.when(playlistService.getTracksInPlaylist(Mockito.anyInt())).thenReturn(tracksDTO);
        this.sut = new PlaylistResource(playlistService, successfulAuthenticationService);

        //act
        Response response = sut.removeTrackFromPlaylist(owner.token, 1, 1);
        TracksDTO responseEntity = (TracksDTO) response.getEntity();

        //assert
        Assert.assertEquals(tracksDTO, responseEntity);
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void getPlaylistsReturns403WhenUnauthorizedRequestIsSend() {
        //setup
        this.sut = new PlaylistResource(playlistService, unauthorizedAuthenticationService);

        //act
        Response response = sut.getPlaylists(owner.token);
        PlayListsDTO responseEntity = (PlayListsDTO) response.getEntity();

        //assert
        Assert.assertNull(responseEntity);
        Assert.assertEquals(403, response.getStatus());
    }

    @Test
    public void deletePlaylistReturns403WhenUnauthorizedRequestIsSend() {
        //setup
        this.sut = new PlaylistResource(playlistService, unauthorizedAuthenticationService);

        //act
        Response response = sut.deletePlaylist(owner.token, 1);
        PlayListsDTO responseEntity = (PlayListsDTO) response.getEntity();

        //assert
        Assert.assertNull(responseEntity);
        Assert.assertEquals(403, response.getStatus());
    }

    @Test
    public void createPlaylistReturns403WhenUnauthorizedRequestIsSend() {
        //setup
        PlaylistModel newPlaylist = new PlaylistModel() {{
            name = "MUSICA";
        }};
        this.sut = new PlaylistResource(playlistService, unauthorizedAuthenticationService);

        //act
        Response response = sut.createPlaylist(owner.token, newPlaylist);
        PlayListsDTO responseEntity = (PlayListsDTO) response.getEntity();

        //assert
        Assert.assertNull(responseEntity);
        Assert.assertEquals(403, response.getStatus());
    }

    @Test
    public void editPlaylistNameReturns403WhenUnauthorizedRequestIsSend() {
        //setup
        PlaylistModel newPlaylist = new PlaylistModel() {{
            name = "MUSICA";
        }};
        this.sut = new PlaylistResource(playlistService, unauthorizedAuthenticationService);

        //act
        Response response = sut.editPlaylistName(owner.token, newPlaylist, 1);
        PlayListsDTO responseEntity = (PlayListsDTO) response.getEntity();

        //assert
        Assert.assertNull(responseEntity);
        Assert.assertEquals(403, response.getStatus());
    }

    @Test
    public void getAllTracksInPlaylistReturns403WhenUnauthorizedRequestIsSend() {
        //setup
        this.sut = new PlaylistResource(playlistService, unauthorizedAuthenticationService);

        //act
        Response response = sut.getAllTracksInPlaylist(owner.token, 1);
        PlayListsDTO responseEntity = (PlayListsDTO) response.getEntity();

        //assert
        Assert.assertNull(responseEntity);
        Assert.assertEquals(403, response.getStatus());
    }

    @Test
    public void addTrackToPlaylistReturns403WhenUnauthorizedRequestIsSend() {
        //setup
        TrackModel newTrack = new TrackModel() {{
            title = "spagetti!";
            offlineAvailable = true;
        }};
        this.sut = new PlaylistResource(playlistService, unauthorizedAuthenticationService);

        //act
        Response response = sut.addTrackToPlaylist(owner.token, 1, newTrack);
        PlayListsDTO responseEntity = (PlayListsDTO) response.getEntity();

        //assert
        Assert.assertNull(responseEntity);
        Assert.assertEquals(403, response.getStatus());
    }

    @Test
    public void removeTrackFromPlaylistReturns403WhenUnauthorizedRequestIsSend() {
        //setup
        this.sut = new PlaylistResource(playlistService, unauthorizedAuthenticationService);

        //act
        Response response = sut.removeTrackFromPlaylist(owner.token, 1, 1);
        PlayListsDTO responseEntity = (PlayListsDTO) response.getEntity();

        //assert
        Assert.assertNull(responseEntity);
        Assert.assertEquals(403, response.getStatus());
    }

    @Test
    public void deletePlaylistReturns403WhenUserIsNotOwner() {
        //setup
        Mockito.doThrow(UnauthorizedException.class).when(playlistService).checkIfUserMayEditPlaylist(Mockito.any(), Mockito.anyInt());
        this.sut = new PlaylistResource(playlistService, successfulAuthenticationService);

        //act
        Response response = sut.deletePlaylist(owner.token, 1);
        PlayListsDTO responseEntity = (PlayListsDTO) response.getEntity();

        //assert
        Assert.assertNull(responseEntity);
        Assert.assertEquals(403, response.getStatus());
    }

    @Test
    public void editPlaylistNameReturns403WhenUserIsNotOwner() {
        //setup
        PlaylistModel newPlaylist = new PlaylistModel() {{
            name = "MUSICA";
        }};
        this.sut = new PlaylistResource(playlistService, unauthorizedAuthenticationService);

        //act
        Response response = sut.editPlaylistName(owner.token, newPlaylist, 1);
        PlayListsDTO responseEntity = (PlayListsDTO) response.getEntity();

        //assert
        Assert.assertNull(responseEntity);
        Assert.assertEquals(403, response.getStatus());
    }

    @Test
    public void addTrackToPlaylistReturns403WhenUserIsNotOwner() {
        //setup
        TrackModel newTrack = new TrackModel() {{
            title = "spagetti!";
            offlineAvailable = true;
        }};
        this.sut = new PlaylistResource(playlistService, unauthorizedAuthenticationService);

        //act
        Response response = sut.addTrackToPlaylist(owner.token, 1, newTrack);
        PlayListsDTO responseEntity = (PlayListsDTO) response.getEntity();

        //assert
        Assert.assertNull(responseEntity);
        Assert.assertEquals(403, response.getStatus());
    }

    @Test
    public void removeTrackFromPlaylistReturns403WhenUserIsNotOwner() {
        //setup
        this.sut = new PlaylistResource(playlistService, unauthorizedAuthenticationService);

        //act
        Response response = sut.removeTrackFromPlaylist(owner.token, 1, 1);
        PlayListsDTO responseEntity = (PlayListsDTO) response.getEntity();

        //assert
        Assert.assertNull(responseEntity);
        Assert.assertEquals(403, response.getStatus());
    }
}
