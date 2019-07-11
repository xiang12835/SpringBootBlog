package com.java456.dao.blog;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.java456.entity.blog.Link;


public interface LinkDao   extends JpaRepository<Link,Integer>,JpaSpecificationExecutor<Link> {
	
	
	@Query(value="select * from t_link  where id = ?1",nativeQuery = true)
	public Link findId(Integer id);
	
	
}
