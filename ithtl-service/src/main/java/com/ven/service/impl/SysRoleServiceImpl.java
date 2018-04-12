package com.ven.service.impl;

import com.ven.model.system.SysRole;
import com.ven.repo.SysRoleRepository;
import com.ven.service.SysRoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleRepository roleRepository;

    @Override
    public SysRole dynamicSave(Integer id, SysRole role) {
        return roleRepository.dynamicSave(id, role);
    }

    @Override
    public Page<SysRole> findAll(SysRole role, Pageable pageable) {
        Specification<SysRole> spec = new Specification<SysRole>() {       //查询条件构造

            @Override
            public Predicate toPredicate(Root<SysRole> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();

//                if (StringUtils.isNotBlank(role.getName())) {
//                    list.add(cb.like(root.get("name").as(String.class), "%" + role.getName() + "%"));
//                }

                Predicate[] p = new Predicate[list.size()];
                if (p.length > 0) {
                    query.where(cb.or(list.toArray(p)));
                }

                return query.getRestriction();
            }
        };
        return roleRepository.findAll(spec, pageable);
    }

    @Override
    public List<SysRole> findAll(){
        return roleRepository.findAll();
    }

    @Override
    public SysRole findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public SysRole findOne(Integer id) {
        return (SysRole)roleRepository.findOne(id);
    }

    @Override
    public void delete(Integer id) {
        roleRepository.delete(id);
    }
}
