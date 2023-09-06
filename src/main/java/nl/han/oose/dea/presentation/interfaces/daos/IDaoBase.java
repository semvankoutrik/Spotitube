package nl.han.oose.dea.presentation.interfaces.daos;

import jakarta.annotation.PreDestroy;
import nl.han.oose.dea.domain.shared.EntityBase;
import nl.han.oose.dea.persistence.exceptions.DatabaseException;
import nl.han.oose.dea.persistence.exceptions.NotFoundException;
import nl.han.oose.dea.persistence.utils.Filter;

import java.util.List;

public interface IDaoBase<T extends EntityBase> {
    List<T> get() throws DatabaseException;
    T get(String id) throws NotFoundException, DatabaseException;
    List<T> get(Filter filter) throws DatabaseException;

    void insert(T entity) throws DatabaseException;

    T update(T entity) throws DatabaseException;

    @PreDestroy
    void cleanup();
}
