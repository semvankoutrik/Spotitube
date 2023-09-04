package nl.han.oose.dea.domain.shared;

public abstract class BaseEntity {
    private String id;

    public BaseEntity() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
