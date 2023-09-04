package nl.han.oose.dea.persistence.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());

    public static Connection create() {
        var properties = new Properties();

        try {
            properties.load(DatabaseConnection.class.getClassLoader().getResourceAsStream("database.properties"));

            return DriverManager.getConnection(properties.getProperty("connectionString"));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Can't access database.properties.", e);

            throw new RuntimeException();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Something went wrong trying to connect to the database.", e);

            throw new RuntimeException(e);
        }
    }
}
