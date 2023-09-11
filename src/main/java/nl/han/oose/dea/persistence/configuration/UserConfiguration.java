package nl.han.oose.dea.persistence.configuration;

import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.persistence.constants.TableNames;
import nl.han.oose.dea.persistence.shared.Property;

import java.util.function.Supplier;
import java.util.logging.Logger;

public class UserConfiguration extends TableConfigurationBase<User> {
    public UserConfiguration() {
        super(TableNames.USERS, Logger.getLogger(UserConfiguration.class.getName()));

        properties.add(new Property<User>("id")
                .setSetter((user, id) -> user.setId((String) id))
                .setGetter(User::getId)
        );
        properties.add(new Property<User>("username")
                .setSetter((user, username) -> user.setUsername((String) username))
                .setGetter(User::getUsername)
        );
        properties.add(new Property<User>("first_name")
                .setSetter((user, firstName) -> user.setFirstName((String) firstName))
                .setGetter(User::getFirstName)
        );
        properties.add(new Property<User>("last_name")
                .setSetter((user, lastName) -> user.setLastName((String) lastName))
                .setGetter(User::getLastName)
        );
        properties.add(new Property<User>("password_hash")
                .setSetter((user, passwordHash) -> user.setPasswordHash((String) passwordHash))
                .setGetter(User::getPasswordHash)
        );
        properties.add(new Property<User>("password_salt")
                .setSetter((user, passwordSalt) -> user.setPasswordSalt((String) passwordSalt))
                .setGetter(User::getPasswordSalt)
        );
    }

    @Override
    public Supplier<User> entityFactory() {
        return User::new;
    }
}
