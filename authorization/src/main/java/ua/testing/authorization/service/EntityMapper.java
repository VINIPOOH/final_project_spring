package ua.testing.authorization.service;

public interface EntityMapper<T,E> {
    T mapToEntity(E e);
}

