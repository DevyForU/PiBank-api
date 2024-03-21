package com.devyforu.pibanks.Repository;

import java.util.List;

public interface CrudRepository <T>{
    List<T> findAll();
    T save(T toSave);
    T deleteById(String id);

    T getById(String id);
}
