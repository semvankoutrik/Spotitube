package nl.han.oose.dea.persistence.daos;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.persistence.configurations.PlaylistConfiguration;
import nl.han.oose.dea.persistence.utils.DatabaseConnection;
import nl.han.oose.dea.persistence.interfaces.daos.IPlaylistDao;

import java.sql.Connection;
import java.util.logging.Logger;

public class PlaylistDao extends DaoBase<Playlist> implements IPlaylistDao {

    public PlaylistDao() {
        this(DatabaseConnection.create());
    }

    public PlaylistDao(Connection connection) {
        super(new PlaylistConfiguration(), Logger.getLogger(PlaylistDao.class.getName()), connection);
    }
}
