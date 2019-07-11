package com.java456.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.java456.entity.base.Role;

public interface RoleDao  extends JpaRepository<Role,Integer>,JpaSpecificationExecutor<Role> {
	
	
	@Query(value="select * from t_a_role where id = ?1",nativeQuery = true)
	public Role findId(Integer id);
	
	
}
