package nl.han.oose.dea.persistence.configuration;

import nl.han.oose.dea.domain.entities.Playlist;
import nl.han.oose.dea.domain.shared.EntityBase;
import nl.han.oose.dea.persistence.constants.RelationTypes;
import nl.han.oose.dea.persistence.shared.Property;
import nl.han.oose.dea.persistence.shared.Relation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class TableConfigurationBase<T extends EntityBase> implements ITableConfiguration<T> {
    protected final String name;
    protected final List<Property<T>> properties = new ArrayList<>();
    protected final List<Relation<T, ? extends EntityBase>> relations = new ArrayList<>();

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

    public List<Relation<T, ? extends EntityBase>> getRelations() {
        return relations.stream().filter(p -> p.getType() == RelationTypes.HAS_MANY_THROUGH || p.getType() == RelationTypes.HAS_MANY).toList();
    }

    public Optional<Relation<T, ? extends EntityBase>> getRelation(String name) {
        return getRelations().stream().filter(r -> r.getName().equals(name)).findFirst();
    }

    public abstract Supplier<T> entityFactory();

    public T mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        T entity = entityFactory().get();

        for (Property<T> property : getColumns()) {
            Object value = resultSet.getObject(name + "." + property.getName());

            if (value == null && !property.isNullable()) {
                return null;
            }

            property.getSetter().accept(entity, value);
        }

        return entity;

    }

    public void mapRelations(T entity, ResultSet resultSet) throws SQLException {
        for (Relation<T, ?> relation : getRelations().stream().filter(r -> r.getType() == RelationTypes.HAS_MANY_THROUGH).toList()) {
            var relationEntity = relation.getForeignTableConfiguration().mapResultSetToEntity(resultSet);

            var entityList = (List<EntityBase>) relation.getGetter().apply(entity);

            if (entityList == null) entityList = new ArrayList<>();

            if (relationEntity != null) entityList.add(relationEntity);

            relation.getSetter().accept(entity, entityList);
        }
    }
}
