package nl.han.oose.dea.persistence.shared;

import nl.han.oose.dea.domain.shared.EntityBase;
import nl.han.oose.dea.persistence.configuration.ITableConfiguration;
import nl.han.oose.dea.persistence.enums.RelationTypes;

public class Relations {
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

    public static <TProperty extends EntityBase, TRelation extends EntityBase> HasManyThroughRelation<TProperty, TRelation> hasManyThrough(
            String name,
            String linkTable,
            String linkColumn,
            String foreignLinkColumn,
            String foreignTable,
            String foreignColumn,
            ITableConfiguration<TRelation> configuration,
            Class<TProperty> propertyClass) {
        return new HasManyThroughRelation<>(name, foreignTable, foreignColumn, RelationTypes.HAS_MANY_THROUGH, configuration, linkTable, linkColumn, foreignLinkColumn);
    }
}
