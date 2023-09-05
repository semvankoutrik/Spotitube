package nl.han.oose.dea.persistence.shared;

import nl.han.oose.dea.domain.shared.EntityBase;
import nl.han.oose.dea.persistence.configuration.ITableConfiguration;
import nl.han.oose.dea.persistence.constants.RelationTypes;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Relation<TProperty extends EntityBase, TRelation extends EntityBase> extends Property<TProperty> {
    private final String foreignTable;
    private String foreignColumn;
    private String linkTable;
    private String linkColumn;
    private String foreignLinkColumn;
    private final RelationTypes type;
    private ITableConfiguration<TRelation> foreignTableConfiguration;

    public Relation(String name, String foreignTable, String foreignColumn, RelationTypes type, ITableConfiguration<TRelation> foreignTableConfiguration) {
        super(name);
        this.foreignTable = foreignTable;
        this.foreignColumn = foreignColumn;
        this.type = type;
        this.foreignTableConfiguration = foreignTableConfiguration;
    }

    public static <TProperty extends EntityBase, TRelation extends EntityBase> Relation<TProperty, TRelation> hasOne(
            String name,
            String foreignTable,
            String foreignColumn,
            ITableConfiguration<TRelation> configuration,
            Class<TProperty> propertyClass
    ) {
        return new Relation<>(name, foreignTable, foreignColumn, RelationTypes.HAS_ONE, configuration);
    }

    public static <TProperty extends EntityBase, TRelation extends EntityBase> Relation<TProperty, TRelation> hasMany(
            String name,
            String foreignTable,
            String foreignColumn,
            ITableConfiguration<TRelation> configuration,
            Class<TProperty> propertyClass) {
        return new Relation<>(name, foreignTable, foreignColumn, RelationTypes.HAS_MANY, configuration);
    }

    public static <TProperty extends EntityBase, TRelation extends EntityBase> Relation<TProperty, TRelation> hasManyThrough(
            String name,
            String linkTable,
            String linkColumn,
            String foreignLinkColumn,
            String foreignTable,
            String foreignColumn,
            ITableConfiguration<TRelation> configuration,
            Class<TProperty> propertyClass) {
        return new Relation<TProperty, TRelation>(name, foreignTable, foreignColumn, RelationTypes.HAS_MANY_THROUGH, configuration)
                .setLinkTable(linkTable)
                .setLinkColumn(linkColumn)
                .setForeignLinkColumn(foreignLinkColumn);
    }

    public String getForeignColumn() {
        return foreignColumn;
    }

    public RelationTypes getType() {
        return type;
    }

    public String getForeignTable() {
        return foreignTable;
    }

    public Relation<TProperty, TRelation> setForeignColumn(String foreignColumn) {
        this.foreignColumn = foreignColumn;

        return this;
    }

    public String getLinkTable() {
        return linkTable;
    }

    public Relation<TProperty, TRelation> setLinkTable(String linkTable) {
        this.linkTable = linkTable;

        return this;
    }

    public String getLinkColumn() {
        return linkColumn;
    }

    public Relation<TProperty, TRelation> setLinkColumn(String linkColumn) {
        this.linkColumn = linkColumn;

        return this;
    }

    public String getForeignLinkColumn() {
        return foreignLinkColumn;
    }

    public Relation<TProperty, TRelation> setForeignLinkColumn(String foreignLinkColumn) {
        this.foreignLinkColumn = foreignLinkColumn;

        return this;
    }

    @Override
    public Relation<TProperty, TRelation> setSetter(BiConsumer<TProperty, Object> setter) {
        super.setSetter(setter);

        return this;
    }

    @Override
    public Relation<TProperty, TRelation> setGetter(Function<TProperty, Object> getter) {
        super.setGetter(getter);

        return this;
    }

    @Override
    public Relation<TProperty, TRelation> setIgnoreIfNull(boolean ignoreIfNull) {
        super.setIgnoreIfNull(ignoreIfNull);

        return this;
    }
}
