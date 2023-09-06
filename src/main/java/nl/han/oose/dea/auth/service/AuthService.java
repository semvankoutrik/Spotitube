package nl.han.oose.dea.auth.service;

import jakarta.inject.Inject;
import nl.han.oose.dea.auth.exceptions.InvalidTokenException;
import nl.han.oose.dea.auth.exceptions.InvalidPasswordException;
import nl.han.oose.dea.auth.helpers.JwtHandler;
import nl.han.oose.dea.auth.helpers.PasswordHelper;
import nl.han.oose.dea.auth.models.Claim;
import nl.han.oose.dea.auth.shared.ClaimTypes;
import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.persistence.exceptions.DatabaseException;
import nl.han.oose.dea.presentation.interfaces.daos.IUserDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

public class AuthService {
    private IUserDao userDao;

    @Inject
    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    private final String signingKey;

    public AuthService() throws IOException {
        Properties properties = new Properties();
        properties.load(AuthService.class.getClassLoader().getResourceAsStream("auth.properties"));

        signingKey = properties.getProperty("secretKey");
    }

    public String signIn(User user, String password) throws InvalidPasswordException {
        if (!checkPassword(user, password)) throw new InvalidPasswordException();

        List<Claim> claims = new ArrayList<>();

        claims.add(new Claim(ClaimTypes.Subject, user.getId()));

        return JwtHandler.create(claims, signingKey);
    }

    public List<Claim> validateToken(String token) throws InvalidTokenException {
        return JwtHandler.validate(token, signingKey);
    }

    public boolean checkPassword(User user, String password) {
        byte[] salt = Base64.getDecoder().decode(user.getPasswordSalt());
        byte[] storedHash = Base64.getDecoder().decode(user.getPasswordHash());

        byte[] inputHash = PasswordHelper.generateHash(password, salt);

        boolean valid = false;

        for (int i = 0; i < inputHash.length; i++) {
            valid = inputHash[i] == storedHash[i];
        }

        return valid;
    }

    public User registerUser(User user, String password) throws DatabaseException {
        byte[] salt = PasswordHelper.generateSalt();
        byte[] hashBytes = PasswordHelper.generateHash(password, salt);

        user.setPasswordSalt(Base64.getEncoder().encodeToString(salt));
        user.setPasswordHash(Base64.getEncoder().encodeToString(hashBytes));

        userDao.insert(user);

        return user;
    }
}
