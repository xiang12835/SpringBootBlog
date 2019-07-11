package com.java456.dao.blog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import com.java456.entity.blog.BlogType;


public interface BlogTypeDao extends JpaRepository<BlogType,Integer>,JpaSpecificationExecutor<BlogType> {
	
	@Query(value="select * from t_a_blog_type  where id = ?1",nativeQuery = true)
	public BlogType findId(Integer id);
	
	
}
