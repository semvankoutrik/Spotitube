package nl.han.oose.dea.persistence.repositories;

import jakarta.inject.Inject;
import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.PlaylistTrack;
import nl.han.oose.dea.domain.exceptions.DatabaseException;
import nl.han.oose.dea.domain.exceptions.NotFoundException;
import nl.han.oose.dea.domain.interfaces.IPlaylistRepository;
import nl.han.oose.dea.persistence.interfaces.daos.IPlaylistDao;

import java.util.List;

public class PlaylistRepository implements IPlaylistRepository {
    private IPlaylistDao playlistDao;

    @Inject
    public void setPlaylistDao(IPlaylistDao playlistDao) {this.playlistDao = playlistDao;}

    @Override
    public void create(Playlist playlist) throws DatabaseException {
        playlistDao.insert(playlist);
    }

    @Override
    public Playlist get(String id) throws NotFoundException, DatabaseException {
        playlistDao.include("tracks");

        return playlistDao.get(id);
    }

    @Override
    public List<PlaylistTrack> getTracks(String playlistId) throws NotFoundException, DatabaseException {
        playlistDao.include("tracks");

        return playlistDao.get(playlistId).getTracks();
    }

    @Override
    public List<Playlist> getAll() throws DatabaseException {
        playlistDao.include("tracks");

        return playlistDao.get();
    }

    @Override
    public void update(Playlist playlist) throws DatabaseException {
        playlistDao.update(playlist);
    }

    @Override
    public void delete(String id) throws DatabaseException {
        playlistDao.delete(id);
    }
}
