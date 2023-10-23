package nl.han.oose.dea.persistence.utils;

import nl.han.oose.dea.domain.shared.EntityBase;
import nl.han.oose.dea.persistence.shared.Property;

import java.util.List;

public class QueryHelper {
    public static <T extends EntityBase> String propertiesToQuery(String tableName, List<Property<T>> properties) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < properties.size(); i++) {
            Property<T> p = properties.get(i);

            builder.append(tableName).append(".").append(p.getName()).append(" ");
            builder.append("AS ").append(tableName).append("_").append(p.getName());

            if (i != properties.size() - 1) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }
}
