package nl.han.oose.dea.persistence.configuration;

import nl.han.oose.dea.domain.shared.EntityBase;
import nl.han.oose.dea.persistence.constants.RelationTypes;
import nl.han.oose.dea.persistence.shared.HasManyThroughRelation;
import nl.han.oose.dea.persistence.shared.Property;
import nl.han.oose.dea.persistence.shared.Relation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class TableConfigurationBase<T extends EntityBase> implements ITableConfiguration<T> {
    protected final String name;
    private final Logger logger;
    protected final List<Property<T>> properties = new ArrayList<>();
    protected final List<Relation<T, ? extends EntityBase>> relations = new ArrayList<>();

    protected TableConfigurationBase(String name, Logger logger) {
        this.name = name;
        this.logger = logger;
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

    public List<Relation<T, ? extends EntityBase>> getRelations() {
        return relations.stream().filter(p -> p.getType() == RelationTypes.HAS_MANY_THROUGH || p.getType() == RelationTypes.HAS_MANY).toList();
    }

    public Optional<Relation<T, ? extends EntityBase>> getRelation(String name) {
        return getRelations().stream().filter(r -> r.getName().equals(name)).findFirst();
    }

    public abstract Supplier<T> entityFactory();

    public T mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return mapResultSetToEntity(resultSet, false);
    }

    public T mapResultSetToEntity(ResultSet resultSet, boolean isRelation) throws SQLException {
        T entity = entityFactory().get();

        boolean relationEmpty = isRelation;
        List<String> invalidColumns = new ArrayList<>();

        for (Property<T> property : getColumns()) {
            Object value = resultSet.getObject(name + "." + property.getName());

            if (!isRelation) {
                if (value == null && !property.isNullable()) {
                    invalidColumns.add(property.getName());
                }

                if (value != null) {
                    property.setValue(entity, value);
                }
            } else {
                if (value == null && !property.isNullable()) {
                    invalidColumns.add(property.getName());
                }

                if (value != null) {
                    relationEmpty = false;
                    property.setValue(entity, value);
                }
            }
        }

        if (relationEmpty) {
            return null;
        }

        invalidColumns.forEach(c -> logger.log(Level.WARNING, "The value of the column \"" + getName() + "\".\"" + c + "\" defined as non-nullable is null."));

        return entity;
    }

    public void mapRelations(T entity, ResultSet resultSet) throws SQLException {
        for (HasManyThroughRelation<T, ?> relation : getRelations().stream().filter(r -> r.getType() == RelationTypes.HAS_MANY_THROUGH).map(r -> (HasManyThroughRelation<T, ?>) r).toList()) {
            var relationEntity = relation.getForeignTableConfiguration().mapResultSetToEntity(resultSet, true);

            var entityList = (List<EntityBase>) relation.getValue(entity);

            if (entityList == null) entityList = new ArrayList<>();

            if (relationEntity != null) {
                entityList.add(relationEntity);

                for (Property<?> property : relation.getProperties()) {
                    Object value = resultSet.getObject(relation.getLinkTable() + "." + property.getName());

                    property.setValue(relationEntity, value);
                }
            }

            relation.setValue(entity, entityList);
        }
    }
}
