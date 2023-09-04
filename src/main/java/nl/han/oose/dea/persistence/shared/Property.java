package nl.han.oose.dea.persistence.shared;

import nl.han.oose.dea.persistence.constants.ColumnTypes;

import java.util.function.BiConsumer;
import java.util.function.Function;

public record Property<T>(String name, ColumnTypes type, BiConsumer<T, Object> setter, Function<T, Object> getter) { }
