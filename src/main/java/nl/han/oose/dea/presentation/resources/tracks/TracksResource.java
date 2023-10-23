package nl.han.oose.dea.presentation.resources.tracks;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.domain.entities.Track;
import nl.han.oose.dea.domain.exceptions.DatabaseException;
import nl.han.oose.dea.domain.exceptions.NotFoundException;
import nl.han.oose.dea.domain.interfaces.ITrackRepository;
import nl.han.oose.dea.presentation.resources.shared.ResourceBase;
import nl.han.oose.dea.presentation.resources.tracks.dtos.GetTrackResponse;

import java.util.List;

@Path("/tracks")
public class TracksResource extends ResourceBase {
    private ITrackRepository trackRepository;

    @Inject
    public void setTrackRepository(ITrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("forPlaylist") String id) {
        try {
            List<Track> tracks;

            if (id == null) {
                tracks = trackRepository.getAll();
            } else {
                tracks = trackRepository.getNotInPlaylist(id);
            }

            List<GetTrackResponse> response = tracks.stream().map(GetTrackResponse::fromEntity).toList();

            return ok(response);
        } catch (DatabaseException e) {
            return internalServerError();
        }
    }

    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") String id) {
        try {
            Track track = trackRepository.get(id);

            return ok(GetTrackResponse.fromEntity(track));
        } catch (DatabaseException e) {
            return internalServerError();
        } catch (NotFoundException e) {
            return notFound();
        }
    }
}
