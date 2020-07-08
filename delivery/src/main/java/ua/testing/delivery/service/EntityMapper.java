package ua.testing.delivery.service;
/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface EntityMapper<T, E> {
    T mapToEntity(E e);
}

