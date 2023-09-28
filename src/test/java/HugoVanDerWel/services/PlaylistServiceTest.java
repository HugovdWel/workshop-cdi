package HugoVanDerWel.services;

import HugoVanDerWel.presentation.dataTransferObjects.PlayListsDTO;
import HugoVanDerWel.presentation.dataTransferObjects.TracksDTO;
import HugoVanDerWel.service.exceptions.UnauthorizedException;
import HugoVanDerWel.service.models.PlaylistModel;
import HugoVanDerWel.service.models.TrackModel;
import HugoVanDerWel.service.models.UserModel;
import HugoVanDerWel.data.persistence.PlaylistPersistence;
import HugoVanDerWel.service.services.PlaylistService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.*;

public class PlaylistServiceTest {
    private PlaylistService sut;
    private PlaylistPersistence playlistPersistence;
    private UserModel userModel = new UserModel() {{
        username = "Gert";
    }};
    private PlaylistModel[] playlistModel = new PlaylistModel[]{new PlaylistModel() {{
        name = "Playlistnaam1";
        id = 0;
    }}, new PlaylistModel() {{
        name = "Playlistnaam2";
        id = 1;
    }}, new PlaylistModel() {{
        name = "Playlistnaam3";
        id = 2;
    }},};
    private TrackModel[] trackmodels0 = new TrackModel[]{new TrackModel() {{
        title = "Song1";
        duration = 5;
    }}, new TrackModel() {{
        title = "Song2";
        duration = 15;
    }}, new TrackModel() {{
        title = "Song3";
        duration = 20;
    }}, new TrackModel() {{
        title = "Song4";
        duration = 25;
    }},};
    private TrackModel[] trackmodels1 = new TrackModel[]{new TrackModel() {{
        title = "Song4";
        duration = 30;
    }},};
    private TrackModel[] trackmodels2 = new TrackModel[]{};

    @Before
    public void setUp() {
        playlistPersistence = Mockito.mock(PlaylistPersistence.class);
        sut = new PlaylistService(playlistPersistence);
    }

    @Test
    public void getAllPlaylistsGetsPlaylistsFormsTotalPlayLengthCorrectly() {
        //Arrange
        when(playlistPersistence.getAllPlaylists(Mockito.any(UserModel.class))).thenReturn(playlistModel);
        when(playlistPersistence.getTracksInPlaylist(0)).thenReturn(trackmodels0);
        when(playlistPersistence.getTracksInPlaylist(1)).thenReturn(trackmodels1);
        when(playlistPersistence.getTracksInPlaylist(2)).thenReturn(trackmodels2);

        //Act
        PlayListsDTO response = sut.getAllPlaylists(new UserModel() {{
            username = "Karel";
        }});

        //Assert
        Assert.assertEquals(95, response.length);
    }

    @Test
    public void getAllPlaylistsGetsPlaylistsFormsPlayListsDTOCorrectly() {
        //Arrange
        when(playlistPersistence.getAllPlaylists(Mockito.any(UserModel.class))).thenReturn(playlistModel);
        when(playlistPersistence.getTracksInPlaylist(0)).thenReturn(trackmodels0);
        when(playlistPersistence.getTracksInPlaylist(1)).thenReturn(trackmodels1);
        when(playlistPersistence.getTracksInPlaylist(2)).thenReturn(trackmodels2);

        //Act
        PlayListsDTO response = sut.getAllPlaylists(new UserModel() {{
            username = "Karel";
        }});

        //Assert
        Assert.assertArrayEquals(trackmodels0, response.playlists[0].tracks);
        Assert.assertArrayEquals(trackmodels1, response.playlists[1].tracks);
        Assert.assertArrayEquals(trackmodels2, response.playlists[2].tracks);
        Assert.assertEquals(playlistModel[0].name, response.playlists[0].name);
        Assert.assertEquals(playlistModel[1].name, response.playlists[1].name);
        Assert.assertEquals(playlistModel[2].name, response.playlists[2].name);
    }

