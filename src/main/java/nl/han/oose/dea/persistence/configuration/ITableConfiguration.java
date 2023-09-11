package nl.han.oose.dea.persistence.configuration;

import nl.han.oose.dea.domain.shared.EntityBase;
import nl.han.oose.dea.persistence.shared.Property;
import nl.han.oose.dea.persistence.shared.Relation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface ITableConfiguration<T extends EntityBase> {
    String getName();
    List<Property<T>> getProperties();
    List<Property<T>> getColumns();
    List<Relation<T, ? extends EntityBase>> getRelations();
    Optional<Relation<T, ? extends EntityBase>> getRelation(String name);
    T mapResultSetToEntity(ResultSet resultSet) throws SQLException;
    T mapResultSetToEntity(ResultSet resultSet, boolean isRelation) throws SQLException;
    void mapRelations(T entity, ResultSet resultSet) throws SQLException;

    Supplier<T> entityFactory();
}
