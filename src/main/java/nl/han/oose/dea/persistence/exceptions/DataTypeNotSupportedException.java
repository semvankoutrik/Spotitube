package nl.han.oose.dea.persistence.exceptions;

public class DataTypeNotSupportedException extends Throwable {
    private final String dataType;

    public DataTypeNotSupportedException(String dataType) {
        this.dataType = dataType;
    }

    public String getDataType() {
        return dataType;
    }
}
