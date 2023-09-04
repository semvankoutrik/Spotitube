package nl.han.oose.dea.persistence.daos;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.RequestScoped;
import nl.han.oose.dea.domain.shared.BaseEntity;
import nl.han.oose.dea.persistence.exceptions.DatabaseException;
import nl.han.oose.dea.persistence.exceptions.NotFoundException;
import nl.han.oose.dea.persistence.shared.Field;
import nl.han.oose.dea.persistence.utils.DatabaseConnection;
import nl.han.oose.dea.presentation.interfaces.daos.IBaseDao;

import java.sql.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public abstract class BaseDao<T extends BaseEntity> implements IBaseDao<T> {
    private final String tableName;
    protected Map<String, Field<T>> properties = new HashMap<>();
    private final Connection connection;
    protected final Logger logger;

    public String getTableName() {
        return tableName;
    }

    public BaseDao(String tableName, Logger logger) {
        this.tableName = tableName;
        this.logger = logger;
        this.connection = DatabaseConnection.create();
    }

    protected abstract Supplier<T> entityFactory();

    @Override
    public T get(String id) throws NotFoundException, DatabaseException {
        try {
            // Create and execute statement.
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ?");
            statement.setString(1, tableName);

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
    public T create(T entity) throws NotFoundException, DatabaseException {
        try {
            // INSERT INTO ? (column1, column2, column3
            StringBuilder queryBuilder = new StringBuilder("INSERT INTO ? (");
            String columnNames = properties.keySet().stream().map(Object::toString).collect(Collectors.joining(", "));
            queryBuilder.append(columnNames);

            // INSERT INTO ? (column1, column2, column3) VALUES (?, ?, ?)
            queryBuilder.append(") VALUES (");
            String values = String.join(", ", Collections.nCopies(properties.size(), "?"));
            queryBuilder.append(values);
            queryBuilder.append(")");

            // Create and execute statement
            PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());
            statement.setString(1, tableName);

            // TODO: Create function to set correct value.
            properties.entrySet().stream().forEach(p -> p.getValue());

            statement.execute();
            statement.close();

            return entity;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Something went wrong communicating with the database", e);

            throw new DatabaseException();
        }
    }

    private T mapToEntity(ResultSet resultSet) {
        T entity = entityFactory().get();

        properties.forEach((key, field) -> {
            try {
                Object value = resultSet.getObject(key);

                field.setter().accept(entity, value);
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