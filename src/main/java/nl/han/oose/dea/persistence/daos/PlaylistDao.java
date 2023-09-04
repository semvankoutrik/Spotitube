package nl.han.oose.dea.persistence.daos;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.persistence.constants.RelationType;
import nl.han.oose.dea.persistence.constants.TableNames;
import nl.han.oose.dea.persistence.shared.Property;
import nl.han.oose.dea.presentation.interfaces.daos.IPlaylistDao;

import java.util.function.Supplier;
import java.util.logging.Logger;

public class PlaylistDao extends BaseDao<Playlist> implements IPlaylistDao {
    public PlaylistDao() {
        super(TableNames.PLAYLISTS, Logger.getLogger(PlaylistDao.class.getName()));

        properties.add(new Property<Playlist>("id")
                .setSetter((playlist, id) -> playlist.setId((String) id))
                .setGetter(Playlist::getId)
        );
        properties.add(new Property<Playlist>("name")
                .setSetter((playlist, name) -> playlist.setName((String) name))
                .setGetter(Playlist::getName)
        );
        properties.add(new Property<Playlist>("owner_id")
                .setSetter((playlist, id) -> {
                    User user = new User();
                    user.setId((String) id);
                    playlist.setOwner(user);
                })
                .setGetter((playlist) -> playlist.getOwner().getId())
                .setRelationType(RelationType.MANY_TO_ONE)
        );
    }

    @Override
    protected Supplier<Playlist> entityFactory() {
        return Playlist::new;
    }
}
