package HugoVanDerWel.services;

import HugoVanDerWel.dataTransferObjects.PlayListsDTO;
import HugoVanDerWel.models.PlaylistModel;
import HugoVanDerWel.models.TrackModel;
import HugoVanDerWel.models.UserModel;
import HugoVanDerWel.persistence.PlaylistPersistence;
import jakarta.inject.Inject;

import java.util.Arrays;

public class PlaylistService {

    @Inject
    private PlaylistPersistence playlistPersistence;

    public PlayListsDTO getAllPlaylists(UserModel owner) {
        PlaylistModel[] playlistModels = playlistPersistence.getAllPlaylists(owner);
        if(playlistModels.length == 0){
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

    public void createPlaylist(PlaylistModel inputPlaylistModel) {
        playlistPersistence.createPlaylist(
                new PlaylistModel() {{
                    id = 0;
                    name = inputPlaylistModel.name;
                    tracks = inputPlaylistModel.tracks;
                }});
    }

    public void replacePlaylist(PlaylistModel newPlaylistModel, int playlistId) {
        this.deletePlaylist(playlistId);
        newPlaylistModel.id = playlistId;
        this.createPlaylist(newPlaylistModel);
    }

    public TrackModel[] getTracksInPlaylist(int playlistId) {
        return playlistPersistence.getTracksInPlaylist(playlistId);
    }

    public void addTrackToPlaylist(int playlistId, TrackModel track) {
        this.playlistPersistence.addTrackToPlaylist(playlistId, track);
    }

    public void removeTrackFromPlaylist(int playlistId, int trackId) {
        this.playlistPersistence.removeTrackFromPlaylist(playlistId, trackId);
    }
}
