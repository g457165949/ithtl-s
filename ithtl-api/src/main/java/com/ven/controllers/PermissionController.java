package com.ven.controllers;

import com.ven.model.system.SysPermission;
import com.ven.service.SysPermissionService;
import org.omg.CosNaming.NamingContextPackage.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/permission/*")
public class PermissionController extends BaseController{

    @Resource
    SysPermissionService permissionService;

    @GetMapping("/list")
    public Map<Object, Object> list(SysPermission permission) {
        Page<SysPermission> permissionPage = permissionService.findAll(permission,pageable());
        return success(permissionPage);
    }

    @GetMapping("/items")
    public Map<Object, Object> items() {
        return success(permissionService.items());
    }

    @GetMapping("/edit")
    public Map<Object,Object> edit(@RequestParam(value = "parent_id", defaultValue = "0") Integer parent_id,
                                   @RequestParam(value = "id", defaultValue = "0") Integer id,
                                   ModelMap map) {
        if(parent_id > 0){
            SysPermission permission = permissionService.findById(parent_id);
            map.addAttribute("parent",permission);
        }
        if(id > 0){
            map.addAttribute("info",permissionService.findById(id));
        }

        return success(map);
    }

    @PostMapping("/edit")
    public Map<Object, Object> edit(SysPermission permission, @RequestParam(value = "id", defaultValue = "0") Integer id) {
        System.out.println(permission.toString());
        return success(permissionService.dynamicSave(id, permission));
    }

    @GetMapping("/delete/{id}")
    public Map<Object, Object> delete(@PathVariable Integer id) throws Exception {
        System.out.println("---->>delete:" + id);
        SysPermission permission = findModel(id);
        if (permission != null) {
            permissionService.delete(id);
        }
        return success();
    }
//
//    @GetMapping("view")
//    public String View(@RequestParam int id, Model model) throws Exception {
//        model.addAttribute("model", findModel(id));
//        return "permission/view";
//    }

    /**
     * 查询权限对象
     *
     * @param id
     * @return
     * @throws NotEmpty
     */
    protected SysPermission findModel(int id) throws Exception {
        SysPermission permission = permissionService.findById(id);
        if (permission == null) {
            throw new Exception("Object is not find");
        }
        return permission;
    }
}
