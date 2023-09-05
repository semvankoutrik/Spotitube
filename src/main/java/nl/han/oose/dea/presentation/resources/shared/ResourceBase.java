package nl.han.oose.dea.presentation.resources.shared;

import jakarta.ws.rs.core.Response;

public abstract class ResourceBase {
    public Response ok() {
        return Response.ok().build();
    }

    public Response ok(Object entity) {
        return Response.ok(entity).build();
    }

    public Response notFound() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    public Response internalServerError() {
        return Response.serverError().build();
    }
}
