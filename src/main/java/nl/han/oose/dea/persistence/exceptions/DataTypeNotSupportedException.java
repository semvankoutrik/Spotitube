package nl.han.oose.dea.persistence.exceptions;

public class DataTypeNotSupportedException extends Throwable {
    private String dataType;

    public DataTypeNotSupportedException(String dataType) {
        this.dataType = dataType;
    }

    public String getDataType() {
        return dataType;
    }
}
