package com.java456.dao.base;


import java.util.List;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.java456.entity.base.RoleMenu;



public interface RoleMenuDao extends JpaRepository<RoleMenu,Integer>,JpaSpecificationExecutor<RoleMenu> {
	
	
	@Query(value="select * from t_role_menu where id = ?1",nativeQuery = true)
	public RoleMenu findId(Integer id);
	
	@Query(value="select * from t_role_menu where role_id = ?1 and menu_id =?2",nativeQuery = true)
	public RoleMenu findByRoleIdAndMenuId(Integer roleId,Integer menuId);
	
	/**
	 * 设置权限 时需要 清空  之前的权限 。
	 * @param roleId
	 */
	@Modifying
	@Transactional
	@Query(value="delete  from t_role_menu where role_id = ?1",nativeQuery = true)
	public Integer deleteByRoleId(Integer roleId);
	
	/**
	 * 根据 rokeid 拿数据   后台首页要用  /admin/mani控制器
	 * @param roleId
	 * @return
	 */
	@Query(value="select * from t_role_menu where role_id = ?1",nativeQuery = true)
	public List<RoleMenu> findByRoleId(Integer roleId);
	
	
	
	
}
