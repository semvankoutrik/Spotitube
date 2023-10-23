package nl.han.oose.dea.domain.interfaces;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.PlaylistTrack;
import nl.han.oose.dea.domain.exceptions.DatabaseException;
import nl.han.oose.dea.domain.exceptions.NotFoundException;

import java.util.List;

public interface IPlaylistRepository {
    void create(Playlist playlist) throws DatabaseException;
    Playlist get(String id) throws NotFoundException, DatabaseException;
    List<PlaylistTrack> getTracks(String playlistId) throws NotFoundException, DatabaseException;
    List<Playlist> getAll() throws DatabaseException;
    void update(Playlist playlist) throws DatabaseException;
    void delete(String id) throws DatabaseException;
}
