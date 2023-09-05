package nl.han.oose.dea.presentation.resources.auth.dtos;

public class LoginResponse {
    private String token;
    private String name;

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
