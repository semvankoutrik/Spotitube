package nl.han.oose.dea.domain.entities;

import nl.han.oose.dea.domain.shared.EntityBase;

import java.util.List;

public class User extends EntityBase {
    private String firstName;
    private String lastName;
    private List<Playlist> playlists;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }
}
