package nl.han.oose.dea.presentation.resources.shared;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.auth.models.AuthContext;
import nl.han.oose.dea.auth.models.AuthenticatedUser;
import nl.han.oose.dea.persistence.models.BadRequest;

public abstract class ResourceBase {
    private AuthContext authContext;

    @Inject
    public void setAuthContext(AuthContext authContext) {
        this.authContext = authContext;
    }

    public AuthenticatedUser getUser() {
        return authContext.getAuthenticatedUser();
    }

    public Response ok() {
        return Response.ok().build();
    }

    public Response ok(Object entity) {
        return Response.ok(entity).build();
    }

    public Response notFound() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    public Response badRequest() { return Response.status(Response.Status.BAD_REQUEST).build(); }
    public Response badRequest(String message) { return Response.status(Response.Status.BAD_REQUEST).entity(BadRequest.fromMessage(message)).build(); }
    public Response badRequest(BadRequest badRequest) { return Response.status(Response.Status.BAD_REQUEST).entity(badRequest).build(); }

    public Response internalServerError() {
        return Response.serverError().build();
    }
}
