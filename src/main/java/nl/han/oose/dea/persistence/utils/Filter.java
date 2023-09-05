package nl.han.oose.dea.persistence.utils;

import nl.han.oose.dea.persistence.constants.FilterTypes;
import nl.han.oose.dea.persistence.exceptions.DataTypeNotSupportedException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Filter {
    private FilterTypes type;
    private String column;
    private Object value;
    private List<Filter> children;

    public static Filter andFilter(List<Filter> filters) {
        Filter filter = new Filter();
        filter.setChildren(filters);
        filter.setType(FilterTypes.AND);

        return filter;
    }

    public static Filter orFilter(List<Filter> filters) {
        Filter filter = new Filter();
        filter.setChildren(filters);
        filter.setType(FilterTypes.OR);

        return filter;
    }

    public static Filter equalFilter(String column, Object value) {
        Filter filter = new Filter();
        filter.setColumn(column);
        filter.setValue(value);
        filter.setType(FilterTypes.EQUAL);

        return filter;
    }

    public static Filter notEqualFilter(String column, Object value) {
        Filter filter = new Filter();
        filter.setColumn(column);
        filter.setValue(value);
        filter.setType(FilterTypes.NOT_EQUAL);

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
                for(int i = 0; i < children.size(); i++) {
                    children.get(i).toQuery(query);
                    if (i != children.size() - 1) {
                        query.append(" OR ");
                    }
                }
                query.append(") ");
            }
            case AND -> {
                query.append("(");
                for(int i = 0; i < children.size(); i++) {
                    children.get(i).toQuery(query);
                    if (i != children.size() - 1) {
                        query.append(" AND ");
                    }
                }
                query.append(") ");
            }
            case EQUAL -> {
                query.append(column);
                query.append(" = ? ");
            }
            case NOT_EQUAL -> {
                query.append(column);
                query.append(" != ? ");
            }
            default -> {
                throw new UnsupportedOperationException();
            }
        }

        return query;
    }

    public void setStatementParameters(PreparedStatement preparedStatement, int startingIndex) throws DataTypeNotSupportedException, SQLException {
        if (type == FilterTypes.EQUAL || type == FilterTypes.NOT_EQUAL) {
            PreparedStatementHelper.setStatementParameter(preparedStatement, startingIndex, value);
        } else if (type == FilterTypes.OR || type == FilterTypes.AND) {
            for(Filter filter : children) {
                PreparedStatementHelper.setStatementParameter(preparedStatement, startingIndex, filter);
                startingIndex++;
            }
        } else {
            throw new UnsupportedOperationException();
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

    public FilterTypes getType() {
        return type;
    }

    public String getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }

    public List<Filter> getChildren() {
        return children;
    }
}
