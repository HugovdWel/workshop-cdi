package HugoVanDerWel.resources;

import HugoVanDerWel.dataTransferObjects.PlayListsDTO;
import HugoVanDerWel.dataTransferObjects.TracksDTO;
import HugoVanDerWel.models.TrackModel;
import HugoVanDerWel.services.AuthenticationService;
import HugoVanDerWel.services.PlaylistService;
import HugoVanDerWel.support.TestAuthenticationSupport;
import jakarta.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TrackResourceTest {
    private TrackResource sut;
    private AuthenticationService successfulAuthenticationServiceMock;
    private AuthenticationService incorrectAuthenticationServiceMock;
    private PlaylistService playlistServiceMock;

    @Before
    public void setUp() {
        successfulAuthenticationServiceMock = TestAuthenticationSupport.getSuccessfullLoginAuthenticationService();
        incorrectAuthenticationServiceMock = TestAuthenticationSupport.getIncorrectLoginAuthenticationService();
        playlistServiceMock = Mockito.mock(PlaylistService.class);
    }

    @Test
    public void getAvailableTracksForPlaylistReturnsCorrectData() {
        //setup
        TracksDTO expectedResponse = new TracksDTO() {{
            tracks = new TrackModel[]{new TrackModel() {{
                title = "mock";
            }}};
        }};
        Mockito.when(playlistServiceMock.getTracksNotInPlaylist(32)).thenReturn(expectedResponse);
        sut = new TrackResource(playlistServiceMock, successfulAuthenticationServiceMock);

        //act
        Response response = sut.getAvailableTracksForPlaylist("123", 32);
        TracksDTO responseEntity = (TracksDTO) response.getEntity();

        //assert
        Assert.assertEquals(expectedResponse, responseEntity);
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void getAvailableTracksForPlaylistReturnsEmptyResponse() {
        //setup
        TracksDTO expectedResponse = new TracksDTO();
        Mockito.when(playlistServiceMock.getTracksNotInPlaylist(32)).thenReturn(expectedResponse);
        sut = new TrackResource(playlistServiceMock, successfulAuthenticationServiceMock);

        //act
        Response response = sut.getAvailableTracksForPlaylist("123", 32);
        TracksDTO responseEntity = (TracksDTO) response.getEntity();

        //assert
        Assert.assertEquals(expectedResponse, responseEntity);
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void getAvailableTracksForPlaylistGives403WhenTokenInvalid() {
        //setup
        TracksDTO expectedResponse = new TracksDTO();
        Mockito.when(playlistServiceMock.getTracksNotInPlaylist(32)).thenReturn(expectedResponse);
        sut = new TrackResource(playlistServiceMock, incorrectAuthenticationServiceMock);

        //act
        Response response = sut.getAvailableTracksForPlaylist("123", 32);
        TracksDTO responseEntity = (TracksDTO) response.getEntity();

        //assert
        Assert.assertNull(responseEntity);
        Assert.assertEquals(403, response.getStatus());
    }
}
