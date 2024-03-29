package nl.han.oose.dea.persistence.configurations;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.PlaylistTrack;
import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.persistence.constants.TableNames;
import nl.han.oose.dea.persistence.shared.Property;
import nl.han.oose.dea.persistence.shared.Relations;

import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class PlaylistConfiguration extends TableConfigurationBase<Playlist> {
    public PlaylistConfiguration() {
        super(TableNames.PLAYLISTS, Logger.getLogger(PlaylistConfiguration.class.getName()));

        properties.add(new Property<Playlist>("id")
                .setSetter((playlist, id) -> playlist.setId((String) id))
                .setGetter(Playlist::getId)
        );
        properties.add(new Property<Playlist>("name")
                .setSetter((playlist, name) -> playlist.setName((String) name))
                .setGetter(Playlist::getName)
        );
        properties.add(Relations.hasOne("owner_id", TableNames.USERS, "id", new UserConfiguration(), Playlist.class)
                .setSetter((playlist, id) -> {
                    User user = new User();
                    user.setId((String) id);
                    playlist.setOwner(user);
                })
                .setGetter(p -> p.getOwner().getId())
        );
        relations.add(Relations.hasManyThrough("tracks", TableNames.PLAYLIST_TRACKS, "playlist_id", "track_id", TableNames.TRACKS, "id", new PlaylistTrackConfiguration(), Playlist.class)
                .setSetter((playlist, tracks) -> playlist.setTracks((List<PlaylistTrack>) tracks))
                .setGetter(Playlist::getTracks)
                .addProperty(new Property<PlaylistTrack>("offline_available")
                        .setGetter(PlaylistTrack::isOfflineAvailable)
                        .setSetter((playlistTrack, offlineAvailable) -> playlistTrack.setOfflineAvailable((boolean) offlineAvailable))
                )
        );
    }

    @Override
    public Supplier<Playlist> entityFactory() {
        return Playlist::new;
    }
}
