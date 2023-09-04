package nl.han.oose.dea.persistence.daos;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.RequestScoped;
import nl.han.oose.dea.domain.shared.BaseEntity;
import nl.han.oose.dea.persistence.constants.RelationType;
import nl.han.oose.dea.persistence.exceptions.DataTypeNotSupportedException;
import nl.han.oose.dea.persistence.exceptions.DatabaseException;
import nl.han.oose.dea.persistence.exceptions.NotFoundException;
import nl.han.oose.dea.persistence.shared.Property;
import nl.han.oose.dea.persistence.utils.DatabaseConnection;
import nl.han.oose.dea.presentation.interfaces.daos.IBaseDao;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public abstract class BaseDao<T extends BaseEntity> implements IBaseDao<T> {
    private final String tableName;
    private final Connection connection;
    protected List<Property<T>> properties = new ArrayList<>();
    protected final Logger logger;

    protected abstract Supplier<T> entityFactory();

    public BaseDao(String tableName, Logger logger) {
        this.tableName = tableName;
        this.logger = logger;
        this.connection = DatabaseConnection.create();
    }

    private List<Property<T>> getColumns() {
        return properties.stream().filter(p -> {
            return p.getRelationType() == null ||
                    p.getRelationType() == RelationType.ONE_TO_ONE ||
                    p.getRelationType() == RelationType.MANY_TO_ONE;
        }).toList();
    }

    @Override
    public T get(String id) throws NotFoundException, DatabaseException {
        try {
            // Create and execute statement.
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName);

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
    public T create(T entity) throws DatabaseException {
        try {
            List<Property<T>> columns = getColumns();

            // INSERT INTO ? (column1, column2, column3
            StringBuilder queryBuilder = new StringBuilder("INSERT INTO " + tableName + " (");
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

                if (property.getName().equals("id")) {
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
            StringBuilder queryBuilder = new StringBuilder("UPDATE " + tableName + " SET ");
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
                    setStatementParameter(statement, index, value);
                }

                index++;
            }

            setStatementParameter(statement, index, entity.getId());

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

    public void setStatementParameter(PreparedStatement statement, int index, Object value) throws SQLException, DataTypeNotSupportedException {
        if (value == null) {
            statement.setNull(index, Types.NULL);
        } else if (value instanceof String) {
            statement.setString(index, (String) value);
        } else if (value instanceof Integer) {
            statement.setInt(index, (Integer) value);
        } else if (value instanceof Double) {
            statement.setDouble(index, (Double) value);
        } else if (value instanceof Date) {
            statement.setDate(index, new java.sql.Date(((Date) value).getTime()));
        } else if (value instanceof Boolean) {
            statement.setBoolean(index, (Boolean) value);
        } else {
            throw new DataTypeNotSupportedException(value.getClass().getName());
        }
    }

    private T mapToEntity(ResultSet resultSet) {
        T entity = entityFactory().get();

        properties.forEach((property) -> {
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