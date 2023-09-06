package nl.han.oose.dea.persistence.daos;

import nl.han.oose.dea.domain.entities.Track;
import nl.han.oose.dea.persistence.configuration.TrackConfiguration;

import java.util.logging.Logger;

public class TrackDao extends DaoBase<Track> {
    public TrackDao() {
        super(new TrackConfiguration(), Logger.getLogger(TrackDao.class.getName()));
    }
}
