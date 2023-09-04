package nl.han.oose.dea.presentation.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import nl.han.oose.dea.persistence.daos.PlaylistDao;
import nl.han.oose.dea.presentation.interfaces.daos.IPlaylistDao;

import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/playlists")
public class PlaylistResource {
    private IPlaylistDao playlistDao;
    private Logger logger = Logger.getLogger(PlaylistResource.class.getName());

    @Inject
    public void setPlaylistDao(PlaylistDao playlistDao) {
        this.playlistDao = playlistDao;
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public String get(@PathParam("id") String id) {
        logger.log(Level.SEVERE, "GET /playlist/{id} received.");
        return id;
    }
}
