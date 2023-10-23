package nl.han.oose.dea.domain.interfaces;

import nl.han.oose.dea.domain.entities.Track;
import nl.han.oose.dea.domain.exceptions.DatabaseException;
import nl.han.oose.dea.domain.exceptions.NotFoundException;

import java.util.List;

public interface ITrackRepository {
    List<Track> getAll() throws DatabaseException;
    Track get(String id) throws NotFoundException, DatabaseException;
    List<Track> getNotInPlaylist(String playlistId) throws DatabaseException;
}
