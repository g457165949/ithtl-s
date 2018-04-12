package com.ven.repo;

import com.ven.model.system.SysPermission;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SysPermissionRepository extends ExtJpaRepository<SysPermission,Integer>,JpaSpecificationExecutor {
	SysPermission findByName(String name);
}
