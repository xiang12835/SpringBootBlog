package com.java456.service.base;

import java.util.List;
import java.util.Map;

import com.java456.entity.base.User;


public interface UserService {
	
	public Integer update(User user, String webPath);
	
	/**
	 * @param map
	 * @param page  从0开始 
	 * @param pageSize
	 */
	public List<User> list(Map<String,Object> map,Integer page,Integer pageSize);
	
	public Long getTotal(Map<String,Object> map);

}
