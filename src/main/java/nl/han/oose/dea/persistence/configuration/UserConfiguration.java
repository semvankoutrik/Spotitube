package nl.han.oose.dea.persistence.configuration;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.persistence.constants.TableNames;
import nl.han.oose.dea.persistence.shared.Property;
import nl.han.oose.dea.persistence.shared.Relation;

import java.util.ArrayList;
import java.util.List;

public class UserConfiguration extends TableConfigurationBase<User> {
    public UserConfiguration() {
        super(TableNames.USERS);

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
        properties.add(new Property<User>("playlists")
                .setSetter((user, playlists) -> user.setPlaylists((List<Playlist>) playlists))
                .setGetter(User::getPlaylists)
                .setRelation(Relation.hasMany("playlists", "owner_id"))
        );
    }
}
