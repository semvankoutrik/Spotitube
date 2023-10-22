package nl.han.oose.dea.persistence.interfaces.daos;

import jakarta.annotation.Nullable;
import jakarta.annotation.PreDestroy;
import nl.han.oose.dea.domain.shared.EntityBase;
import nl.han.oose.dea.domain.exceptions.DatabaseException;
import nl.han.oose.dea.domain.exceptions.NotFoundException;
import nl.han.oose.dea.persistence.utils.Filter;
import nl.han.oose.dea.persistence.utils.Join;

import java.util.List;

public interface IDaoBase<T extends EntityBase> {
    List<T> get() throws DatabaseException;
    T get(String id) throws NotFoundException, DatabaseException;
    List<T> get(Filter filter) throws DatabaseException;
    List<T> get(@Nullable Filter filter, Join... joins) throws DatabaseException;
    List<T> get(Join... joins) throws DatabaseException;
    void insert(T entity) throws DatabaseException;
    T update(T entity) throws DatabaseException;
    void delete(String id) throws DatabaseException;
    void include(String relationName);

    @PreDestroy
    void cleanup();
}
