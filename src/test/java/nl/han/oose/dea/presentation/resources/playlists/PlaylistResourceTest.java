package nl.han.oose.dea.presentation.resources.playlists;

import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.auth.models.AuthenticatedUser;
import nl.han.oose.dea.auth.services.AuthContext;
import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.PlaylistTrack;
import nl.han.oose.dea.domain.entities.User;
import nl.han.oose.dea.domain.interfaces.IPlaylistRepository;
import nl.han.oose.dea.persistence.repositories.PlaylistRepository;
import nl.han.oose.dea.presentation.resources.playlists.dtos.GetPlaylistsResponse;
import nl.han.oose.dea.utils.DataSuppliers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlaylistResourceTest {
    private PlaylistsResource sut;
    private IPlaylistRepository playlistRepository;
    private Playlist randomPlaylist1;
    private Playlist randomPlaylist2;
    private User randomUser1;
    private User randomUser2;
    private final PlaylistTrack sultansOfSwing = new PlaylistTrack(
            DataSuppliers.UUIDString.get(),
            "Sultans Of Swing",
            "Dire Straits",
            348,
            1026934930,
            new Date(),
            "",
            true);

    @BeforeEach
    public void setup() {
        this.sut = new PlaylistsResource();

        randomUser1 = new User();
        randomUser1.setId("1ab07ed6-a53d-445a-8819-7eb278aceb27");

        randomUser2 = new User();
        randomUser2.setId("94769f95-8c19-4317-8290-f7789775c4a9");

        List<PlaylistTrack> playlist1Tracks = new ArrayList<>();

        playlist1Tracks.add(sultansOfSwing);
        playlist1Tracks.add(sultansOfSwing);

        randomPlaylist1 = new Playlist(
                "55f40691-fe2b-428d-be44-7ddb3e84ddee",
                "Deja Vu",
                randomUser1,
                playlist1Tracks
        );

        randomPlaylist2 = new Playlist(
                "75edc028-48b1-4a78-85a0-7fe1ac2cf74d",
                "Deja Vu 2",
                randomUser2,
                new ArrayList<>()
        );

        playlistRepository = new PlaylistRepository() {
            @Override
            public Playlist get(String id) {
                return new Playlist();
            }

            @Override
            public List<Playlist> getAll() {
                List<Playlist> playlists = new ArrayList<>();
                playlists.add(randomPlaylist1);
                playlists.add(randomPlaylist2);
                return playlists;
            }
        };

        sut.setPlaylistRepository(playlistRepository);
        sut.setAuthContext(new AuthContext() {
            @Override
            public AuthenticatedUser getAuthenticatedUser() {
                return new AuthenticatedUser(randomUser1.getId(), null);
            }
        });
    }

    @Test
    public void returnsCorrectOwnerField() {
        Response response = sut.getAll();

        assertEquals(200, response.getStatus());

        var entity = (GetPlaylistsResponse) response.getEntity();

        var playlist1 = entity.getPlaylists().get(0);
        assertTrue(playlist1.owner());

        var playlist2 = entity.getPlaylists().get(1);
        assertFalse(playlist2.owner());
    }

    @Test
    public void returnsCorrectTotalLength() {
        Response response = sut.getAll();

        assertEquals(200, response.getStatus());

        var entity = (GetPlaylistsResponse) response.getEntity();

        assertEquals(sultansOfSwing.getDuration() * 2, entity.getLength());
    }
}