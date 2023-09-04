package nl.han.oose.dea.persistence.daos;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.persistence.constants.TableNames;
import nl.han.oose.dea.persistence.shared.Field;
import nl.han.oose.dea.presentation.interfaces.daos.IPlaylistDao;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlaylistDao extends BaseDao<Playlist> implements IPlaylistDao {
    public PlaylistDao() {
        super(TableNames.PLAYLISTS, Logger.getLogger(PlaylistDao.class.getName()));

        properties.put("id", new Field<>((playlist, id) -> playlist.setId((String) id), (Playlist::getId)));
        properties.put("name", new Field<>((playlist, name) -> playlist.setName((String) name), (Playlist::getName)));
    }

    @Override
    protected Supplier<Playlist> entityFactory() {
        return Playlist::new;
    }
}
