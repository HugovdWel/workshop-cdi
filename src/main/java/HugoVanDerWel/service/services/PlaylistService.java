package HugoVanDerWel.service.services;

import HugoVanDerWel.presentation.dataTransferObjects.PlayListsDTO;
import HugoVanDerWel.presentation.dataTransferObjects.TracksDTO;
import HugoVanDerWel.service.exceptions.UnauthorizedException;
import HugoVanDerWel.service.models.PlaylistModel;
import HugoVanDerWel.service.models.TrackModel;
import HugoVanDerWel.service.models.UserModel;
import HugoVanDerWel.data.persistence.PlaylistPersistence;
import jakarta.inject.Inject;

public class PlaylistService {

    private PlaylistPersistence playlistPersistence;

    @Inject
    public PlaylistService(PlaylistPersistence playlistPersistence) {
        this.playlistPersistence = playlistPersistence;
    }

    public PlayListsDTO getAllPlaylists(UserModel owner) {
        PlaylistModel[] playlistModels = playlistPersistence.getAllPlaylists(owner);
        if (playlistModels.length == 0) {
            return new PlayListsDTO();
        }
        int totalPlayLength = 0;

        for (PlaylistModel playlistModel : playlistModels) {
            int playlistPlayLength = 0;
            playlistModel.tracks = playlistPersistence.getTracksInPlaylist(playlistModel.id);
            for (TrackModel trackModel : playlistModel.tracks) {
                playlistPlayLength += trackModel.duration;
            }
            totalPlayLength += playlistPlayLength;
        }

        int finalTotalPlayLength = totalPlayLength;
        return new PlayListsDTO() {{
            playlists = playlistModels;
            length = finalTotalPlayLength;
        }};
    }

    public void deletePlaylist(int id) {
        playlistPersistence.deletePlaylist(id);
    }

    public void createPlaylist(PlaylistModel inputPlaylistModel, UserModel playlistOwner) {
        playlistPersistence.createPlaylist(new PlaylistModel() {{
            ownerName = playlistOwner.username;
            name = inputPlaylistModel.name;
        }});
    }

    public void editPlaylistName(String newName, int playlistId) {
        playlistPersistence.updatePlaylistName(new PlaylistModel() {{
            id = playlistId;
            name = newName;
        }});
    }

    public TracksDTO getTracksInPlaylist(int playlistId) {
        return new TracksDTO() {{
            tracks = playlistPersistence.getTracksInPlaylist(playlistId);
        }};
    }

    public TracksDTO getTracksNotInPlaylist(int playlistId) {
        return new TracksDTO() {{
            tracks = playlistPersistence.getTracksNotInPlaylist(playlistId);
        }};
    }

    public void addTrackToPlaylist(int playlistId, TrackModel track) {
        this.playlistPersistence.addTrackToPlaylist(playlistId, track.id, track.offlineAvailable);
    }

    public void removeTrackFromPlaylist(int playlistId, int trackId) {
        this.playlistPersistence.removeTrackFromPlaylist(playlistId, trackId);
    }

    public void checkIfUserMayEditPlaylist(UserModel user, int playlistId) {
        if (!this.playlistPersistence.getOwnerForPlaylist(playlistId).username.equals(user.username)) {
            throw new UnauthorizedException();
        }
    }
}
