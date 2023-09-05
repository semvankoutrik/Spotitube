package nl.han.oose.dea.utils;

import java.util.UUID;
import java.util.function.Supplier;

public class DataSuppliers {
    public static Supplier<String> UUIDString = () -> UUID.randomUUID().toString();
}
