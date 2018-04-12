package com.ven.repo.impl;

import com.ven.repo.ExtJpaRepository;
import com.ven.utils.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.Serializable;

public class SimpleExtJpaRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements ExtJpaRepository<T, ID> {
    private final EntityManager em;

    public SimpleExtJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager em) {
        super(entityInformation, em);
        this.em = em;
    }

    public SimpleExtJpaRepository(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.em = em;
    }

    @Transactional
    public T dynamicSave(ID id, T entity) {
        T managedEntity = this.findOne(id);
        T mergedEntity;
        if (managedEntity == null) {
            em.persist(entity);
            mergedEntity = entity;
        } else {
            BeanUtils.copyProperties(entity, managedEntity, BeanUtil.getNullPropertyNames(entity));
            em.merge(managedEntity);
            mergedEntity = managedEntity;
        }
        return mergedEntity;
    }

}