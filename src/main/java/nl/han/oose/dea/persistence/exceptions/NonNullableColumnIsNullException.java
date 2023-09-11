package nl.han.oose.dea.persistence.exceptions;

public class NonNullableColumnIsNullException extends RuntimeException {
    private final String columnName;

    public NonNullableColumnIsNullException(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}
