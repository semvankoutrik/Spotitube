package nl.han.oose.dea.persistence.daos;

import nl.han.oose.dea.domain.entities.Track;
import nl.han.oose.dea.persistence.configurations.TrackConfiguration;
import nl.han.oose.dea.persistence.interfaces.daos.ITrackDao;

import java.util.logging.Logger;

public class TrackDao extends DaoBase<Track> implements ITrackDao {
    public TrackDao() {
        super(new TrackConfiguration(), Logger.getLogger(TrackDao.class.getName()));
    }
}
