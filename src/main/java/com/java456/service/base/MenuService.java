package com.java456.service.base;

import java.util.List;
import java.util.Map;

import com.java456.entity.base.Menu;

public interface MenuService {
	
	
	public Integer add(Menu menu);
	
	public Integer update(Menu menu);
	
	/**
	 * @param map
	 * @param page  从0开始 
	 * @param pageSize
	 */
	public List<Menu> list(Map<String,Object> map,Integer page,Integer pageSize);
	
	public Long getTotal(Map<String,Object> map);
	
	public Menu findById(Integer id);
	
	public Integer delete(Integer id);
	
	public  List<Menu>  findByPId(Integer pId);
	
	
}
