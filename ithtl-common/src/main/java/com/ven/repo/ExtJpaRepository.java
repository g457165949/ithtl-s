package com.ven.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface ExtJpaRepository <T, ID extends Serializable> extends JpaRepository<T,ID> {
    /**
     * insert or dynamic update entity (will findOne first)
     * @param id entity id
     * @param entity entity
     * @return entity
     */
    T dynamicSave(ID id, T entity);
}
