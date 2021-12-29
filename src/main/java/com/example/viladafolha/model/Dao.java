package com.example.viladafolha.model;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> getById(Long id);
    T getByEmail(String email);
    List<T> getAll();
    void save(T t);
    void update(T t, String[] params);
    void delete(T t);
}
