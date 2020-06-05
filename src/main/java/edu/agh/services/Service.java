package edu.agh.services;

import edu.agh.entities.Entity;

public interface Service<T> {
    T find(final Long id);
    void delete(final Long id);
    T createOrUpdate(T object);
}
