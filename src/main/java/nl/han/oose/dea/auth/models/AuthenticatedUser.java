package nl.han.oose.dea.auth.models;

import jakarta.annotation.Nullable;

import java.util.HashMap;
import java.util.List;

public class AuthenticatedUser {
    private final String id;
    private final HashMap<String, Object> claims = new HashMap<>();

    public AuthenticatedUser(String id, @Nullable List<Claim> claims) {
        this.id = id;

        if(claims != null) {
            for(Claim claim : claims) {
                this.claims.put(claim.type(), claim.value());
            }
        }
    }

    public String getId() {
        return id;
    }

    public Object getClaim(String type) {
        return claims.get(type);
    }
}
