package nl.han.oose.dea.auth.filters;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import nl.han.oose.dea.auth.services.AuthContext;
import nl.han.oose.dea.auth.annotations.Authorize;
import nl.han.oose.dea.auth.exceptions.InvalidTokenException;
import nl.han.oose.dea.auth.models.AuthenticatedUser;
import nl.han.oose.dea.auth.models.Claim;
import nl.han.oose.dea.auth.services.AuthService;
import nl.han.oose.dea.auth.shared.ClaimTypes;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Provider
@Authorize
public class AuthorizationFilter implements ContainerRequestFilter {
    private AuthContext authContext;
    private AuthService authService;
    private final Properties properties = new Properties();

    @Inject
    public void setAuthContext(AuthContext authContext) {
        this.authContext = authContext;
    }

    @Inject
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        MultivaluedMap<String, String> queryParams = containerRequestContext.getUriInfo().getQueryParameters();

        List<String> tokenParameter = queryParams.get("token");

        if(tokenParameter == null) {
            unauthorized(containerRequestContext);

            return;
        }

        Optional<String> token = tokenParameter.stream().findFirst();

        if(token.isEmpty()) {
            unauthorized(containerRequestContext);

            return;
        }

        try
        {
            List<Claim> claims = authService.validateToken(token.get());

            Optional<Claim> subClaim = claims.stream().filter(c -> c.type().equals(ClaimTypes.Subject)).findFirst();

            if (subClaim.isEmpty()) {
                unauthorized(containerRequestContext);

                return;
            }

            AuthenticatedUser user = new AuthenticatedUser((String) subClaim.get().value(), claims);

            authContext.setAuthenticatedUser(user);
        } catch (InvalidTokenException e) {
            unauthorized(containerRequestContext);
        }
    }

    public void unauthorized(ContainerRequestContext context) {
        context.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    }
}
