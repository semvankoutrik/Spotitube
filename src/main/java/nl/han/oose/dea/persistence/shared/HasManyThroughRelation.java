package nl.han.oose.dea.persistence.shared;

import nl.han.oose.dea.domain.shared.EntityBase;
import nl.han.oose.dea.persistence.configuration.ITableConfiguration;
import nl.han.oose.dea.persistence.constants.RelationTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class HasManyThroughRelation<TProperty extends EntityBase, TRelation extends EntityBase> extends Relation<TProperty, TRelation> {
    private final String linkTable;
    private final String linkColumn;
    private final String foreignLinkColumn;
    private final ITableConfiguration<TRelation> foreignTableConfiguration;
    private List<Property<TRelation>> properties = new ArrayList<>();

    public HasManyThroughRelation(String name, String foreignTable, String foreignColumn, RelationTypes type, ITableConfiguration<TRelation> foreignTableConfiguration, String linkTable, String linkColumn, String foreignLinkColumn) {
        super(name, foreignTable, foreignColumn, type, foreignTableConfiguration);
        this.linkTable = linkTable;
        this.linkColumn = linkColumn;
        this.foreignLinkColumn = foreignLinkColumn;
        this.foreignTableConfiguration = foreignTableConfiguration;
    }

    public String getLinkTable() {
        return linkTable;
    }
    public String getLinkColumn() {
        return linkColumn;
    }
    public String getForeignLinkColumn() {
        return foreignLinkColumn;
    }

    @Override
    public HasManyThroughRelation<TProperty, TRelation> setSetter(BiConsumer<TProperty, Object> setter) {
        super.setSetter(setter);

        return this;
    }

    @Override
    public HasManyThroughRelation<TProperty, TRelation> setGetter(Function<TProperty, Object> getter) {
        super.setGetter(getter);

        return this;
    }

    public ITableConfiguration<TRelation> getForeignTableConfiguration() {
        return foreignTableConfiguration;
    }

    public List<Property<TRelation>> getProperties() {
        return properties;
    }

    public HasManyThroughRelation<TProperty, TRelation> setProperties(List<Property<TRelation>> properties) {
        this.properties = properties;

        return this;
    }
}
