package nl.han.oose.dea.persistence.configuration;

import nl.han.oose.dea.domain.shared.EntityBase;
import nl.han.oose.dea.persistence.shared.Property;

import java.util.List;

public interface ITableConfiguration<T extends EntityBase> {
    String getName();
    List<Property<T>> getProperties();
    List<Property<T>> getColumns();
    List<Property<T>> getRelations();
}
