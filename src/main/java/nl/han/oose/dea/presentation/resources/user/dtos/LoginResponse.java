package nl.han.oose.dea.presentation.resources.user.dtos;

public class LoginResponse {
    private final String token;
    private final String name;

    public LoginResponse(String token, String name) {
        this.token = token;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }
}
