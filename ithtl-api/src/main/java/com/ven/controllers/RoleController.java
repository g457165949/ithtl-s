package com.ven.controllers;

import com.ven.model.system.SysPermission;
import com.ven.model.system.SysRole;
import com.ven.service.SysPermissionService;
import com.ven.service.SysRoleService;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/role/*")
public class RoleController extends BaseController {

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysPermissionService permissionService;

    @GetMapping("list")
    public Map<Object, Object> list(SysRole role) {
        Page<SysRole> rolePage = roleService.findAll(role, pageable());
        return success(rolePage);
    }

    @PostMapping("/edit")
    public Map<Object, Object> edit(SysRole role, @RequestParam(value = "id", defaultValue = "0") Integer id) {
        if (id == 0) {
            role.setCreatedAt((int) (System.currentTimeMillis() / 1000));
        }
        return success(roleService.dynamicSave(id, role));
    }

    @GetMapping("/delete/{id}")
    public Map<Object, Object> delete(@PathVariable Integer id) throws Exception {
        System.out.println("---->>delete:" + id);
        SysRole role = findModel(id);
        if (role != null) {
            roleService.delete(id);
        }
        return success();
    }

    @GetMapping("/view/{id}")
    public Map<Object,Object> view(@PathVariable Integer id, ModelMap map) throws Exception {
        System.out.println("---->>vew:" + id);
        SysRole role = findModel(id);
        List<Map<String,Object>> nodeList = new ArrayList<Map<String,Object>>();
        for (SysPermission p : role.getSysPermissions()) {
            Map<String,Object> nodeMap= new HashMap<String,Object>();
            nodeMap.put("id",p.getId());
            nodeMap.put("label",p.getName());
            nodeList.add(nodeMap);
        }
        map.addAttribute("menuItems",permissionService.items());
        map.addAttribute("nodeList",nodeList);
        return success(map);
    }

    @PostMapping("/view/{id}")
    public Map<Object, Object> view(@PathVariable Integer id, @RequestParam String permissionIds) throws Exception {
        SysRole role = findModel(id);
        String[] pids = permissionIds.split(",");
        if(pids!=null && pids.length > 0){
            List<String> list = new ArrayList<String>();
            List<SysPermission> plist = new ArrayList<SysPermission>();
            plist.addAll(role.getSysPermissions());
            for (SysPermission p : plist) {
                Arrays.sort(pids);
                int index = Arrays.binarySearch(pids, Integer.toString(p.getId()));
                if(index < 0){
                    role.getSysPermissions().remove(p);
                }else{
                    list.add(p.getId().toString());
                }
            }

            for (String s : pids){
                if(!list.contains(s)){
                    SysPermission permission = new SysPermission();
                    permission.setId(Integer.parseInt(s));
                    role.getSysPermissions().add(permission);
                }
            }
            roleService.dynamicSave(id,role);
        }
        return success();
    }


    /**
     * 查询权限对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    protected SysRole findModel(Integer id) throws Exception {
        SysRole role = roleService.findOne(id);
        if (role == null) {
            throw new Exception("Object is not find");
        }
        return role;
    }
}
