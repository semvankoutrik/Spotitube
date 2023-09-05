package nl.han.oose.dea.persistence.shared;

import nl.han.oose.dea.persistence.constants.RelationTypes;

public class Relation {
    private final String foreignTable;
    private String foreignColumn;
    private String linkTable;
    private String linkColumn;
    private String foreignLinkColumn;
    private final RelationTypes type;

    public Relation(String foreignTable, String foreignColumn, RelationTypes type) {
        this.foreignTable = foreignTable;
        this.foreignColumn = foreignColumn;
        this.type = type;
    }

    public static Relation hasOne(String foreignTable, String foreignColumn) {
        return new Relation(foreignTable, foreignColumn, RelationTypes.HAS_ONE);
    }

    public static Relation hasMany(String foreignTable, String foreignColumn) {
        return new Relation(foreignTable, foreignColumn, RelationTypes.HAS_MANY);
    }

    public static Relation hasManyThrough(String linkTable, String linkColumn, String foreignLinkColumn, String foreignTable, String foreignColumn) {
        return new Relation(foreignTable, foreignColumn, RelationTypes.HAS_MANY_THROUGH)
                .setLinkTable(linkTable)
                .setLinkColumn(linkColumn)
                .setForeignLinkColumn(foreignLinkColumn);
    }

    public String getForeignColumn() {
        return foreignColumn;
    }

    public RelationTypes getType() {
        return type;
    }

    public String getForeignTable() {
        return foreignTable;
    }

    public Relation setForeignColumn(String foreignColumn) {
        this.foreignColumn = foreignColumn;

        return this;
    }

    public String getLinkTable() {
        return linkTable;
    }

    public Relation setLinkTable(String linkTable) {
        this.linkTable = linkTable;

        return this;
    }

    public String getLinkColumn() {
        return linkColumn;
    }

    public Relation setLinkColumn(String linkColumn) {
        this.linkColumn = linkColumn;

        return this;
    }

    public String getForeignLinkColumn() {
        return foreignLinkColumn;
    }

    public Relation setForeignLinkColumn(String foreignLinkColumn) {
        this.foreignLinkColumn = foreignLinkColumn;

        return this;
    }
}
