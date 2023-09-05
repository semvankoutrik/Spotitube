package nl.han.oose.dea.auth.helpers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import nl.han.oose.dea.auth.exceptions.InvalidTokenException;
import nl.han.oose.dea.auth.models.Claim;

import java.util.Date;
import java.util.List;

public class JwtHandler {
    public static String create(List<Claim> claims, String signingKey) throws IllegalArgumentException {
        JWTCreator.Builder jwtBuilder = JWT.create();

        for (Claim claim : claims) {
            String type = claim.type();
            Object value = claim.value();

            if (value instanceof Boolean) {
                jwtBuilder.withClaim(type, (Boolean) value);
            } else if (value instanceof String) {
                jwtBuilder.withClaim(type, (String) value);
            } else if (value instanceof Date) {
                jwtBuilder.withClaim(type, (Date) value);
            } else if (value instanceof Integer) {
                jwtBuilder.withClaim(type, (Integer) value);
            } else if (value instanceof Long) {
                jwtBuilder.withClaim(type, (Long) value);
            } else if (value instanceof Double) {
                jwtBuilder.withClaim(type, (Double) value);
            } else if (value instanceof List<?>) {
                jwtBuilder.withClaim(type, (List<?>) value);
            } else {
                throw new IllegalArgumentException("Data-type not supported for claim: " + type);
            }
        }

        jwtBuilder.withIssuedAt(new Date());

        return jwtBuilder.sign(getAlgorithm(signingKey));
    }

    public static List<Claim> validate(String token, String signingKey) throws InvalidTokenException {
        try {
            JWTVerifier verifier = JWT.require(getAlgorithm(signingKey)).build();

            DecodedJWT decodedJWT = verifier.verify(token);

            return decodedJWT
                    .getClaims()
                    .entrySet()
                    .stream()
                    .map(s -> new Claim(s.getKey(), s.getValue().as(Object.class))).toList();
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException();
        }
    }

    private static Algorithm getAlgorithm(String secret) {
        return Algorithm.HMAC256(secret);
    }
}
