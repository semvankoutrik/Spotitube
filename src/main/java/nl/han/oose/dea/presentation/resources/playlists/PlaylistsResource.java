package nl.han.oose.dea.presentation.resources.playlists;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.auth.annotations.Authorize;
import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.exceptions.DatabaseException;
import nl.han.oose.dea.domain.exceptions.NotFoundException;
import nl.han.oose.dea.persistence.interfaces.daos.IPlaylistDao;
import nl.han.oose.dea.presentation.resources.playlists.dtos.*;
import nl.han.oose.dea.presentation.resources.shared.ResourceBase;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Path("/playlists")
@Authorize
public class PlaylistsResource extends ResourceBase {
    private IPlaylistDao playlistDao;
    private final Logger logger = Logger.getLogger(PlaylistsResource.class.getName());

    @Inject
    public void setPlaylistDao(IPlaylistDao playlistDao) {
        this.playlistDao = playlistDao;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try
        {
            List<Playlist> playlists = playlistDao.get();

            return ok(GetPlaylistsResponse.fromEntity(playlists, getUser().getId()));
        }
        catch (DatabaseException e)
        {
            return internalServerError();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid CreatePlaylistRequest request) {
        try
        {
            Playlist playlist = request.mapToEntity(getUser().getId());

            playlistDao.insert(playlist);

            List<Playlist> playlists = playlistDao.get();

            return ok(GetPlaylistsResponse.fromEntity(playlists, getUser().getId()));
        }
        catch (DatabaseException e)
        {
            return internalServerError();
        }
    }

    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") String id) {
        try
        {
            Playlist playlist = playlistDao.get(id);

            return ok(GetPlaylistResponse.fromEntity(playlist, getUser().getId()));
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

    @GET
    @Path("{id}/tracks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracks(@PathParam("id") String id) {
        try
        {
            Playlist playlist = playlistDao.get(id);

            return ok(GetPlaylistTracksResponse.fromEntity(playlist.getTracks()));
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

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, UpdatePlaylistRequest request) {
        try
        {
            Playlist playlist = playlistDao.get(id);

            if (!Objects.equals(playlist.getOwner().getId(), getUser().getId())) {
                return forbidden();
            }

            try
            {
                Playlist updatedPlaylist = request.mapToEntity();
                updatedPlaylist.setOwner(playlist.getOwner());

                playlistDao.update(updatedPlaylist);

                return ok(GetPlaylistResponse.fromEntity(playlistDao.get(id), getUser().getId()));
            }
            catch (DatabaseException e)
            {
                return internalServerError();
            }
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

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        try
        {
            Playlist playlist = playlistDao.get(id);

            if(!Objects.equals(playlist.getOwner().getId(), getUser().getId())) {
                return forbidden();
            }

            playlistDao.delete(id);

            List<Playlist> playlists = playlistDao.get();

            return ok(GetPlaylistsResponse.fromEntity(playlists, getUser().getId()));
        }
        catch (DatabaseException e)
        {
            return internalServerError();
        } catch (NotFoundException e) {
            return notFound();
        }
    }
}
