package nl.han.oose.dea;

import nl.han.oose.dea.persistence.utils.DatabaseConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseSetup {
    final Connection connection = DatabaseConnection.create();

    @Test
    public void resetDatabase() throws SQLException {
        dropTables();
        createTables();
    }

    @Test
    public void dropTables() throws SQLException {
        connection.prepareStatement("DROP TABLE IF EXISTS playlist_tracks").execute();
        connection.prepareStatement("DROP TABLE IF EXISTS tracks").execute();
        connection.prepareStatement("DROP TABLE IF EXISTS playlists").execute();
        connection.prepareStatement("DROP TABLE IF EXISTS users").execute();
    }

    @Test
    public void truncateTables() throws SQLException {
        connection.prepareStatement("DELETE FROM playlist_tracks").execute();
        connection.prepareStatement("DELETE FROM tracks").execute();
        connection.prepareStatement("DELETE FROM playlists").execute();
        connection.prepareStatement("DELETE FROM users").execute();
    }

    @Test
    public void createTables() throws SQLException {
        connection.prepareStatement("""
            CREATE TABLE users (
                id VARCHAR(255) PRIMARY KEY,
                username VARCHAR(255) NOT NULL,
                first_name VARCHAR(255) NOT NULL,
                last_name VARCHAR(255) NOT NULL,
                password_hash VARCHAR(255) NOT NULL,
                password_salt VARCHAR(255) NOT NULL
            )
        """).execute();
        connection.prepareStatement("""
            CREATE TABLE playlists (
                id VARCHAR(255) PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                owner_id VARCHAR(255) NOT NULL,
                CONSTRAINT fk_user FOREIGN KEY(owner_id) REFERENCES users(id)
            )
        """).execute();
        connection.prepareStatement("""
            CREATE TABLE tracks (
                id VARCHAR(255) PRIMARY KEY,
                performer VARCHAR(255) NOT NULL,
                title VARCHAR(255) NOT NULL,
                duration int NOT NULL,
                album VARCHAR(255),
                playcount int,
                publication_date DATE,
                description VARCHAR(255)
            )
        """).execute();
        connection.prepareStatement("""
            CREATE TABLE playlist_tracks (
                playlist_id VARCHAR(255) NOT NULL,
                track_id VARCHAR(255) NOT NULL,
                offline_available BOOLEAN NOT NULL,
                CONSTRAINT fk_playlist FOREIGN KEY(playlist_id) REFERENCES playlists(id),
                CONSTRAINT fk_track FOREIGN KEY(track_id) REFERENCES tracks(id)
            )
        """).execute();
    }
}
