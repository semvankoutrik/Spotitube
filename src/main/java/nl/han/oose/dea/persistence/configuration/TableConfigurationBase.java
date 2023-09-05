package nl.han.oose.dea.persistence.configuration;

import nl.han.oose.dea.domain.shared.EntityBase;
import nl.han.oose.dea.persistence.constants.RelationTypes;
import nl.han.oose.dea.persistence.shared.Property;
import nl.han.oose.dea.persistence.shared.Relation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class TableConfigurationBase<T extends EntityBase> implements ITableConfiguration<T> {
    protected final String name;
    protected final List<Property<T>> properties = new ArrayList<>();
    protected final List<Relation<T, ?>> relations = new ArrayList<>();

    protected TableConfigurationBase(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Property<T>> getProperties() {
        return properties;
    }

    public List<Property<T>> getColumns() {
        List<Property<T>> columns = new ArrayList<>(properties.stream().toList());
        List<Relation<T, ?>> relationColumns = relations.stream().filter(r -> r.getType() == RelationTypes.HAS_ONE).toList();

        columns.addAll(relationColumns);

        return columns;
    }

    public List<Relation<T, ?>> getRelations() {
        return relations.stream().filter(p -> p.getType() == RelationTypes.HAS_MANY_THROUGH || p.getType() == RelationTypes.HAS_MANY).toList();
    }

    public Optional<Relation<T, ?>> getRelation(String name) {
        return getRelations().stream().filter(r -> r.getName().equals(name)).findFirst();
    }
}
