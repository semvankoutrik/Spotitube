package nl.han.oose.dea.presentation.resources.tracks;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.auth.annotations.Authorize;
import nl.han.oose.dea.domain.entities.Track;
import nl.han.oose.dea.persistence.exceptions.DatabaseException;
import nl.han.oose.dea.persistence.exceptions.NotFoundException;
import nl.han.oose.dea.presentation.interfaces.daos.ITrackDao;
import nl.han.oose.dea.presentation.resources.shared.ResourceBase;
import nl.han.oose.dea.presentation.resources.tracks.dtos.GetTrackResponse;

import java.util.List;
import java.util.logging.Logger;

@Path("/tracks")
public class TracksResource extends ResourceBase {
    private ITrackDao tracksDao;
    private final Logger logger = Logger.getLogger(TracksResource.class.getName());

    @Inject
    public void setPlaylistDao(ITrackDao tracksDao) {
        this.tracksDao = tracksDao;
    }

    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") String id) {
        try
        {
            Track track = tracksDao.get(id);

            return ok(GetTrackResponse.fromEntity(track));
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
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try
        {
            List<Track> tracks = tracksDao.get();

            List<GetTrackResponse> response = tracks.stream().map(GetTrackResponse::fromEntity).toList();

            return ok(response);
        }
        catch (DatabaseException e)
        {
            return internalServerError();
        }
    }
}
