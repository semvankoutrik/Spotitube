package nl.han.oose.dea.persistence.configuration;

import nl.han.oose.dea.domain.shared.EntityBase;
import nl.han.oose.dea.persistence.shared.Property;
import nl.han.oose.dea.persistence.shared.Relation;

import java.util.List;
import java.util.Optional;

public interface ITableConfiguration<T extends EntityBase> {
    String getName();
    List<Property<T>> getProperties();
    List<Property<T>> getColumns();
    List<Relation<T, ?>> getRelations();
    Optional<Relation<T, ?>> getRelation(String name);
}
