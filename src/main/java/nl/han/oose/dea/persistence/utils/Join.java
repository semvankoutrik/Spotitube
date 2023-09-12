package nl.han.oose.dea.persistence.utils;

import nl.han.oose.dea.persistence.enums.JoinTypes;
import nl.han.oose.dea.persistence.exceptions.DataTypeNotSupportedException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

public class Join {
    private final JoinTypes type;
    private final String table;
    private final Filter filter;

    public Join(JoinTypes type, String table, Filter filter) {
        this.type = type;
        this.table = table;
        this.filter = filter;
    }

    public static Join leftJoin(String table, Filter filter) {
        return new Join(JoinTypes.LEFT_JOIN, table, filter);
    }

    public String toQuery() {
        StringBuilder query = new StringBuilder();

        switch (type) {
            case LEFT_JOIN -> {
                query.append(" LEFT JOIN \"").append(table).append("\" ON ");
                filter.toQuery(query);
            }
            default -> throw new UnsupportedOperationException();
        }

        return query.toString();
    }

    public void setStatementParameters(PreparedStatement preparedStatement, AtomicInteger index) throws DataTypeNotSupportedException, SQLException {
        filter.setStatementParameters(preparedStatement, index);
    }

    public JoinTypes getType() {
        return type;
    }
}
