package nl.han.oose.dea.domain.shared;

public abstract class EntityBase {
    private String id;

    public EntityBase() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
