package nl.han.oose.dea.persistence.utils;

import java.util.Arrays;

public class Filters {
    public static Filter and(Filter... filters) {
        return Filter.andFilter(Arrays.stream(filters).toList());
    }

    public static Filter or(Filter... filters) {
        return Filter.orFilter(Arrays.stream(filters).toList());
    }

    public static Filter equal(String column, Object value) {
        return Filter.equalFilter(column, value);
    }

    public static Filter notEqual(String column, Object value) {
        return Filter.notEqualFilter(column, value);
    }
}
