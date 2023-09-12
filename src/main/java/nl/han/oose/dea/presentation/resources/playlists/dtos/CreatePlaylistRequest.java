package nl.han.oose.dea.presentation.resources.playlists.dtos;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.entities.User;

import java.util.List;

public class CreatePlaylistRequest {
    private String id;
    private String name;
    private boolean owner;
    private List<CreatePlaylistTrackRequest> tracks;

    public Playlist mapToEntity(String userId) {
        User user = new User();
        user.setId(userId);
        Playlist playlist = new Playlist();
        playlist.setOwner(user);
        playlist.setName(name);
        playlist.setTracks(tracks.stream().map(CreatePlaylistTrackRequest::mapToEntity).toList());

        return playlist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTracks(List<CreatePlaylistTrackRequest> tracks) {
        this.tracks = tracks;
    }
}
