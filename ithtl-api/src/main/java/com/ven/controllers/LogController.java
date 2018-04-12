package com.ven.controllers;

import com.ven.model.system.SysLog;
import com.ven.service.SysLogService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;


@RestController
@RequestMapping("/log/*")
public class LogController extends BaseController{

    @Resource
    private SysLogService logService;

    @RequestMapping("list")
    public Map<Object,Object> list(SysLog log){
        Page<SysLog> logPage = logService.findAll(log,pageable());
        return success(logPage);
    }
}
