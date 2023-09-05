package nl.han.oose.dea.persistence.configuration;

import nl.han.oose.dea.domain.entities.Track;
import nl.han.oose.dea.persistence.constants.TableNames;
import nl.han.oose.dea.persistence.shared.Property;

import java.util.function.Supplier;

public class TrackConfiguration extends TableConfigurationBase<Track> {
    public TrackConfiguration() {
        super(TableNames.TRACKS);

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
    }

    @Override
    protected Supplier<Track> entityFactory() {
        return Track::new;
    }
}
