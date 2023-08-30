package HugoVanDerWel.persistence;

import HugoVanDerWel.Models.PlaylistModel;
import HugoVanDerWel.Models.TrackModel;
import HugoVanDerWel.Models.UserModel;
import HugoVanDerWel.services.Database;
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
            while(results.next()){
                playlists.add(new PlaylistModel(){{
                    owner = Objects.equals(results.getString("owner"), currentUser.username);
                    name = results.getString("name");
                    id = results.getInt("id");
                }});
            }
            return playlists.toArray(PlaylistModel[]::new);
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public void deletePlaylist(int id) {
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement(
                    "DELETE FROM Playlist WHERE PLAYLISTID = ?");
            query.setInt(1, id);
            query.executeUpdate();
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public void createPlaylist(PlaylistModel playlistModel) {
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement(
                    "INSERT INTO Playlist (playlistname, username) VALUES (?, ?)");
            query.setString(1, playlistModel.name);
            query.setString(2, String.valueOf(playlistModel.ownerName));
            query.executeUpdate();
        }
        catch(RuntimeException | SQLException e){
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public TrackModel[] getTracksInPlaylist(int playlistId) {
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement(
                    "SELECT trackId, title, performer, duration, album, publicationdate, description  FROM Track t left join Track_in_playlist p ON t.trackId = p.trackId WHERE playlistId = ?");
            query.setInt(1, playlistId);
            ResultSet resultset = query.executeQuery();
            List<TrackModel> tracks = new ArrayList<>();
            while(resultset.next()) {
                tracks.add(new TrackModel(){{
                    id = resultset.getInt("trackId");
                    title = resultset.getString("title");
                    performer = resultset.getString("performer");
                    duration = resultset.getInt("duration");
                    album = resultset.getString("album");
//                    playcount = resultset.get;
                    date = resultset.getDate("publicationdate");
                    description = resultset.getString("description");
//                    offlineAvailable = resultset.get;
                }});
            }
            return tracks.toArray(TrackModel[]::new);
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public void addTrackToPlaylist(int playlistId, TrackModel track) {
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement("INSERT INTO Track_in_playlist (playlistId, trackId, offlineAvailable, playCount) VALUES (?, ?, ?, ?)");
            query.setInt(1, playlistId);
            query.setInt(2, track.id);
            query.setBoolean(3, track.offlineAvailable);
            query.setInt(4, track.playcount);
            query.executeUpdate();
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    public void removeTrackFromPlaylist(int playlistId, int trackId) {
        try {
            Connection connection = db.getConnection();
            PreparedStatement query = connection.prepareStatement("DELETE FROM Track_in_playlist WHERE playlistId = ? AND trackId = ?");
            query.setInt(1, playlistId);
            query.setInt(2, trackId);
            query.executeUpdate();
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }
}
