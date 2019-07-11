package com.java456.service.blog;

import java.util.List;
import java.util.Map;
import com.java456.entity.blog.Link;

public interface LinkService {
	
	public Integer update(Link link );
	
	/**
	 * @param map
	 * @param page  从0开始 
	 * @param pageSize
	 */
	public List<Link> list(Map<String,Object> map,Integer page,Integer pageSize);
	
	public Long getTotal(Map<String,Object> map);
	
	
}
