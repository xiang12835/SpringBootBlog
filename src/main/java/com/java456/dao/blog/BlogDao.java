package com.java456.dao.blog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import com.java456.entity.blog.Blog;

import net.sf.json.JSONObject;

public interface BlogDao  extends JpaRepository< Blog,Integer>,JpaSpecificationExecutor< Blog> {
	
	@Query(value="select * from t_blog   where id = ?1",nativeQuery = true)
	public Blog findId(Integer id);
	
	
	@Query(value="SELECT DATE_FORMAT(create_date_time,'%Y年%m月') AS DATE,COUNT(*) AS blogCount  	FROM   `t_blog` 	GROUP BY DATE_FORMAT(create_date_time,'%Y年%m月') ORDER BY  DATE_FORMAT(create_date_time,'%Y年%m月') DESC;",nativeQuery = true)
	public List<JSONObject> group();
	
	
}
