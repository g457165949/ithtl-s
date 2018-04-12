package com.ven.repo;

import com.ven.model.system.SysRole;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleRepository extends ExtJpaRepository<SysRole,Integer>,JpaSpecificationExecutor {

    SysRole findByName(String name);

    SysRole dynamicSave(Integer id, SysRole role);
}
