package nl.han.oose.dea.persistence.configurations;

import nl.han.oose.dea.domain.entities.Track;
import nl.han.oose.dea.persistence.constants.TableNames;
import nl.han.oose.dea.persistence.shared.Property;
import nl.han.oose.dea.persistence.shared.Relations;

import java.util.Date;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class TrackConfiguration extends TableConfigurationBase<Track> {
    public TrackConfiguration() {
        super(TableNames.TRACKS, Logger.getLogger(TrackConfiguration.class.getName()));

        properties.add(new Property<Track>("id")
                .setSetter((track, id) -> track.setId((String) id))
                .setGetter(Track::getId)
        );
        properties.add(new Property<Track>("performer")
                .setSetter((track, performer) -> track.setPerformer((String) performer))
                .setGetter(Track::getPerformer)
        );
        properties.add(new Property<Track>("title")
                .setSetter((track, title) -> track.setTitle((String) title))
                .setGetter(Track::getTitle)
        );
        properties.add(new Property<Track>("duration")
                .setSetter((track, duration) -> track.setDuration((int) duration))
                .setGetter(Track::getDuration)
        );
        properties.add(new Property<Track>("playcount")
                .setSetter((track, playcount) -> track.setPlaycount((int) playcount))
                .setGetter(Track::getPlaycount)
        );
        properties.add(new Property<Track>("description")
                .setSetter((track, description) -> track.setDescription((String) description))
                .setGetter(Track::getDescription)
        );
        properties.add(new Property<Track>("publication_date")
                .setSetter((track, publicationDate) -> track.setPublicationDate((Date) publicationDate))
                .setGetter(Track::getPublicationDate)
                .setNullable(true)
        );
        relations.add(Relations.hasManyThrough("playlists", TableNames.PLAYLIST_TRACKS, "track_id", "playlist_id", TableNames.PLAYLISTS, "id", new PlaylistConfiguration(), Track.class));
    }

    @Override
    public Supplier<Track> entityFactory() {
        return Track::new;
    }
}
