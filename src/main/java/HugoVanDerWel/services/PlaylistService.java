package HugoVanDerWel.services;

import HugoVanDerWel.DataTransferObjects.PlayListsDTO;
import HugoVanDerWel.Models.PlaylistModel;
import HugoVanDerWel.Models.TrackModel;
import HugoVanDerWel.Models.UserModel;
import HugoVanDerWel.persistence.PlaylistPersistence;
import jakarta.inject.Inject;

public class PlaylistService {

    @Inject
    private PlaylistPersistence playlistPersistence;

    public PlayListsDTO getAllPlaylists(UserModel owner) {
        PlaylistModel[] playlistModels = playlistPersistence.getAllPlaylists(owner);
        return new PlayListsDTO() {{
            playlists = playlistModels;
            length = playlists.length;
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