    @Test
    public void getAllPlaylistsRespondsWithoutCrashWhenThereAreNoPlaylists() {
        //Arrange
        when(playlistPersistence.getAllPlaylists(Mockito.any(UserModel.class))).thenReturn(new PlaylistModel[0]);
        when(playlistPersistence.getTracksInPlaylist(Mockito.anyInt())).thenThrow(RuntimeException.class);

        //Act
        PlayListsDTO response = sut.getAllPlaylists(new UserModel() {{
            username = "Karel";
        }});

        //Assert
        Assert.assertNull(response.playlists);
        Assert.assertEquals(0, response.length);
    }

    @Test
    public void deletePlaylistPassesCommandSuccessfully() {
        //Arrange

        //Act
        sut.deletePlaylist(1);

        //Assert
        verify(playlistPersistence, times(1)).deletePlaylist(1);
    }

    @Test
    public void createPlaylistPassesCommandSuccessfully() {
        //Arrange
        ArgumentCaptor<PlaylistModel> argument = ArgumentCaptor.forClass(PlaylistModel.class);

        //Act
        sut.createPlaylist(new PlaylistModel() {{
            name = "appel";
        }}, new UserModel() {{
            username = "Karel";
        }});

        //Assert
        Mockito.verify(playlistPersistence).createPlaylist(argument.capture());
        Assert.assertEquals("Karel", argument.getValue().ownerName);
        Assert.assertEquals("appel", argument.getValue().name);
    }

    @Test
    public void editPlaylistNamePassesCommandSuccessfully() {
        //Arrange
        ArgumentCaptor<PlaylistModel> argument = ArgumentCaptor.forClass(PlaylistModel.class);

        //Act
        sut.editPlaylistName("nieuwetest", 2);

        //Assert
        Mockito.verify(playlistPersistence).updatePlaylistName(argument.capture());
        Assert.assertEquals(2, argument.getValue().id);
        Assert.assertEquals("nieuwetest", argument.getValue().name);
    }

    @Test
    public void getTracksInPlaylistPassesCommandSuccessfully() {
        //Arrange
        when(playlistPersistence.getTracksInPlaylist(2)).thenReturn(trackmodels0);

        //Act
        TracksDTO response = sut.getTracksInPlaylist(2);

        //Assert
        Mockito.verify(playlistPersistence).getTracksInPlaylist(2);
        Assert.assertEquals(trackmodels0, response.tracks);
    }

    @Test
    public void getTracksNotInPlaylistPassesCommandSuccessfully() {
        //Arrange
        when(playlistPersistence.getTracksNotInPlaylist(2)).thenReturn(trackmodels0);

        //Act
        TracksDTO response = sut.getTracksNotInPlaylist(2);

        //Assert
        Mockito.verify(playlistPersistence).getTracksNotInPlaylist(2);
        Assert.assertEquals(trackmodels0, response.tracks);
    }

    @Test
    public void addTrackToPlaylistPassesCommandSuccessfully() {
        //Arrange
        when(playlistPersistence.getTracksNotInPlaylist(2)).thenReturn(trackmodels0);

        //Act
        sut.addTrackToPlaylist(2, new TrackModel() {{
            title = "MoooOOo";
            id = 55;
            offlineAvailable = true;
        }});

        //Assert
        Mockito.verify(playlistPersistence).addTrackToPlaylist(2, 55, true);
    }

    @Test
    public void removeTrackFromPlaylistPassesCommandSuccessfully() {
        //Arrange

        //Act
        sut.removeTrackFromPlaylist(2, 55);

        //Assert
        Mockito.verify(playlistPersistence).removeTrackFromPlaylist(2, 55);
    }

    @Test
    public void checkIfUserMayEditPlaylistApprovesCorrectly() {
        //Arrange
        when(playlistPersistence.getOwnerForPlaylist(2)).thenReturn(new UserModel() {{
            username = "Kevin";
        }});

        //Act
        sut.checkIfUserMayEditPlaylist(new UserModel() {{
            username = "Kevin";
        }}, 2);

        //Assert
    }

    @Test
    public void checkIfUserMayEditPlaylistDeclinesCorrectly() {
        //Arrange
        when(playlistPersistence.getOwnerForPlaylist(2)).thenReturn(new UserModel() {{
            username = "Caarrrlll";
        }});

        //Act
        try {
            sut.checkIfUserMayEditPlaylist(new UserModel() {{
                username = "Kevin";
            }}, 2);


            //Assert
            Assert.fail();
        } catch (UnauthorizedException ignored) {
        }
    }
}
