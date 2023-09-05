package nl.han.oose.dea.persistence.shared;

import nl.han.oose.dea.persistence.constants.RelationTypes;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Property<TEntity> {
    private final String name;
    private BiConsumer<TEntity, Object> setter;
    private Function<TEntity, Object> getter;
    private Relation relation;

    public Property(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public RelationTypes getRelationType() {
        if (relation == null) return null;
        return relation.getType();
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

    public Relation getRelation() {
        return relation;
    }

    public Property<TEntity> setRelation(Relation relation) {
        this.relation = relation;

        return this;
    }
}
