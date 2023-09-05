package nl.han.oose.dea.persistence.shared;

import nl.han.oose.dea.persistence.constants.RelationTypes;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Property<TEntity> {
    private final String name;
    private BiConsumer<TEntity, Object> setter;
    private Function<TEntity, Object> getter;
    private boolean ignoreIfNull = false;

    public Property(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public BiConsumer<TEntity, Object> getSetter() {
        return setter;
    }

    public Property<TEntity> setSetter(BiConsumer<TEntity, Object> setter) {
        this.setter = setter;

        return this;
    }

    public Function<TEntity, Object> getGetter() {
        return getter;
    }

    public Property<TEntity> setGetter(Function<TEntity, Object> getter) {
        this.getter = getter;

        return this;
    }

    public boolean getIgnoreIfNull() {
        return ignoreIfNull;
    }

    public Property<TEntity> setIgnoreIfNull(boolean ignoreIfNull) {
        this.ignoreIfNull = ignoreIfNull;

        return this;
    }
}
