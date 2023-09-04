package nl.han.oose.dea.persistence.shared;

public record Relation(String table, String column, boolean toMany) { }
