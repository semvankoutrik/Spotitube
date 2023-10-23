package nl.han.oose.dea.persistence.utils;

import nl.han.oose.dea.persistence.enums.FilterTypes;
import nl.han.oose.dea.persistence.exceptions.DataTypeNotSupportedException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Filter {
    private FilterTypes type;
    private String table;
    private String column;
    private String secondTable;
    private String secondColumn;
    private Object value;
    private List<Filter> children;

    public static Filter and(Filter... filters) {
        Filter filter = new Filter();
        filter.setChildren(Arrays.stream(filters).toList());
        filter.setType(FilterTypes.AND);

        return filter;
    }

    public static Filter or(Filter... filters) {
        Filter filter = new Filter();
        filter.setChildren(Arrays.stream(filters).toList());
        filter.setType(FilterTypes.OR);

        return filter;
    }

    public static Filter equal(String table, String column, String secondTable, String secondColumn) {
        Filter filter = new Filter();
        filter.setTable(table);
        filter.setColumn(column);
        filter.setSecondTable(secondTable);
        filter.setSecondColumn(secondColumn);
        filter.setType(FilterTypes.EQUAL);

        return filter;
    }

    public static Filter equal(String table, String column, Object value) {
        Filter filter = new Filter();
        filter.setTable(table);
        filter.setColumn(column);
        filter.setValue(value);
        filter.setType(FilterTypes.EQUAL);

        return filter;
    }

    public static Filter notEqual(String table, String column, Object value) {
        Filter filter = new Filter();
        filter.setTable(table);
        filter.setColumn(column);
        filter.setValue(value);
        filter.setType(FilterTypes.NOT_EQUAL);

        return filter;
    }

    public static Filter isNull(String table, String column) {
        Filter filter = new Filter();
        filter.setTable(table);
        filter.setColumn(column);
        filter.setType(FilterTypes.IS_NULL);

        return filter;
    }

    public static Filter isNotNull(String table, String column) {
        Filter filter = new Filter();
        filter.setTable(table);
        filter.setColumn(column);
        filter.setType(FilterTypes.IS_NOT_NULL);

        return filter;
    }

    public String toQuery() {
        StringBuilder queryBuilder = new StringBuilder("WHERE ");

        return toQuery(queryBuilder).toString();
    }

    public StringBuilder toQuery(StringBuilder query) {
        switch (type) {
            case OR -> {
                query.append("(");
                for (int i = 0; i < children.size(); i++) {
                    children.get(i).toQuery(query);
                    if (i != children.size() - 1) {
                        query.append(" OR ");
                    }
                }
                query.append(") ");
            }
            case AND -> {
                query.append("(");
                for (int i = 0; i < children.size(); i++) {
                    children.get(i).toQuery(query);
                    if (i != children.size() - 1) {
                        query.append(" AND ");
                    }
                }
                query.append(") ");
            }
            case EQUAL -> {
                query.append(table).append(".").append(column);

                if (value == null) {
                    query.append( "= ").append(secondTable).append(".").append(secondColumn);
                } else {
                    query.append(" = ? ");
                }
            }
            case NOT_EQUAL -> {
                query.append(table).append(".").append(column);

                if (value == null) {
                    query.append("!= ").append(secondTable).append(".").append(secondColumn);
                } else {
                    query.append(" != ? ");
                }
            }
            case IS_NULL -> {
                query.append(table).append(".").append(column);
                query.append(" IS NULL ");
            }
            case IS_NOT_NULL -> {
                query.append(table).append(".").append(column);
                query.append(" IS NOT NULL ");
            }
            default -> throw new UnsupportedOperationException();
        }

        return query;
    }

    public void setStatementParameters(PreparedStatement preparedStatement, int startingIndex) throws DataTypeNotSupportedException, SQLException {
        setStatementParameters(preparedStatement, new AtomicInteger(startingIndex));
    }

    /**
     * @param preparedStatement
     * @param startingIndex     Of type 'AtomicInteger' to ensure the starting index is incremented after each time the parameter is set.
     * @throws DataTypeNotSupportedException
     * @throws SQLException
     */
    public void setStatementParameters(PreparedStatement preparedStatement, AtomicInteger startingIndex) throws DataTypeNotSupportedException, SQLException {
        if (type != FilterTypes.IS_NULL && type != FilterTypes.IS_NOT_NULL) {
            if (type == FilterTypes.EQUAL || type == FilterTypes.NOT_EQUAL) {
                if (value != null) {
                    PreparedStatementHelper.setStatementParameter(preparedStatement, startingIndex.get(), value);
                } else {
                    return;
                }
            } else if (type == FilterTypes.OR || type == FilterTypes.AND) {
                for (Filter filter : children) {
                    filter.setStatementParameters(preparedStatement, startingIndex);
                }
            } else {
                throw new UnsupportedOperationException();
            }
            startingIndex.set(startingIndex.get() + 1);
        }
    }

    public void setType(FilterTypes type) {
        this.type = type;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setChildren(List<Filter> children) {
        this.children = children;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getSecondTable() {
        return secondTable;
    }

    public void setSecondTable(String secondTable) {
        this.secondTable = secondTable;
    }

    public String getSecondColumn() {
        return secondColumn;
    }

    public void setSecondColumn(String secondColumn) {
        this.secondColumn = secondColumn;
    }
}
