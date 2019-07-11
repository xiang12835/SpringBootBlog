package com.java456.service.blog;

import java.util.List;
import java.util.Map;

import com.java456.entity.base.User;
import com.java456.entity.blog.BlogType;

public interface BlogTypeService {

	public Integer update(BlogType blogType);
	
	
	/**
	 * @param map
	 * @param page  从0开始 
	 * @param pageSize
	 */
	public List<BlogType> list(Map<String,Object> map,Integer page,Integer pageSize);
	
	public Long getTotal(Map<String,Object> map);
	
	
	
}
