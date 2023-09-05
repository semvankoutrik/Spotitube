package nl.han.oose.dea.persistence.configuration;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.Track;
import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.persistence.constants.TableNames;
import nl.han.oose.dea.persistence.shared.Property;
import nl.han.oose.dea.persistence.shared.Relation;

import java.util.ArrayList;
import java.util.List;

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
        properties.add(new Property<Playlist>("owner_id")
                .setSetter((playlist, id) -> {
                    User user = new User();
                    user.setId((String) id);
                    playlist.setOwner(user);
                })
                .setGetter((playlist) -> playlist.getOwner().getId())
                .setRelation(Relation.hasOne(TableNames.USERS, "id"))
        );
        properties.add(new Property<Playlist>("tracks")
                .setSetter((playlist, tracks) -> playlist.setTracks((List<Track>) tracks))
                .setGetter(Playlist::getTracks)
                .setRelation(Relation.hasManyThrough(TableNames.PLAYLIST_TRACKS, "playlist_id", "track_id", TableNames.TRACKS, "id"))
                .setIgnoreIfNull(true)
        );
    }
}
