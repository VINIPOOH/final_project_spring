package ua.testing.delivery.service;

public interface EntityMapper<T, E> {
    T mapToEntity(E e);
}

