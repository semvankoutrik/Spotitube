package nl.han.oose.dea.persistence.configuration;

import nl.han.oose.dea.domain.shared.EntityBase;
import nl.han.oose.dea.persistence.constants.RelationTypes;
import nl.han.oose.dea.persistence.shared.Property;

import java.util.ArrayList;
import java.util.List;

public abstract class TableConfigurationBase<T extends EntityBase> implements ITableConfiguration<T> {
    protected final String name;
    protected final List<Property<T>> properties = new ArrayList<>();

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
        return properties.stream().filter(p -> p.getRelationType() == null || p.getRelationType() == RelationTypes.HAS_ONE).toList();
    }

    public List<Property<T>> getRelations() {
        return properties.stream().filter(p -> p.getRelationType() == RelationTypes.HAS_MANY_THROUGH || p.getRelationType() == RelationTypes.HAS_MANY).toList();
    }
}
