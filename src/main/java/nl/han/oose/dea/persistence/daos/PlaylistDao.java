package nl.han.oose.dea.persistence.daos;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.persistence.configurations.PlaylistConfiguration;
import nl.han.oose.dea.domain.exceptions.DatabaseException;
import nl.han.oose.dea.domain.exceptions.NotFoundException;
import nl.han.oose.dea.persistence.utils.Filter;
import nl.han.oose.dea.persistence.interfaces.daos.IPlaylistDao;

import java.util.List;
import java.util.logging.Logger;

public class PlaylistDao extends DaoBase<Playlist> implements IPlaylistDao {
    public PlaylistDao() {
        super(new PlaylistConfiguration(), Logger.getLogger(PlaylistDao.class.getName()));
    }

    @Override
    public List<Playlist> get() throws DatabaseException {
        include("tracks");

        return super.get();
    }

    @Override
    public Playlist get(String id) throws NotFoundException, DatabaseException {
        include("tracks");

        return super.get(id);
    }

    @Override
    public List<Playlist> get(Filter filter) throws DatabaseException {
        include("tracks");

        return super.get(filter);
    }
}
