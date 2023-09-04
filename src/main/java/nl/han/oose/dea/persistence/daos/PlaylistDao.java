package nl.han.oose.dea.persistence.daos;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.persistence.constants.TableNames;
import nl.han.oose.dea.persistence.shared.Column;
import nl.han.oose.dea.presentation.interfaces.daos.IPlaylistDao;

import java.util.function.Supplier;
import java.util.logging.Logger;

public class PlaylistDao extends BaseDao<Playlist> implements IPlaylistDao {
    public PlaylistDao() {
        super(TableNames.PLAYLISTS, Logger.getLogger(PlaylistDao.class.getName()));

        columns.put("id", new Column<>((playlist, id) -> playlist.setId((String) id), (Playlist::getId)));
        columns.put("name", new Column<>((playlist, name) -> playlist.setName((String) name), (Playlist::getName)));
    }

    @Override
    protected Supplier<Playlist> entityFactory() {
        return Playlist::new;
    }
}
