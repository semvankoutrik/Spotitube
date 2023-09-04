package nl.han.oose.dea.persistence.daos;

import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.persistence.constants.ColumnTypes;
import nl.han.oose.dea.persistence.constants.TableNames;
import nl.han.oose.dea.persistence.shared.Property;

import java.util.function.Supplier;
import java.util.logging.Logger;

public class UserDao extends BaseDao<User> {
    public UserDao() {
        super(TableNames.USERS, Logger.getLogger(UserDao.class.getName()));

        properties.add(new Property<>("id", ColumnTypes.VALUE, (user, id) -> user.setId((String) id), (User::getId)));
        properties.add(new Property<>("first_name", ColumnTypes.VALUE, (user, firstName) -> user.setFirstName((String) firstName), (User::getFirstName)));
        properties.add(new Property<>("last_name", ColumnTypes.VALUE, (user, lastName) -> user.setLastName((String) lastName), (User::getLastName)));
    }

    @Override
    protected Supplier<User> entityFactory() {
        return User::new;
    }
}
