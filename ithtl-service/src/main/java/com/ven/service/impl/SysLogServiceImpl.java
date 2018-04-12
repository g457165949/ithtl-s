package com.ven.service.impl;

import com.ven.model.system.SysLog;
import com.ven.repo.SysLogRepository;
import com.ven.service.SysLogService;
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
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogRepository sysLogRepository;

    @Override
    public Page<SysLog> findAll(SysLog log, Pageable pageable) {
        Specification<SysLog> spec = new Specification<SysLog>() {       //查询条件构造

            @Override
            public Predicate toPredicate(Root<SysLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();

                Predicate[] p = new Predicate[list.size()];
                if (p.length > 0) {
                    query.where(cb.or(list.toArray(p)));
                }
                query.orderBy(cb.desc(root.get("createdAt")));
                return query.getRestriction();
            }
        };
        return sysLogRepository.findAll(spec, pageable);
    }

    @Override
    public SysLog save(SysLog log){
        return sysLogRepository.save(log);
    }
}
