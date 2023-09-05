package nl.han.oose.dea.domain.entities;

import nl.han.oose.dea.domain.shared.EntityBase;

public class Playlist extends EntityBase {
    private String name;
    private User owner;

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
}
