package nl.han.oose.dea.presentation.resources.playlists;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.persistence.daos.PlaylistDao;
import nl.han.oose.dea.persistence.exceptions.DatabaseException;
import nl.han.oose.dea.persistence.exceptions.NotFoundException;
import nl.han.oose.dea.presentation.interfaces.daos.IPlaylistDao;
import nl.han.oose.dea.presentation.resources.shared.ResourceBase;

import java.util.logging.Logger;

@Path("/playlists")
public class PlaylistsResource extends ResourceBase {
    private IPlaylistDao playlistDao;
    private Logger logger = Logger.getLogger(PlaylistsResource.class.getName());

    @Inject
    public void setPlaylistDao(PlaylistDao playlistDao) {
        this.playlistDao = playlistDao;
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response get(@PathParam("id") String id) {
        try
        {
            Playlist playlist = playlistDao.get(id);

            return ok(playlist);
        }
        catch (DatabaseException e)
        {
            return internalServerError();
        }
        catch (NotFoundException e)
        {
            return notFound();
        }
    }
}
