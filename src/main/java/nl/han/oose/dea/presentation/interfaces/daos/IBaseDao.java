package nl.han.oose.dea.presentation.interfaces.daos;

import jakarta.annotation.PreDestroy;
import nl.han.oose.dea.domain.shared.EntityBase;
import nl.han.oose.dea.persistence.exceptions.DatabaseException;
import nl.han.oose.dea.persistence.exceptions.NotFoundException;

public interface IBaseDao<T extends EntityBase> {
    T get(String id) throws NotFoundException, DatabaseException;

    T insert(T entity) throws NotFoundException, DatabaseException;

    T update(T entity) throws DatabaseException;

    @PreDestroy
    void cleanup();
}
