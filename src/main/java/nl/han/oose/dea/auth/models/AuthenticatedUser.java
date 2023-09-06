package nl.han.oose.dea.auth.models;

import java.util.HashMap;
import java.util.List;

public class AuthenticatedUser {
    private final String id;
    private final HashMap<String, Object> claims = new HashMap<>();

    public AuthenticatedUser(String id, List<Claim> claims) {
        this.id = id;

        for(Claim claim : claims) {
            this.claims.put(claim.type(), claim.value());
        }
    }

    public String getId() {
        return id;
    }

    public Object getClaim(String type) {
        return claims.get(type);
    }
}
