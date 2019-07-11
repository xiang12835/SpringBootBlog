package com.java456.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.java456.entity.base.Config;


public interface ConfigDao  extends JpaRepository< Config,Integer>,JpaSpecificationExecutor< Config> {
	
	@Query(value="select * from t_config  where id = ?1",nativeQuery = true)
	public  Config findId(Integer id);
	
	
}
