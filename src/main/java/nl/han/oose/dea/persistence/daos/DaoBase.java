package nl.han.oose.dea.persistence.daos;

import jakarta.annotation.Nullable;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.RequestScoped;
import nl.han.oose.dea.domain.shared.EntityBase;
import nl.han.oose.dea.persistence.configurations.ITableConfiguration;
import nl.han.oose.dea.persistence.enums.RelationTypes;
import nl.han.oose.dea.persistence.exceptions.DataTypeNotSupportedException;
import nl.han.oose.dea.domain.exceptions.DatabaseException;
import nl.han.oose.dea.domain.exceptions.NotFoundException;
import nl.han.oose.dea.persistence.shared.HasManyThroughRelation;
import nl.han.oose.dea.persistence.shared.Property;
import nl.han.oose.dea.persistence.shared.Relation;
import nl.han.oose.dea.persistence.utils.*;
import nl.han.oose.dea.persistence.interfaces.daos.IDaoBase;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static nl.han.oose.dea.persistence.utils.PreparedStatementHelper.setStatementParameter;

@RequestScoped
public abstract class DaoBase<T extends EntityBase> implements IDaoBase<T> {
    protected final Logger logger;
    private final ITableConfiguration<T> tableConfig;
    private final Connection connection;
    private final List<String> includes = new ArrayList<>();

    public DaoBase(ITableConfiguration<T> tableConfig, Logger logger) {
        this.tableConfig = tableConfig;
        this.logger = logger;
        this.connection = DatabaseConnection.create();
    }

    public void include(String relationName) {
        if (tableConfig.getRelations().stream().noneMatch(r -> r.getName().equals(relationName))) {
            logger.log(Level.SEVERE, "Tried to include unknown relation: " + relationName);
        }

        if (includes.stream().noneMatch(i -> i.equals(relationName))) {
            includes.add(relationName);
        }
    }

    public List<T> get() throws DatabaseException {
        try {
            PreparedStatement statement = connection.prepareStatement(selectQuery() + orderById());

            return executeSelectAndMap(statement);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Something went wrong communicating with the database", e);

            throw new DatabaseException();
        }
    }

    public T get(String id) throws NotFoundException, DatabaseException {
        Optional<T> entity = get(Filter.equal(tableConfig.getName(), "id", id)).stream().findFirst();

        if (entity.isEmpty()) throw new NotFoundException();

        return entity.get();
    }

    public List<T> get(Join... joins) throws DatabaseException {
        return get(null, joins);
    }

    public List<T> get(@Nullable Filter filter, Join... joins) throws DatabaseException {
        try {
            StringBuilder query = new StringBuilder(selectQuery(false) + " ");

            Arrays.stream(joins).forEach(j -> query.append(j.toQuery()));

            if (filter != null) query.append(filter.toQuery());

            PreparedStatement statement = connection.prepareStatement(query.toString());

            AtomicInteger index = new AtomicInteger(1);

            for (Join join : joins) {
                join.setStatementParameters(statement, index);
            }

            if (filter != null) filter.setStatementParameters(statement, index);

            return executeSelectAndMap(statement);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Something went wrong communicating with the database", e);

            throw new DatabaseException();
        } catch (DataTypeNotSupportedException e) {
            logger.log(Level.SEVERE, "Given data-type not supported.", e);

            throw new RuntimeException(e);
        }
    }


    public List<T> get(Filter filter) throws DatabaseException {
        try {
            PreparedStatement statement = connection.prepareStatement(selectQuery() + " " + filter.toQuery() + orderById());

            filter.setStatementParameters(statement, 1);

            return executeSelectAndMap(statement);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Something went wrong communicating with the database", e);

            throw new DatabaseException();
        } catch (DataTypeNotSupportedException e) {
            logger.log(Level.SEVERE, "Given data-type not supported.", e);

            throw new RuntimeException(e);
        }
    }

