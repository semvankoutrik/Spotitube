package nl.han.oose.dea;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.persistence.daos.UserDao;
import nl.han.oose.dea.persistence.exceptions.DatabaseException;
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
    public void insertUsers() throws DatabaseException {
        UserDao userDao = new UserDao();

        for(User user : users) {
            userDao.insert(user);
        }
    }

    @Test
    public void insertPlaylists() {
        Playlist playlist = new Playlist();

        playlist.setName("Deja Vu");
        playlist.setOwner(users.get(0));
    }
}
