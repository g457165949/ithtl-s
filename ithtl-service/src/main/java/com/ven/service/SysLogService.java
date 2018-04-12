package com.ven.service;

import com.ven.model.system.SysLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface SysLogService {

    Page<SysLog> findAll(SysLog log, Pageable pageable);

    SysLog save(SysLog log);
}
