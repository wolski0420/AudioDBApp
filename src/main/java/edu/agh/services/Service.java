package edu.agh.services;

import edu.agh.entities.Entity;

import java.util.Collection;

public interface Service<T> {
    T find(final Long id);
    void delete(final Long id);
    T createOrUpdate(T object);
    Collection<T> getEntitiesById(Collection<Long> ids);
}
