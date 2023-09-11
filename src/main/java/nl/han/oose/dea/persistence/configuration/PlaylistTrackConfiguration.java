package nl.han.oose.dea.persistence.configuration;

import nl.han.oose.dea.domain.entities.PlaylistTrack;
import nl.han.oose.dea.domain.entities.Track;
import nl.han.oose.dea.persistence.constants.TableNames;
import nl.han.oose.dea.persistence.shared.Property;

import java.util.Date;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class PlaylistTrackConfiguration extends TableConfigurationBase<PlaylistTrack> {
    public PlaylistTrackConfiguration() {
        super(TableNames.TRACKS, Logger.getLogger(PlaylistTrackConfiguration.class.getName()));

        properties.add(new Property<PlaylistTrack>("id")
                .setSetter((track, id) -> track.setId((String) id))
                .setGetter(Track::getId)
        );
        properties.add(new Property<PlaylistTrack>("performer")
                .setSetter((track, performer) -> track.setPerformer((String) performer))
                .setGetter(Track::getPerformer)
        );
        properties.add(new Property<PlaylistTrack>("title")
                .setSetter((track, title) -> track.setTitle((String) title))
                .setGetter(Track::getTitle)
        );
        properties.add(new Property<PlaylistTrack>("duration")
                .setSetter((track, duration) -> track.setDuration((int) duration))
                .setGetter(Track::getDuration)
        );
        properties.add(new Property<PlaylistTrack>("playcount")
                .setSetter((track, playcount) -> track.setPlaycount((int) playcount))
                .setGetter(Track::getPlaycount)
                .setNullable(true)
        );
        properties.add(new Property<PlaylistTrack>("description")
                .setSetter((track, description) -> track.setDescription((String) description))
                .setGetter(Track::getDescription)
                .setNullable(true)
        );
        properties.add(new Property<PlaylistTrack>("publication_date")
                .setSetter((track, publicationDate) -> track.setPublicationDate((Date) publicationDate))
                .setGetter(Track::getPublicationDate)
        );
    }

    @Override
    public Supplier<PlaylistTrack> entityFactory() {
        return PlaylistTrack::new;
    }
}
