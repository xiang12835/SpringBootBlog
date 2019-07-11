package com.java456.service.base;

import com.java456.entity.base.RoleMenu;

public interface RoleMenuService {
	
	public Integer add(RoleMenu roleMenu);
	
	
	public RoleMenu findByRoleIdAndMenuId(Integer roleId,Integer menuId);
	
	
	public Integer deleteByRoleId(Integer roleId);
	
	
}
