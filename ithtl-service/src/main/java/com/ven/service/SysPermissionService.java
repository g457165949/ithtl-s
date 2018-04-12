package com.ven.service;

import com.ven.model.system.SysPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SysPermissionService {

	List<SysPermission> findAll();

	Page<SysPermission> findAll(SysPermission sysPermission, Pageable pageable);

	SysPermission findByName(String name);

	SysPermission findById(int id);

	void delete(Integer id);

	List<SysPermission> items();

	SysPermission dynamicSave(Integer id, SysPermission sysPermission);
}
