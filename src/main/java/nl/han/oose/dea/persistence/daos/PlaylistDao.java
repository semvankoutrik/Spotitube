package nl.han.oose.dea.persistence.daos;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.persistence.constants.ColumnTypes;
import nl.han.oose.dea.persistence.constants.TableNames;
import nl.han.oose.dea.persistence.shared.Property;
import nl.han.oose.dea.presentation.interfaces.daos.IPlaylistDao;

import java.util.function.Supplier;
import java.util.logging.Logger;

public class PlaylistDao extends BaseDao<Playlist> implements IPlaylistDao {
    public PlaylistDao() {
        super(TableNames.PLAYLISTS, Logger.getLogger(PlaylistDao.class.getName()));

        properties.add(new Property<>("id", ColumnTypes.VALUE, (playlist, id) -> playlist.setId((String) id), (Playlist::getId)));
        properties.add(new Property<>("name", ColumnTypes.VALUE, (playlist, name) -> playlist.setName((String) name), (Playlist::getName)));
        properties.add(new Property<>("owner_id", ColumnTypes.TO_ONE, (playlist, id) -> {
            User user = new User();
            user.setId((String) id);
            playlist.setOwner(user);
        }, (playlist) -> playlist.getOwner().getId()));
    }

    @Override
    protected Supplier<Playlist> entityFactory() {
        return Playlist::new;
    }
}
