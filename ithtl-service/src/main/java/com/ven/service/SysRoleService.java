package com.ven.service;

import com.ven.model.system.SysRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SysRoleService {

    SysRole dynamicSave(Integer id, SysRole role);

    Page<SysRole> findAll(SysRole role, Pageable pageable);

    List<SysRole> findAll();

    SysRole findByName(String name);

    SysRole findOne(Integer id);

    void delete(Integer id);
}
