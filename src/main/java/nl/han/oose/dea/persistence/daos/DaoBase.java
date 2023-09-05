package nl.han.oose.dea.persistence.daos;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.RequestScoped;
import nl.han.oose.dea.domain.shared.EntityBase;
import nl.han.oose.dea.persistence.configuration.ITableConfiguration;
import nl.han.oose.dea.persistence.constants.RelationTypes;
import nl.han.oose.dea.persistence.exceptions.DataTypeNotSupportedException;
import nl.han.oose.dea.persistence.exceptions.DatabaseException;
import nl.han.oose.dea.persistence.exceptions.NotFoundException;
import nl.han.oose.dea.persistence.shared.Property;
import nl.han.oose.dea.persistence.utils.DatabaseConnection;
import nl.han.oose.dea.persistence.utils.Filter;
import nl.han.oose.dea.persistence.utils.PreparedStatementHelper;
import nl.han.oose.dea.presentation.interfaces.daos.IBaseDao;

import java.sql.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static nl.han.oose.dea.persistence.utils.PreparedStatementHelper.setStatementParameter;

@RequestScoped
public abstract class DaoBase<T extends EntityBase> implements IBaseDao<T> {
    private final ITableConfiguration<T> tableConfig;
    private final Connection connection;
    protected final Logger logger;

    protected abstract Supplier<T> entityFactory();

    public DaoBase(ITableConfiguration<T> tableConfig, Logger logger) {
        this.tableConfig = tableConfig;
        this.logger = logger;
        this.connection = DatabaseConnection.create();
    }

    private List<Property<T>> getColumns() {
        return tableConfig.getProperties().stream().filter(p -> p.getRelationType() == null || p.getRelationType() == RelationTypes.HAS_ONE).toList();
    }

    public List<T> get() throws DatabaseException {
        try {
            // Create and execute statement.
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"" + tableConfig.getName() + "\"");

            ResultSet resultSet = statement.executeQuery();

            List<T> entities = new ArrayList<>();

            while(resultSet.next()) {
                entities.add(mapToEntity(resultSet));
            }

            statement.close();

            return entities;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Something went wrong communicating with the database", e);

            throw new DatabaseException();
        }
    }

    public List<T> get(Filter filter) throws DatabaseException {
        try {
            // Create and execute statement.
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"" + tableConfig.getName() + "\" " + filter.toQuery());

            filter.setStatementParameters(statement, 1);

            ResultSet resultSet = statement.executeQuery();

            List<T> entities = new ArrayList<>();

            while(resultSet.next()) {
                entities.add(mapToEntity(resultSet));
            }

            statement.close();

            return entities;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Something went wrong communicating with the database", e);

            throw new DatabaseException();
        } catch (DataTypeNotSupportedException e) {
            logger.log(Level.SEVERE, "Given data-type not supported.", e);

            throw new RuntimeException(e);
        }
    }

    @Override
    public T get(String id) throws NotFoundException, DatabaseException {
        try {
            StringBuilder query = new StringBuilder("SELECT * FROM \"" + tableConfig.getName() + "\" WHERE \"" + tableConfig.getName() + "\".\"id\" = ?");

            // Create and execute statement.
            PreparedStatement statement = connection.prepareStatement(query.toString());
            statement.setString(1, id);

            ResultSet resultSet = statement.executeQuery();
            boolean found = resultSet.next();

            if (!found) throw new NotFoundException();

            T entity = mapToEntity(resultSet);

            statement.close();

            return entity;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Something went wrong communicating with the database", e);

            throw new DatabaseException();
        }
    }

    @Override
    public T insert(T entity) throws DatabaseException {
        try {
            List<Property<T>> columns = getColumns();

            // INSERT INTO ? (column1, column2, column3
            StringBuilder queryBuilder = new StringBuilder("INSERT INTO \"" + tableConfig.getName() + "\" (");
            String columnNames = columns.stream().map(Property::getName).collect(Collectors.joining(", "));
            queryBuilder.append(columnNames);

            // INSERT INTO ? (column1, column2, column3) VALUES (?, ?, ?)
            queryBuilder.append(") VALUES (");
            String values = String.join(", ", Collections.nCopies(columns.size(), "?"));
            queryBuilder.append(values);
            queryBuilder.append(")");

            // Prepare statement
            PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());

            // Set columns
            int index = 1;
            for (Property<T> property : getColumns()) {
                Object value = property.getGetter().apply(entity);

                if (property.getName().equals("id") && value == null) {
                    value = UUID.randomUUID().toString();
                }

                setStatementParameter(statement, index, value);

                index++;
            }

            // Execute and close.
            statement.execute();
            statement.close();

            return entity;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Something went wrong saving the data.", e);

            throw new DatabaseException();
        } catch (DataTypeNotSupportedException e) {
            logger.log(Level.SEVERE, "Data-type not supported: " + e.getDataType());

            throw new DatabaseException();
        }
    }

    @Override
    public T update(T entity) throws DatabaseException {
        try {
            List<Property<T>> columns = getColumns();

            // UPDATE ? SET column1 = ?, column2 = ?, column3 = ?
            StringBuilder queryBuilder = new StringBuilder("UPDATE " + tableConfig.getName() + " SET ");
            String placeholders = columns.stream().map(Property::getName).collect(Collectors.joining(" = ?, "));
            queryBuilder.append(placeholders);
            queryBuilder.append(" WHERE id = ?");

            // Prepare statement
            PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());

            // Set columns
            int index = 1;
            for (Property<T> property : columns) {
                Object value = property.getGetter().apply(entity);

                if (!property.getName().equals("id")) {
                    PreparedStatementHelper.setStatementParameter(statement, index, value);
                }

                index++;
            }

            PreparedStatementHelper.setStatementParameter(statement, index, entity.getId());

            // Execute and close.
            statement.execute();
            statement.close();

            return entity;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Something went wrong saving the data.", e);

            throw new DatabaseException();
        } catch (DataTypeNotSupportedException e) {
            logger.log(Level.SEVERE, "Data-type not supported: " + e.getDataType());

            throw new DatabaseException();
        }
    }

    private T mapToEntity(ResultSet resultSet) {
        T entity = entityFactory().get();

        getColumns().forEach((property) -> {
            try {
                Object value = resultSet.getObject(property.getName());

                property.getSetter().accept(entity, value);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error mapping the result set to entity.", e);

                throw new RuntimeException(e);
            }
        });

        return entity;
    }

    @Override
    @PreDestroy
    public void cleanup() {
        try {
            connection.close();
            logger.log(Level.INFO, "Connection with database closed.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not close database-connection.", e);
        }
    }
}