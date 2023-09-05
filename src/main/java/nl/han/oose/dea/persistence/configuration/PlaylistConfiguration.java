package nl.han.oose.dea.persistence.configuration;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.Track;
import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.persistence.constants.TableNames;
import nl.han.oose.dea.persistence.shared.Property;
import nl.han.oose.dea.persistence.shared.Relation;

import java.util.List;
import java.util.function.Supplier;

public class PlaylistConfiguration extends TableConfigurationBase<Playlist> {
    public PlaylistConfiguration() {
        super(TableNames.PLAYLISTS);

        properties.add(new Property<Playlist>("id")
                .setSetter((playlist, id) -> playlist.setId((String) id))
                .setGetter(Playlist::getId)
        );
        properties.add(new Property<Playlist>("name")
                .setSetter((playlist, name) -> playlist.setName((String) name))
                .setGetter(Playlist::getName)
        );
        properties.add(Relation.hasOne("owner_id", TableNames.USERS, "id", new UserConfiguration(), Playlist.class)
                .setSetter((playlist, id) -> {
                    User user = new User();
                    user.setId((String) id);
                    playlist.setOwner(user);
                })
                .setGetter(p -> p.getOwner().getId())
        );
        relations.add(Relation.hasManyThrough("tracks", TableNames.PLAYLIST_TRACKS, "playlist_id", "track_id", TableNames.TRACKS, "id", new TrackConfiguration(), Playlist.class)
                .setSetter((playlist, tracks) -> playlist.setTracks((List<Track>) tracks))
                .setGetter(Playlist::getTracks)
                .setIgnoreIfNull(true)
        );
    }

    @Override
    protected Supplier<Playlist> entityFactory() {
        return Playlist::new;
    }
}
