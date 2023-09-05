package nl.han.oose.dea.persistence.utils;

import nl.han.oose.dea.persistence.exceptions.DataTypeNotSupportedException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

public class PreparedStatementHelper {
    public static void setStatementParameter(PreparedStatement statement, int index, Object value) throws SQLException, DataTypeNotSupportedException {
        if (value == null) {
            statement.setNull(index, Types.NULL);
        } else if (value instanceof String) {
            statement.setString(index, (String) value);
        } else if (value instanceof Integer) {
            statement.setInt(index, (Integer) value);
        } else if (value instanceof Double) {
            statement.setDouble(index, (Double) value);
        } else if (value instanceof Date) {
            statement.setDate(index, new java.sql.Date(((Date) value).getTime()));
        } else if (value instanceof Boolean) {
            statement.setBoolean(index, (Boolean) value);
        } else {
            throw new DataTypeNotSupportedException(value.getClass().getName());
        }
    }
}
