package nl.han.oose.dea;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.Track;
import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.persistence.daos.PlaylistDao;
import nl.han.oose.dea.persistence.daos.TrackDao;
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
        insertPlaylistsAndTracks();
    }

    @Test
    public void insertUsers() throws DatabaseException {
        UserDao userDao = new UserDao();

        for(User user : users) {
            userDao.insert(user);
        }
    }

    @Test
    public void insertPlaylistsAndTracks() throws DatabaseException {
        TrackDao trackDao = new TrackDao();

        Track ladyWriter = new Track();
        ladyWriter.setId(DataSuppliers.UUIDString.get());
        ladyWriter.setTitle("Lady Writer");
        ladyWriter.setPerformer("Dire Straits");
        ladyWriter.setDuration(224);
        trackDao.insert(ladyWriter);

        Track sultansOfSwing = new Track();
        ladyWriter.setId(DataSuppliers.UUIDString.get());
        ladyWriter.setTitle("Sultans Of Swing");
        ladyWriter.setPerformer("Dire Straits");
        ladyWriter.setDuration(348);
        trackDao.insert(sultansOfSwing);

        Track tunnelOfLove = new Track();
        ladyWriter.setId(DataSuppliers.UUIDString.get());
        ladyWriter.setTitle("Tunnel Of Love");
        ladyWriter.setPerformer("Dire Straits");
        ladyWriter.setDuration(489);
        trackDao.insert(tunnelOfLove);

        // Insert playlists
        PlaylistDao playlistDao = new PlaylistDao();

        // Deja Vu
        Playlist dejaVu = new Playlist();
        dejaVu.setId(DataSuppliers.UUIDString.get());
        dejaVu.setName("Deja Vu");
        dejaVu.setOwner(users.get(0));

        List<Track> dejaVuTracks = new ArrayList<>();
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
