package nl.han.oose.dea.persistence.daos;

import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.persistence.constants.TableNames;
import nl.han.oose.dea.persistence.shared.Column;

import java.util.function.Supplier;
import java.util.logging.Logger;

public class UserDao extends BaseDao<User> {
    public UserDao() {
        super(TableNames.PLAYLISTS, Logger.getLogger(UserDao.class.getName()));

        columns.put("id", new Column<>((user, id) -> user.setId((String) id), (User::getId)));
        columns.put("first_name", new Column<>((user, firstName) -> user.setFirstName((String) firstName), (User::getFirstName)));
        columns.put("last_name", new Column<>((user, lastName) -> user.setLastName((String) lastName), (User::getLastName)));
    }

    @Override
    protected Supplier<User> entityFactory() {
        return User::new;
    }
}
