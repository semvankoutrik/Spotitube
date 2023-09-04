package nl.han.oose.dea.presentation.interfaces.daos;

import jakarta.annotation.PreDestroy;
import nl.han.oose.dea.domain.shared.BaseEntity;
import nl.han.oose.dea.persistence.exceptions.DatabaseException;
import nl.han.oose.dea.persistence.exceptions.NotFoundException;

public interface IBaseDao<T extends BaseEntity> {
    T get(String id) throws NotFoundException, DatabaseException;

    T create(T entity) throws NotFoundException, DatabaseException;

    @PreDestroy
    void cleanup();
}
