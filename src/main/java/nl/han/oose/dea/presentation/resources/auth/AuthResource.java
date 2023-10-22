package nl.han.oose.dea.presentation.resources.auth;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.auth.exceptions.InvalidPasswordException;
import nl.han.oose.dea.auth.service.AuthService;
import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.domain.exceptions.DatabaseException;
import nl.han.oose.dea.domain.interfaces.IUserService;
import nl.han.oose.dea.persistence.utils.Filter;
import nl.han.oose.dea.persistence.interfaces.daos.IUserDao;
import nl.han.oose.dea.presentation.resources.auth.dtos.LoginRequest;
import nl.han.oose.dea.presentation.resources.auth.dtos.LoginResponse;
import nl.han.oose.dea.presentation.resources.shared.ResourceBase;

import java.util.Optional;

@Path("")
public class AuthResource extends ResourceBase {
    private IUserService userService;
    private AuthService authService;

    @Inject
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @Inject
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid LoginRequest loginRequest) {
        try {
            Optional<User> user = userService.getUserByUsername(loginRequest.user());

            if (user.isEmpty()) return badRequest("Username or password unknown.");

            String token = authService.signIn(user.get(), loginRequest.password());

            LoginResponse response = new LoginResponse(token, user.get().getFirstName() + " " + user.get().getLastName());

            return ok(response);
        } catch (InvalidPasswordException e) {
            return badRequest("Username or password unknown.");
        } catch (DatabaseException e) {
            return internalServerError();
        }
    }
}
