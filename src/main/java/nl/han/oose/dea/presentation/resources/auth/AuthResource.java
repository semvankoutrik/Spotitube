package nl.han.oose.dea.presentation.resources.auth;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.auth.exceptions.InvalidPasswordException;
import nl.han.oose.dea.auth.service.AuthService;
import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.persistence.daos.UserDao;
import nl.han.oose.dea.persistence.exceptions.DatabaseException;
import nl.han.oose.dea.persistence.utils.Filters;
import nl.han.oose.dea.presentation.resources.auth.dtos.LoginRequest;
import nl.han.oose.dea.presentation.resources.auth.dtos.LoginResponse;
import nl.han.oose.dea.presentation.resources.shared.ResourceBase;

import java.util.Optional;

@Path("")
public class AuthResource extends ResourceBase {
    private UserDao userDao;
    private AuthService authService;

    @Inject
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Inject
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @POST
    @Path("/login")
    @Produces("application/json")
    public Response login(LoginRequest loginRequest) {
        try {
            Optional<User> user = userDao.get(Filters.equal("username", loginRequest.user())).stream().findFirst();

            if (user.isEmpty()) return notFound();

            String token = authService.signIn(user.get(), loginRequest.password());

            LoginResponse response = new LoginResponse(token, user.get().getFirstName() + " " + user.get().getLastName());

            return ok(response);
        } catch (InvalidPasswordException e) {
            return notFound();
        } catch (DatabaseException e) {
            return internalServerError();
        }
    }
}
