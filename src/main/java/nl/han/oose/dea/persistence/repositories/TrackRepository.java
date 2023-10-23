package nl.han.oose.dea.persistence.repositories;

import jakarta.inject.Inject;
import nl.han.oose.dea.domain.entities.Track;
import nl.han.oose.dea.domain.exceptions.DatabaseException;
import nl.han.oose.dea.domain.exceptions.NotFoundException;
import nl.han.oose.dea.domain.interfaces.ITrackRepository;
import nl.han.oose.dea.persistence.interfaces.daos.ITrackDao;
import nl.han.oose.dea.persistence.utils.Filter;
import nl.han.oose.dea.persistence.utils.Join;

import java.util.List;

public class TrackRepository implements ITrackRepository {
    private ITrackDao trackDao;

    @Inject
    public void setTrackDao(ITrackDao trackDao) {
        this.trackDao = trackDao;
    }

    @Override
    public List<Track> getAll() throws DatabaseException {
        return trackDao.get();
    }

    @Override
    public Track get(String id) throws NotFoundException, DatabaseException {
        return trackDao.get(id);
    }

    @Override
    public List<Track> getNotInPlaylist(String playlistId) throws DatabaseException {
        trackDao.include("playlists");

        return trackDao.get(
                Filter.isNull("playlist_tracks", "playlist_id"),
                Join.leftJoin(
                        "playlist_tracks",
                        Filter.and(
                                Filter.equal("playlist_tracks", "track_id", "tracks", "id"),
                                Filter.equal("playlist_tracks", "playlist_id", playlistId)
                        )
                )
        );
    }
}
