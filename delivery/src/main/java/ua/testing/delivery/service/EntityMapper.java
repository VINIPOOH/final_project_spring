package ua.testing.delivery.service;

interface EntityMapper<T, E> {
    T mapToEntity(E e);
}

