package nl.han.oose.dea.persistence.daos;

import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.persistence.configuration.UserConfiguration;
import nl.han.oose.dea.presentation.interfaces.daos.IUserDao;

import java.util.logging.Logger;

public class UserDao extends DaoBase<User> implements IUserDao {
    public UserDao() {
        super(new UserConfiguration(), Logger.getLogger(UserDao.class.getName()));
    }
}
