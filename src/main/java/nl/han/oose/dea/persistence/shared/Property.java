package nl.han.oose.dea.persistence.shared;

import nl.han.oose.dea.domain.shared.EntityBase;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Property<TEntity extends EntityBase> {
    private final String name;
    private BiConsumer<TEntity, Object> setter;
    private Function<TEntity, Object> getter;
    private boolean nullable = false;

    public Property(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Property<TEntity> setSetter(BiConsumer<TEntity, Object> setter) {
        this.setter = setter;

        return this;
    }

    public Object getValue(EntityBase entity) {
        return this.getter.apply((TEntity) entity);
    }

    public void setValue(EntityBase entity, Object value) {
        this.setter.accept((TEntity) entity, value);
    }


    public Property<TEntity> setGetter(Function<TEntity, Object> getter) {
        this.getter = getter;

        return this;
    }

    public boolean isNullable() {
        return nullable;
    }

    public Property<TEntity> setNullable(boolean nullable) {
        this.nullable = nullable;

        return this;
    }

    public BiConsumer<TEntity, Object> getSetter() {
        return setter;
    }

    public Function<TEntity, Object> getGetter() {
        return getter;
    }
}
