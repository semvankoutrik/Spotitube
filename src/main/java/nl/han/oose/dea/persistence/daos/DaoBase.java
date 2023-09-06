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
import nl.han.oose.dea.persistence.shared.Relation;
import nl.han.oose.dea.persistence.utils.*;
import nl.han.oose.dea.presentation.interfaces.daos.IBaseDao;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static nl.han.oose.dea.persistence.utils.PreparedStatementHelper.setStatementParameter;

@SuppressWarnings("unchecked")
@RequestScoped
public abstract class DaoBase<T extends EntityBase> implements IBaseDao<T> {
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

        includes.add(relationName);
    }

    public List<T> get() throws DatabaseException {
        try {
            PreparedStatement statement = connection.prepareStatement(selectQuery() + orderById());

            ResultSet resultSet = statement.executeQuery();

            List<T> entities = new ArrayList<>();

            String currentId = "";
            T currentEntity = null;

            while (resultSet.next()) {
                if (!currentId.equals(resultSet.getString(tableConfig.getName() + ".id"))) {
                    currentId = resultSet.getString(tableConfig.getName() + ".id");
                    currentEntity = tableConfig.mapResultSetToEntity(resultSet);
                    tableConfig.mapRelations(currentEntity, resultSet);
                    entities.add(currentEntity);
                } else {
                    tableConfig.mapRelations(currentEntity, resultSet);
                }
            }

            statement.close();

            return entities;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Something went wrong communicating with the database", e);

            throw new DatabaseException();
        }
    }

    public T get(String id) throws NotFoundException, DatabaseException {
        Optional<T> entity = get(Filters.equal("id", id)).stream().findFirst();

        if (entity.isEmpty()) throw new NotFoundException();

        return entity.get();
    }

    public List<T> get(Filter filter) throws DatabaseException {
        try {
            PreparedStatement statement = connection.prepareStatement(selectQuery() + " " + filter.toQuery() + orderById());

            filter.setStatementParameters(statement, 1);

            ResultSet resultSet = statement.executeQuery();

            List<T> entities = new ArrayList<>();

            while (resultSet.next()) {
                entities.add(tableConfig.mapResultSetToEntity(resultSet));
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
    public T insert(T entity) throws DatabaseException {
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
                Object value = property.getGetter().apply(entity);

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
            connection.setAutoCommit(false);

            List<Property<T>> columns = tableConfig.getColumns();

            // UPDATE ? SET column1 = ?, column2 = ?, column3 = ?
            StringBuilder queryBuilder = new StringBuilder("UPDATE \"" + tableConfig.getName() + "\" SET ");
            String placeholders = columns.stream().map(c -> "\"" + c.getName() + "\" = ?, ").collect(Collectors.joining());
            queryBuilder.append(placeholders);
            queryBuilder.append(" WHERE id = ?");

            PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());

            // Set ?-values
            int index = 1;
            for (Property<T> property : columns) {
                Object value = property.getGetter().apply(entity);

                if (!property.getName().equals("id")) {
                    PreparedStatementHelper.setStatementParameter(statement, index, value);
                }

                index++;
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

    private void updateRelations(T entity) throws SQLException {
        for (Relation<T, ?> relation : tableConfig.getRelations().stream().filter(r -> r.getType() == RelationTypes.HAS_MANY_THROUGH).toList()) {
            List<? extends EntityBase> objects = (List<? extends EntityBase>) relation.getGetter().apply(entity);

            if (objects != null) {
                String linkTable = relation.getLinkTable();
                String linkColumn = relation.getLinkColumn();
                String foreignLinkColumn = relation.getForeignLinkColumn();

                String deleteLinksQuery = "DELETE FROM \"" + linkTable + "\" WHERE \"" + linkColumn + "\" = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteLinksQuery);
                deleteStatement.setString(1, entity.getId());
                deleteStatement.execute();
                deleteStatement.close();

                String insertLinksQuery = "INSERT INTO \"" + linkTable + "\" (" + linkColumn + ", " + foreignLinkColumn + ") VALUES (?, ?)";

                for (EntityBase object : objects) {
                    PreparedStatement insertLinksStatement = connection.prepareStatement(insertLinksQuery);
                    insertLinksStatement.setString(1, entity.getId());
                    insertLinksStatement.setString(2, object.getId());

                    insertLinksStatement.execute();
                    insertLinksStatement.close();
                }
            }
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

    private String selectQuery() {
        StringBuilder query = new StringBuilder("SELECT ");

        query.append(QueryHelper.propertiesToQuery(tableConfig.getName(), tableConfig.getProperties()));

        if (!includes.isEmpty()) {
            for (String relation : includes) {
                Optional<Relation<T, ?>> relationProperty = tableConfig.getRelation(relation);

                if (relationProperty.isEmpty()) throw new NullPointerException();

                String tableName = relationProperty.get().getForeignTable();

                query.append(", ");

                query.append(QueryHelper.propertiesToQuery(tableName, relationProperty.get().getForeignTableConfiguration().getProperties()));
            }
        }

        query.append(" FROM \"").append(tableConfig.getName()).append("\" ");

        if (!includes.isEmpty()) {
            for (String relation : includes) {
                Optional<Relation<T, ?>> relationProperty = tableConfig.getRelation(relation);

                if (relationProperty.isEmpty()) throw new NullPointerException();

                if (relationProperty.get().getType() == RelationTypes.HAS_MANY_THROUGH) {
                    query.append(" LEFT JOIN \"").append(relationProperty.get().getLinkTable()).append("\" ");
                    query.append(" ON \"").append(relationProperty.get().getLinkTable()).append("\".\"").append(relationProperty.get().getLinkColumn()).append("\"");
                    query.append(" = \"").append(tableConfig.getName()).append("\".\"").append("id\" ");

                    query.append(" LEFT JOIN \"").append(relationProperty.get().getForeignTable()).append("\" ");
                    query.append(" ON \"").append(relationProperty.get().getLinkTable()).append("\".\"").append(relationProperty.get().getForeignLinkColumn()).append("\"");
                    query.append(" = \"").append(relationProperty.get().getForeignTable()).append("\".\"").append("id").append("\" ");
                }
            }
        }

        return query.toString();
    }

    private String orderById() {
        return " ORDER BY \"" + tableConfig.getName() + "\".\"" + "id" +"\"";
    }
}