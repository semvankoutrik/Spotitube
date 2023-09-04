package nl.han.oose.dea;

import nl.han.oose.dea.domain.entities.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class LocalSetup {
    private List<User> users = new ArrayList<>();

    public LocalSetup() {
        User user1 = new User();
        user1.setFirstName("Sem");
        user1.setLastName("van Koutrik");
        users.add(user1);

        User user2 = new User();
        user2.setFirstName("Sam");
        user2.setLastName("van Koetrik");
        users.add(user2);
    }

    @Test
    public void insertUsers() {

    }

    @Test
    public void insertPlaylists() {

    }
}
