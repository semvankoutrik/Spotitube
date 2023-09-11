package nl.han.oose.dea.domain.entities;

import nl.han.oose.dea.domain.shared.EntityBase;

import java.util.List;

public class Playlist extends EntityBase {
    private String name;
    private User owner;
    private List<PlaylistTrack> tracks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<PlaylistTrack> getTracks() {
        return tracks;
    }

    public void setTracks(List<PlaylistTrack> tracks) {
        this.tracks = tracks;
    }
}
