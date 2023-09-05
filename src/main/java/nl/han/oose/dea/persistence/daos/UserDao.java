package nl.han.oose.dea.persistence.daos;

import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.persistence.configuration.UserConfiguration;

import java.util.function.Supplier;
import java.util.logging.Logger;

public class UserDao extends DaoBase<User> {
    public UserDao() {
        super(new UserConfiguration(), Logger.getLogger(UserDao.class.getName()));
    }
}
