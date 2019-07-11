package com.java456.service.base;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.java456.dao.base.RoleMenuDao;
import com.java456.entity.base.RoleMenu;



@Service("roleMenuService")
public class RoleMenuServiceImpl implements RoleMenuService {
	
	@Resource
	private RoleMenuDao  roleMenuDao;
	
	@Override
	public Integer add(RoleMenu roleMenu) {
		roleMenuDao.save(roleMenu);
		return 1;
	}
	
	@Override
	public RoleMenu findByRoleIdAndMenuId(Integer roleId, Integer menuId) {
		return roleMenuDao.findByRoleIdAndMenuId(roleId, menuId);
	}
	
	@Override
	public Integer deleteByRoleId(Integer roleId) {
		return roleMenuDao.deleteByRoleId(roleId);
	}

}
