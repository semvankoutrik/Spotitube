package nl.han.oose.dea.persistence.shared;

import nl.han.oose.dea.persistence.constants.RelationType;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Property<T> {
    private final String name;
    private RelationType relationType;
    private BiConsumer<T, Object> setter;
    private Function<T, Object> getter;

    public Property(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public Property<T> setRelationType(RelationType relationType) {
        this.relationType = relationType;

        return this;
    }

    public BiConsumer<T, Object> getSetter() {
        return setter;
    }

    public Property<T> setSetter(BiConsumer<T, Object> setter) {
        this.setter = setter;

        return this;
    }

    public Function<T, Object> getGetter() {
        return getter;
    }

    public Property<T> setGetter(Function<T, Object> getter) {
        this.getter = getter;

        return this;
    }
}
