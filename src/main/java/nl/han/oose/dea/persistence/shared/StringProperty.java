package nl.han.oose.dea.persistence.shared;

import nl.han.oose.dea.domain.shared.EntityBase;

public class StringProperty<T extends EntityBase> extends Property<T, String> {
    public StringProperty(String name) {
        super(name);
    }
}
