package nl.han.oose.dea.localSetup;

import nl.han.oose.dea.auth.services.AuthService;
import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.PlaylistTrack;
import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.persistence.daos.PlaylistDao;
import nl.han.oose.dea.persistence.daos.TrackDao;
import nl.han.oose.dea.persistence.daos.UserDao;
import nl.han.oose.dea.domain.exceptions.DatabaseException;
import nl.han.oose.dea.persistence.repositories.UserRepository;
import nl.han.oose.dea.utils.DataSuppliers;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocalSetup {
    private final List<User> users = new ArrayList<>();

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
    public void seedAll() throws DatabaseException, SQLException, IOException {
        new DatabaseSetup().truncateTables();

        insertUsers();
        insertPlaylistsAndTracks();
    }

    @Test
    public void insertUsers() throws DatabaseException, IOException {
        AuthService authService = new AuthService();
        UserRepository userRepository = new UserRepository();
        UserDao userDao = new UserDao();

        userRepository.setUserDao(userDao);
        authService.setUserRepository(userRepository);

        for(User user : users) {
            authService.registerUser(user, "Test1234!");
        }
    }

    @Test
    public void insertPlaylistsAndTracks() throws DatabaseException {
        TrackDao trackDao = new TrackDao();

        PlaylistTrack ladyWriter = new PlaylistTrack();
        ladyWriter.setId(DataSuppliers.UUIDString.get());
        ladyWriter.setTitle("Lady Writer");
        ladyWriter.setPerformer("Dire Straits");
        ladyWriter.setDuration(224);
        ladyWriter.setOfflineAvailable(false);
        ladyWriter.setPlaycount(95304036);
        trackDao.insert(ladyWriter);

        PlaylistTrack sultansOfSwing = new PlaylistTrack();
        sultansOfSwing.setId(DataSuppliers.UUIDString.get());
        sultansOfSwing.setTitle("Sultans Of Swing");
        sultansOfSwing.setPerformer("Dire Straits");
        sultansOfSwing.setDuration(348);
        sultansOfSwing.setOfflineAvailable(true);
        sultansOfSwing.setPlaycount(1026934930);
        trackDao.insert(sultansOfSwing);

        PlaylistTrack tunnelOfLove = new PlaylistTrack();
        tunnelOfLove.setId(DataSuppliers.UUIDString.get());
        tunnelOfLove.setTitle("Tunnel Of Love");
        tunnelOfLove.setPerformer("Dire Straits");
        tunnelOfLove.setDuration(489);
        tunnelOfLove.setOfflineAvailable(false);
        tunnelOfLove.setPlaycount(74358678);
        trackDao.insert(tunnelOfLove);

        // Insert playlists
        PlaylistDao playlistDao = new PlaylistDao();

        // Deja Vu
        Playlist dejaVu = new Playlist();
        dejaVu.setId(DataSuppliers.UUIDString.get());
        dejaVu.setName("Deja Vu");
        dejaVu.setOwner(users.get(0));

        List<PlaylistTrack> dejaVuTracks = new ArrayList<>();
        dejaVuTracks.add(ladyWriter);
        dejaVuTracks.add(sultansOfSwing);
        dejaVuTracks.add(tunnelOfLove);
        dejaVu.setTracks(dejaVuTracks);

        playlistDao.insert(dejaVu);

        // Summer
        Playlist summer = new Playlist();

        summer.setId(DataSuppliers.UUIDString.get());
        summer.setName("Summer");
        summer.setOwner(users.get(1));

        playlistDao.insert(summer);
    }
}
