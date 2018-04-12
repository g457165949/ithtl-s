package com.ven.repo;

import com.ven.model.system.SysLog;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysLogRepository extends ExtJpaRepository<SysLog,Integer>,JpaSpecificationExecutor {
}