    public void insert(T entity) throws DatabaseException {
        try {
            connection.setAutoCommit(false);

            List<Property<T>> columns = tableConfig.getColumns();

            // INSERT INTO ? (column1, column2, column3
            StringBuilder insertEntityQuery = new StringBuilder("INSERT INTO \"" + tableConfig.getName() + "\" (");
            String columnNames = columns.stream().map(Property::getName).collect(Collectors.joining(", "));
            insertEntityQuery.append(columnNames);

            // INSERT INTO ? (column1, column2, column3) VALUES (?, ?, ?)
            insertEntityQuery.append(") VALUES (");
            String values = String.join(", ", Collections.nCopies(columns.size(), "?"));
            insertEntityQuery.append(values);
            insertEntityQuery.append(")");

            PreparedStatement statement = connection.prepareStatement(insertEntityQuery.toString());

            // Set ?-values
            int index = 1;
            for (Property<T> property : tableConfig.getColumns()) {
                Object value = property.getValue(entity);

                if (property.getName().equals("id") && value == null) {
                    value = UUID.randomUUID().toString();
                    entity.setId((String) value);
                }

                setStatementParameter(statement, index, value);

                index++;
            }

            statement.execute();
            statement.close();

            updateRelations(entity);

            connection.commit();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Something went wrong saving the data.", e);

            throw new DatabaseException();
        } catch (DataTypeNotSupportedException e) {
            logger.log(Level.SEVERE, "Data-type not supported: " + e.getDataType());

            throw new DatabaseException();
        }
    }

