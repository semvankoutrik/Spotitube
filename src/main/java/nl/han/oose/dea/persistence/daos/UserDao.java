package nl.han.oose.dea.persistence.daos;

import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.persistence.configurations.UserConfiguration;
import nl.han.oose.dea.persistence.interfaces.daos.IUserDao;
import nl.han.oose.dea.persistence.utils.DatabaseConnection;

import java.sql.Connection;
import java.util.logging.Logger;

public class UserDao extends DaoBase<User> implements IUserDao {
    public UserDao()
    {
        this(DatabaseConnection.create());
    }

    public UserDao(Connection connection) {
        super(new UserConfiguration(), Logger.getLogger(UserDao.class.getName()), connection);
    }
}
