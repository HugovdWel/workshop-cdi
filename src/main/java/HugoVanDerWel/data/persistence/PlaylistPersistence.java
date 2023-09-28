package HugoVanDerWel.data.persistence;

import HugoVanDerWel.data.mappers.TrackMapper;
import HugoVanDerWel.service.models.PlaylistModel;
import HugoVanDerWel.service.models.TrackModel;
import HugoVanDerWel.service.models.UserModel;
import HugoVanDerWel.service.services.Database;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlaylistPersistence {

    private Database db;

    @Inject
    public PlaylistPersistence(Database db) {
        this.db = db;
    }

    public PlaylistModel[] getAllPlaylists(UserModel currentUser) {
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement("SELECT owner, name, id FROM Playlist");
            ResultSet results = query.executeQuery();

            List<PlaylistModel> playlists = new ArrayList<>();
            while (results.next()) {
                playlists.add(new PlaylistModel() {{
                    owner = Objects.equals(results.getString("owner"), currentUser.username);
                    name = results.getString("name");
                    id = results.getInt("id");
                }});
            }
            if(playlists.isEmpty()) return new PlaylistModel[]{};
            connection.close();
            return playlists.toArray(new PlaylistModel[0]);
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException("A database error has occurred.");
        }
    }

    public void deletePlaylist(int id) {
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement("DELETE FROM Playlist WHERE ID = ?");
            query.setInt(1, id);
            query.executeUpdate();
            connection.close();
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException("A database error has occurred.");
        }
    }

    public void createPlaylist(PlaylistModel playlistModel) {
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement("INSERT INTO Playlist (name, owner) VALUES (?, ?)");
            query.setString(1, playlistModel.name);
            query.setString(2, playlistModel.ownerName);
            query.executeUpdate();
            connection.close();
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException("A database error has occurred.");
        }
    }

    public void updatePlaylistName(PlaylistModel playlistModel) {
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement("UPDATE Playlist SET name = ? WHERE id = ?");
            query.setString(1, playlistModel.name);
            query.setInt(2, playlistModel.id);
            query.executeUpdate();
            connection.close();
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException("A database error has occurred.");
        }
    }

    public TrackModel[] getTracksInPlaylist(int playlistId) {
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement("SELECT t.trackId, title, performer, duration, album, publicationdate, description, p.offlineAvailable  FROM Track t left join Track_in_playlist p ON t.trackId = p.trackId WHERE playlistId = ?");
            query.setInt(1, playlistId);
            ResultSet resultset = query.executeQuery();
            List<TrackModel> tracks = new ArrayList<>();
            while (resultset.next()) {
                tracks.add(TrackMapper.mapResultsetToTrackmodel(resultset));
            }
            if(tracks.isEmpty()) return new TrackModel[]{};
            connection.close();
            return tracks.toArray(new TrackModel[0]);
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException("A database error has occurred.");
        }
    }

    public TrackModel[] getTracksNotInPlaylist(int playlistId) {
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement("SELECT t.trackId, title, performer, duration, album, publicationdate, description, 0 as 'offlineAvailable' FROM Track t WHERE trackId NOT IN (SELECT trackId FROM Track_in_playlist where playlistId = ?)");
            query.setInt(1, playlistId);
            ResultSet resultset = query.executeQuery();
            List<TrackModel> tracks = new ArrayList<>();
            while (resultset.next()) {
                tracks.add(TrackMapper.mapResultsetToTrackmodel(resultset));
            }
            if(tracks.isEmpty()) return new TrackModel[]{};
            connection.close();
            return tracks.toArray(new TrackModel[0]);
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException("A database error has occurred.");
        }
    }

    public void addTrackToPlaylist(int playlistId, int trackId, boolean offlineAvailable) {
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement("INSERT INTO Track_in_playlist (playlistId, trackId, offlineAvailable) VALUES (?, ?, ?)");
            query.setInt(1, playlistId);
            query.setInt(2, trackId);
            query.setBoolean(3, offlineAvailable);
            query.executeUpdate();
            connection.close();
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException("A database error has occurred.");
        }
    }

    public void removeTrackFromPlaylist(int playlistId, int trackId) {
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement("DELETE FROM Track_in_playlist WHERE playlistId = ? AND trackId = ?");
            query.setInt(1, playlistId);
            query.setInt(2, trackId);
            query.executeUpdate();
            connection.close();
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException("A database error has occurred.");
        }
    }

    public UserModel getOwnerForPlaylist(int playlistId) {
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement("SELECT owner FROM Playlist WHERE id = ?");
            query.setInt(1, playlistId);
            ResultSet resultset = query.executeQuery();
            UserModel owner = new UserModel();
            while (resultset.next()) {
                owner.username = resultset.getString("owner");
            }
            connection.close();
            return owner;
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException("A database error has occurred.");
        }
    }
}
