package nl.han.oose.dea.presentation.interfaces.daos;

import jakarta.annotation.PreDestroy;
import nl.han.oose.dea.domain.shared.EntityBase;
import nl.han.oose.dea.persistence.exceptions.DatabaseException;
import nl.han.oose.dea.persistence.exceptions.NotFoundException;

import java.util.List;

public interface IBaseDao<T extends EntityBase> {
    List<T> get() throws DatabaseException;
    T get(String id) throws NotFoundException, DatabaseException;

    T insert(T entity) throws DatabaseException;

    T update(T entity) throws DatabaseException;

    @PreDestroy
    void cleanup();
}
