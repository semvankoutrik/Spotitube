package nl.han.oose.dea;

import nl.han.oose.dea.persistence.utils.DatabaseConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseSetup {
    Connection connection = DatabaseConnection.create();

    @Test
    public void resetDatabase() throws SQLException {
        dropTables();
        createTables();
    }

    @Test
    public void dropTables() throws SQLException {
        connection.prepareStatement("DROP TABLE IF EXISTS tracks").execute();
        connection.prepareStatement("DROP TABLE IF EXISTS playlists").execute();
        connection.prepareStatement("DROP TABLE IF EXISTS users").execute();
    }

    @Test
    public void truncateTables() throws SQLException {
        connection.prepareStatement("DELETE FROM tracks").execute();
        connection.prepareStatement("DELETE FROM playlist").execute();
        connection.prepareStatement("DELETE FROM users").execute();
    }

    @Test
    public void createTables() throws SQLException {
        connection.prepareStatement("""
            CREATE TABLE users (
                id VARCHAR(255) PRIMARY KEY,
                name VARCHAR(255) NOT NULL
            )
        """).execute();
        connection.prepareStatement("""
            CREATE TABLE playlist (
                id VARCHAR(255) PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                owner_id VARCHAR(255) NOT NULL,
                CONSTRAINT fk_user FOREIGN KEY(owner_id) REFERENCES users(id)
            )
        """).execute();
    }
}
