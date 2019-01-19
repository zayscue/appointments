package edu.wgu.c195.appointments.persistence.data;

public interface IRepository<T> {
    T get(Object id);
    void add(T entity);
    void update(T entity);
    T delete(T entity);
    void save();
}
