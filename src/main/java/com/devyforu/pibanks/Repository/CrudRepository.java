package com.devyforu.pibanks.Repository;

import java.util.List;

public interface CrudRepository <T>{
    List<T> findAll();
    T save(T toSave);
    T delete(T toDelete);
    T update(T toUpdate);
    T getById(String id);
}
