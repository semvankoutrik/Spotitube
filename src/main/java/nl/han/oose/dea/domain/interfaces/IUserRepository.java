package nl.han.oose.dea.domain.interfaces;

import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.domain.exceptions.DatabaseException;

import java.util.Optional;

public interface IUserRepository {
    Optional<User> getUserByUsername(String username) throws DatabaseException;
    void create(User user) throws DatabaseException;
}
