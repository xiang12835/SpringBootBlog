package com.java456.dao.blog;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.java456.entity.blog.LunBo;


public interface LunBoDao  extends JpaRepository<LunBo,Integer>,JpaSpecificationExecutor<LunBo> {
	
	@Query(value="select * from t_lunbo  where id = ?1",nativeQuery = true)
	public LunBo findId(Integer id);
	
	
	@Query(value="SELECT * FROM `t_lunbo` GROUP BY  order_no",nativeQuery = true)
	public List<LunBo>  list();
	
	
}
