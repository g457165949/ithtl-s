package com.ven.service.impl;

import com.ven.model.system.SysPermission;
import com.ven.repo.SysPermissionRepository;
import com.ven.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysPermissionServiceImpl implements SysPermissionService {

	@Autowired
	private SysPermissionRepository permissionRepository;

	@Override
	public Page<SysPermission> findAll(SysPermission permission, Pageable pageable) {
		Specification<SysPermission> spec = new Specification<SysPermission>() {       //查询条件构造

			@Override
			public Predicate toPredicate(Root<SysPermission> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();

				if (StringUtils.isNotBlank(permission.getName())) {
					list.add(cb.like(root.get("name").as(String.class), "%" + permission.getName() + "%"));
				}

				Predicate[] p = new Predicate[list.size()];
				if (p.length > 0) {
					query.where(cb.or(list.toArray(p)));
				}

				return query.getRestriction();
			}
		};
		return permissionRepository.findAll(spec, pageable);
	}

	@Override
	public List<SysPermission> findAll(){
		return (List<SysPermission>) permissionRepository.findAll();
	}

	@Override
	public SysPermission findByName(String name) {
		return permissionRepository.findByName(name);
	}
	
	@Override
	public SysPermission findById(int id) {
		return permissionRepository.findOne(id);
	}
	
	@Override
	public SysPermission dynamicSave(Integer id, SysPermission permission) {
		return permissionRepository.dynamicSave(id,permission);
	}

	@Override
	public void delete(Integer id){
		permissionRepository.delete(id);
	}

	@Override
	public List<SysPermission> items(){
		// 原始的数据
		List<SysPermission> rootMenu = permissionRepository.findAll();
		// 最后的结果
		List<SysPermission> menuList = new ArrayList<SysPermission>();
		// 先找到所有的一级菜单
		for (int i = 0; i < rootMenu.size(); i++) {
			// 一级菜单没有parentId
			if (rootMenu.get(i).getParentId() == 0) {
				menuList.add(rootMenu.get(i));
			}
		}
		// 为一级菜单设置子菜单，getChild是递归调用的
		for (SysPermission menu : menuList) {
			menu.setChildPermissions(getChild(menu.getId(), rootMenu));
		}
		return menuList;
	}

	/**
	 * 递归查找子菜单
	 * @param id 当前菜单id
	 * @param rootMenu 要查找的列表
	 * @return
	 * */
	private List<SysPermission> getChild(Integer id, List<SysPermission> rootMenu) {
		// 子菜单
		List<SysPermission> childList = new ArrayList<>();
		for (SysPermission permission : rootMenu) {
			// 遍历所有节点，将父菜单id与传过来的id比较
			if (permission.getParentId() > 0) {
				if (permission.getParentId().equals(id)) {
					childList.add(permission);
				}
			}
		}
		// 把子菜单的子菜单再循环一遍
		for (SysPermission permission : childList) {
			// 没有url子菜单还有子菜单
			if (permission.getType() == 1) {
				permission.setChildPermissions(getChild(permission.getId(), rootMenu));
			}
		}
		// 递归退出条件
		if (childList.size() == 0) {
			return null;
		}
		return childList;
	}
}
