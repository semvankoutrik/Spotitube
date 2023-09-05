package nl.han.oose.dea.persistence.daos;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.Track;
import nl.han.oose.dea.persistence.configuration.PlaylistConfiguration;
import nl.han.oose.dea.persistence.configuration.TrackConfiguration;
import nl.han.oose.dea.persistence.exceptions.DatabaseException;
import nl.han.oose.dea.presentation.interfaces.daos.IPlaylistDao;

import java.util.List;
import java.util.logging.Logger;

public class TrackDao extends DaoBase<Track> {
    public TrackDao() {
        super(new TrackConfiguration(), Logger.getLogger(TrackDao.class.getName()));
    }
}
