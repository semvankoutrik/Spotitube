package nl.han.oose.dea.presentation.resources.playlists.dtos;

import nl.han.oose.dea.domain.entities.Playlist;

import java.util.List;

public class UpdatePlaylistRequest {
    private String id;
    private String name;
    private boolean owner;
    private List<UpdatePlaylistTrackRequest> tracks;

    public Playlist mapToEntity() {
        Playlist playlist = new Playlist();

        playlist.setId(id);
        playlist.setName(name);
        playlist.setTracks(tracks.stream().map(UpdatePlaylistTrackRequest::mapToEntity).toList());

        return playlist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UpdatePlaylistTrackRequest> getTracks() {
        return tracks;
    }

    public void setTracks(List<UpdatePlaylistTrackRequest> tracks) {
        this.tracks = tracks;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }
}
