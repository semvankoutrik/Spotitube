package nl.han.oose.dea.persistence.daos;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.persistence.configuration.PlaylistConfiguration;
import nl.han.oose.dea.persistence.exceptions.DatabaseException;
import nl.han.oose.dea.presentation.interfaces.daos.IPlaylistDao;

import java.util.List;
import java.util.function.Supplier;
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
    protected Supplier<Playlist> entityFactory() {
        return Playlist::new;
    }
}
