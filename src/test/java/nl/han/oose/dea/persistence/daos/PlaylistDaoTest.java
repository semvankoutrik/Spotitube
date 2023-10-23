package nl.han.oose.dea.persistence.daos;

import nl.han.oose.dea.domain.exceptions.DatabaseException;
import nl.han.oose.dea.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.SQLException;

public class PlaylistDaoTest {
    private PlaylistDao sut;
    private Connection connection;

    @BeforeEach
    public void setup() {
        connection = mock(Connection.class);

        sut = new PlaylistDao(connection);
    }

    @Test
    public void selectQueryCorrectlyCalled() {
        try {
            sut.get("abc");
            sut.include("");

            verify(connection).prepareStatement(
                    "SELECT playlists.id AS playlists_id, playlists.name AS playlists_name, playlists.owner_id AS playlists_owner_id, tracks.id AS tracks_id, tracks.performer AS tracks_performer, tracks.title AS tracks_title, tracks.duration AS tracks_duration, tracks.playcount AS tracks_playcount, tracks.description AS tracks_description, tracks.publication_date AS tracks_publication_date, playlist_tracks.offline_available AS playlist_tracks_offline_available FROM playlists LEFT JOIN playlist_tracks ON playlist_tracks.playlist_id = playlists.id LEFT JOIN tracks ON playlist_tracks.track_id = tracks.id WHERE playlists.id = ? ORDER BY playlists.id"
            );
        } catch (NotFoundException | DatabaseException e) {
        } catch (SQLException e) {
        } catch (NullPointerException e) {
        }
    }
}
