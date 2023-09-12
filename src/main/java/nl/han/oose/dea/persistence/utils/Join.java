package nl.han.oose.dea.persistence.utils;

import nl.han.oose.dea.persistence.enums.JoinTypes;

import java.util.List;

public class Join {
    private final String foreignKey;
    private final String onTable;
    private final String onColumn;
    private final JoinTypes type;

    public Join(String foreignKey, String onTable, String onColumn, JoinTypes type) {
        this.foreignKey = foreignKey;
        this.onTable = onTable;
        this.onColumn = onColumn;
        this.type = type;
    }

    public static Join leftJoin(String foreignKey, String onTable, String onColumn) {
        return new Join(foreignKey, onTable, onColumn, JoinTypes.LEFT_JOIN);
    }

    public String toQuery() {
        StringBuilder query = new StringBuilder();

        switch (type) {
            case LEFT_JOIN -> {

            }
            default -> throw new UnsupportedOperationException();
        }

        return query.toString();
    }

    public String getForeignKey() {
        return foreignKey;
    }

    public String getOnTable() {
        return onTable;
    }

    public String getOnColumn() {
        return onColumn;
    }

    public JoinTypes getType() {
        return type;
    }
}
