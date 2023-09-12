package nl.han.oose.dea.persistence.shared;

import nl.han.oose.dea.domain.shared.EntityBase;
import nl.han.oose.dea.persistence.configuration.ITableConfiguration;
import nl.han.oose.dea.persistence.enums.RelationTypes;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Relation<TProperty extends EntityBase, TRelation extends EntityBase> extends Property<TProperty> {
    private final String foreignTable;
    private final String foreignColumn;
    private final RelationTypes type;
    private final ITableConfiguration<TRelation> foreignTableConfiguration;

    public Relation(String name, String foreignTable, String foreignColumn, RelationTypes type, ITableConfiguration<TRelation> foreignTableConfiguration) {
        super(name);
        this.foreignTable = foreignTable;
        this.foreignColumn = foreignColumn;
        this.type = type;
        this.foreignTableConfiguration = foreignTableConfiguration;
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

    public ITableConfiguration<TRelation> getForeignTableConfiguration() {
        return foreignTableConfiguration;
    }
}
