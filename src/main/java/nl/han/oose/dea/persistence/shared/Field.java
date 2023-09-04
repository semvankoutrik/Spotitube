package nl.han.oose.dea.persistence.shared;

import java.util.function.BiConsumer;
import java.util.function.Function;

public record Field<T>(BiConsumer<T, Object> setter, Function<T, Object> getter) { }
