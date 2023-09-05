package nl.han.oose.dea;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.persistence.daos.PlaylistDao;
import nl.han.oose.dea.persistence.daos.UserDao;
import nl.han.oose.dea.persistence.exceptions.DatabaseException;
import nl.han.oose.dea.utils.DataSuppliers;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocalSetup {
    private List<User> users = new ArrayList<>();

    public LocalSetup() {
        User user1 = new User();
        user1.setId(DataSuppliers.UUIDString.get());
        user1.setUsername("semvankoutrik");
        user1.setFirstName("Sem");
        user1.setLastName("van Koutrik");
        users.add(user1);

        User user2 = new User();
        user1.setId(DataSuppliers.UUIDString.get());
        user2.setUsername("samvankoetrik");
        user2.setFirstName("Sam");
        user2.setLastName("van Koetrik");
        users.add(user2);
    }

    @Test
    public void seedAll() throws DatabaseException, SQLException {
        new DatabaseSetup().truncateTables();

        insertUsers();
        insertPlaylists();
    }

    @Test
    public void insertUsers() throws DatabaseException {
        UserDao userDao = new UserDao();

        for(User user : users) {
            userDao.insert(user);
        }
    }

    @Test
    public void insertPlaylists() throws DatabaseException {
        PlaylistDao playlistDao = new PlaylistDao();

        Playlist playlist = new Playlist();

        playlist.setId(DataSuppliers.UUIDString.get());
        playlist.setName("Deja Vu");
        playlist.setOwner(users.get(0));

        playlistDao.insert(playlist);
    }
}
