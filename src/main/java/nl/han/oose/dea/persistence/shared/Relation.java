package nl.han.oose.dea.persistence.shared;

import nl.han.oose.dea.persistence.constants.RelationTypes;

public class Relation {
    private final String tableName;
    private String column;
    private RelationTypes type;

    public Relation(String tableName, RelationTypes type) {
        this.tableName = tableName;
        this.type = type;
    }

    public static Relation hasOne(String tableName, String column) {
        return new Relation(tableName, RelationTypes.HAS_ONE)
                .setColumn(column);
    }

    public static Relation hasMany(String tableName, String column) {
        return new Relation(tableName, RelationTypes.HAS_MANY)
                .setColumn(column);
    }

    public String getColumn() {
        return column;
    }

    public Relation setColumn(String column) {
        this.column = column;

        return this;
    }

    public RelationTypes getType() {
        return type;
    }
}
