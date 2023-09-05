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
import java.util.logging.Level;

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

    protected abstract Supplier<T> entityFactory();

    public T mapResultSetToEntity(ResultSet resultSet) {
        T entity = entityFactory().get();

        getColumns().forEach((property) -> {
            try {
                Object value = resultSet.getObject(name + "." + property.getName());

                property.getSetter().accept(entity, value);
            } catch (SQLException e) {
                // TODO: Fix logger
//                logger.log(Level.SEVERE, "Error mapping the result set to entity.", e);

                throw new RuntimeException(e);
            }
        });

        return entity;
    }
}
