package nl.han.oose.dea.persistence.repositories;

import jakarta.inject.Inject;
import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.domain.exceptions.DatabaseException;
import nl.han.oose.dea.domain.interfaces.IUserRepository;
import nl.han.oose.dea.persistence.interfaces.daos.IUserDao;
import nl.han.oose.dea.persistence.utils.Filter;

import java.util.Optional;

public class UserRepository implements IUserRepository {
    private IUserDao userDao;

    @Inject
    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws DatabaseException {
        return userDao.get(Filter.equal("users", "username", username)).stream().findFirst();
    }

    @Override
    public void create(User user) throws DatabaseException {
        userDao.insert(user);
    }
}
