package nl.han.oose.dea.persistence.daos;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.persistence.constants.RelationType;
import nl.han.oose.dea.persistence.constants.TableNames;
import nl.han.oose.dea.persistence.shared.Property;

import java.util.function.Supplier;
import java.util.logging.Logger;

public class UserDao extends BaseDao<User> {
    public UserDao() {
        super(TableNames.USERS, Logger.getLogger(UserDao.class.getName()));

        properties.add(new Property<User>("id")
                .setSetter((user, id) -> user.setId((String) id))
                .setGetter(User::getId)
        );
        properties.add(new Property<User>("first_name")
                .setSetter((user, firstName) -> user.setFirstName((String) firstName))
                .setGetter(User::getFirstName)
        );
        properties.add(new Property<User>("last_name")
                .setSetter((user, firstName) -> user.setFirstName((String) firstName))
                .setGetter(User::getFirstName)
        );
    }

    @Override
    protected Supplier<User> entityFactory() {
        return User::new;
    }
}
