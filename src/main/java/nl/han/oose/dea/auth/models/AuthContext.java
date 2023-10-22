package nl.han.oose.dea.auth.models;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class AuthContext {
    private AuthenticatedUser authenticatedUser;

    public void setAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public AuthenticatedUser getAuthenticatedUser() {
        return authenticatedUser;
    }
}
