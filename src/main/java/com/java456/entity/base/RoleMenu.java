package com.java456.entity.base;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 角色 菜单  关联实体  （中间表）
 * 如果是easyui拿菜单，这样拿，根据角色拿 菜单  过虑父节点id是-1，  拿是根据节点，然后遍历换父节点id
 */
@Entity
@Table(name="t_role_menu")
public class RoleMenu {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="roleId")
	private Role role; // 角色
	
	@ManyToOne
	@JoinColumn(name="menuId")
	private Menu menu; // 菜单
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	@Override
	public String toString() {
		return "RoleMenu [id=" + id + ", role=" + role + ", menu=" + menu + "]";
	}
	
	
}
