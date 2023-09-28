package HugoVanDerWel.data.mappers;

import HugoVanDerWel.service.models.TrackModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TrackMapper {

    public static TrackModel mapResultsetToTrackmodel(ResultSet resultSet) throws SQLException{
        return new TrackModel() {{
            id = resultSet.getInt("trackId");
            title = resultSet.getString("title");
            performer = resultSet.getString("performer");
            duration = resultSet.getInt("duration");
            album = resultSet.getString("album");
            publicationDate = resultSet.getDate("publicationdate");
            description = resultSet.getString("description");
            offlineAvailable = resultSet.getBoolean("offlineAvailable");
        }};
    }
}