    public T update(T entity) throws DatabaseException {
        try {
            connection.setAutoCommit(false);

            List<Property<T>> columns = tableConfig.getColumns();

            // UPDATE ? SET column1 = ?, column2 = ?, column3 = ?
            StringBuilder queryBuilder = new StringBuilder("UPDATE \"" + tableConfig.getName() + "\" SET ");
            String placeholders = columns.stream().filter(c -> !c.getName().equals("id")).map(c -> "\"" + c.getName() + "\" = ?, ").collect(Collectors.joining());
            queryBuilder.append(placeholders);
            // Remove trailing ", "
            queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
            queryBuilder.append(" WHERE \"").append(tableConfig.getName()).append("\".\"id\" = ?");

            PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());

            // Set ?-values
            int index = 1;
            for (Property<T> property : columns) {
                Object value = property.getValue(entity);

                if (!property.getName().equals("id")) {
                    PreparedStatementHelper.setStatementParameter(statement, index, value);

                    index++;
                }
            }

            PreparedStatementHelper.setStatementParameter(statement, index, entity.getId());

            statement.execute();
            statement.close();

            updateRelations(entity);

            connection.commit();

            return entity;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Something went wrong saving the data.", e);

            throw new DatabaseException();
        } catch (DataTypeNotSupportedException e) {
            logger.log(Level.SEVERE, "Data-type not supported: " + e.getDataType());

            throw new DatabaseException();
        }
    }

    public void delete(String id) throws DatabaseException {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + tableConfig.getName() + " WHERE id = ?");
            PreparedStatementHelper.setStatementParameter(statement, 1, id);

            statement.execute();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Something went wrong communicating with the database", e);

            throw new DatabaseException();
        } catch (DataTypeNotSupportedException e) {
            logger.log(Level.SEVERE, "Given data-type not supported.", e);

            throw new RuntimeException(e);
        }
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

    private List<T> executeSelectAndMap(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();

        List<T> entities = new ArrayList<>();

        String currentId = "";
        T currentEntity = null;

        while (resultSet.next()) {
            if (!currentId.equals(resultSet.getString(tableConfig.getName() + ".id"))) {
                currentId = resultSet.getString(tableConfig.getName() + ".id");
                currentEntity = tableConfig.mapResultSetToEntity(resultSet);
                if (!includes.isEmpty()) tableConfig.mapRelations(currentEntity, resultSet);
                entities.add(currentEntity);
            } else {
                if (!includes.isEmpty()) {
                    tableConfig.mapRelations(currentEntity, resultSet);
                }
            }
        }

        statement.close();

        return entities;
    }

    private void updateRelations(T entity) throws SQLException, DataTypeNotSupportedException {
        for (HasManyThroughRelation<T, ? extends EntityBase> relation : tableConfig.getRelations().stream().filter(r -> r.getType() == RelationTypes.HAS_MANY_THROUGH).map(r -> ((HasManyThroughRelation<T, ?>) r)).toList()) {
            if (relation.getGetter() == null) continue;

            List<? extends EntityBase> objects = (List<? extends EntityBase>) relation.getValue(entity);

            if (objects != null) {
                String linkTable = relation.getLinkTable();
                String linkColumn = relation.getLinkColumn();
                String foreignLinkColumn = relation.getForeignLinkColumn();

                // Delete
                String deleteLinksQuery = "DELETE FROM \"" + linkTable + "\" WHERE \"" + linkColumn + "\" = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteLinksQuery);
                deleteStatement.setString(1, entity.getId());
                deleteStatement.execute();
                deleteStatement.close();

                // Create relation with properties
                StringBuilder insertLinksQuery = new StringBuilder("INSERT INTO \"" + linkTable + "\" (" + linkColumn + ", " + foreignLinkColumn);
                var properties = relation.getProperties();
                properties.forEach(p -> insertLinksQuery.append(", ").append(p.getName()));
                insertLinksQuery.append(") VALUES (?, ?");
                properties.forEach(p -> insertLinksQuery.append(", ?"));
                insertLinksQuery.append(")");

                for (var object : objects) {
                    PreparedStatement insertLinksStatement = connection.prepareStatement(insertLinksQuery.toString());
                    insertLinksStatement.setString(1, entity.getId());
                    insertLinksStatement.setString(2, object.getId());

                    // Set ?-values of relation properties
                    int index = 3;
                    for (Property<? extends EntityBase> property : relation.getProperties()) {
                        Object value = property.getValue(object);

                        PreparedStatementHelper.setStatementParameter(insertLinksStatement, index, value);
                        index++;
                    }

                    insertLinksStatement.execute();
                    insertLinksStatement.close();
                }
            }
        }
    }

    private String selectQuery() {
        return selectQuery(true);
    }

    private String selectQuery(boolean useIncludes) {
        StringBuilder query = new StringBuilder("SELECT ");

        query.append(QueryHelper.propertiesToQuery(tableConfig.getName(), tableConfig.getProperties()));

        if (!includes.isEmpty() && useIncludes) {
            for (String relationTableName : includes) {
                Optional<Relation<T, ?>> relationProperty = tableConfig.getRelation(relationTableName);

                if (relationProperty.isEmpty()) throw new NullPointerException();

                String tableName = relationProperty.get().getForeignTable();

                query.append(", ");

                query.append(QueryHelper.propertiesToQuery(tableName, relationProperty.get().getForeignTableConfiguration().getProperties()));

                // Include relation properties
                if (relationProperty.get().getType() == RelationTypes.HAS_MANY_THROUGH) {
                    HasManyThroughRelation<T, ?> hasManyThroughRelation = (HasManyThroughRelation<T, ?>) relationProperty.get();

                    if (!hasManyThroughRelation.getProperties().isEmpty()) {
                        query.append(", ");
                        query.append(QueryHelper.propertiesToQuery(hasManyThroughRelation.getLinkTable(), hasManyThroughRelation.getProperties()));
                    }
                }
            }
        }

        query.append(" FROM \"").append(tableConfig.getName()).append("\" ");

        if (!includes.isEmpty() && useIncludes) {
            for (String relation : includes) {
                Optional<Relation<T, ?>> relationProperty = tableConfig.getRelation(relation);

                if (relationProperty.isEmpty()) throw new NullPointerException();

                if (relationProperty.get().getType() == RelationTypes.HAS_MANY_THROUGH) {
                    HasManyThroughRelation<T, ?> hasManyThroughRelation = (HasManyThroughRelation<T, ?>) relationProperty.get();

                    query.append(" LEFT JOIN \"").append(hasManyThroughRelation.getLinkTable()).append("\" ");
                    query.append(" ON \"").append(hasManyThroughRelation.getLinkTable()).append("\".\"").append(hasManyThroughRelation.getLinkColumn()).append("\"");
                    query.append(" = \"").append(tableConfig.getName()).append("\".\"").append("id\" ");

                    query.append(" LEFT JOIN \"").append(hasManyThroughRelation.getForeignTable()).append("\" ");
                    query.append(" ON \"").append(hasManyThroughRelation.getLinkTable()).append("\".\"").append(hasManyThroughRelation.getForeignLinkColumn()).append("\"");
                    query.append(" = \"").append(hasManyThroughRelation.getForeignTable()).append("\".\"").append("id").append("\" ");
                }
            }
        }

        return query.toString();
    }

    private String orderById() {
        return " ORDER BY \"" + tableConfig.getName() + "\".\"" + "id" + "\"";
    }
}