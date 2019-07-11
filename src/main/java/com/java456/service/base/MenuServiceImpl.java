package com.java456.service.base;


import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.java456.dao.base.MenuDao;
import com.java456.entity.base.Menu;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;


@Service("menuService")
public class MenuServiceImpl implements MenuService {
	
	@Resource
	private MenuDao  menuDao;
	
	@Override
	public Integer add(Menu menu) {
		menuDao.save(menu);
		return 1;
	}
	
	@Override
	public Integer update(Menu menu) {
		Menu origin = menuDao.findId(menu.getId());
		menu = repalce(menu, origin);
		menuDao.save(menu);
		return 1;
	}
	
	@Override
	public List<Menu> list(Map<String, Object> map, Integer page, Integer pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC, "orderNo");
		Page<Menu> pages = menuDao.findAll(new Specification<Menu>() {
			
			@Override
			public Predicate toPredicate(Root<Menu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				// 加入 等于 divId  父节点 
				if (map.get("pId") != null) {
					predicate.getExpressions().add(cb.equal(root.get("pId"), map.get("pId")));
				}
				return predicate;
			}
		}, pageable);
		return pages.getContent();
	}
	
	@Override
	public Long getTotal(Map<String, Object> map){
		Long count=menuDao.count(new Specification<Menu>() {
			@Override
			public Predicate toPredicate(Root<Menu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				// 加入 等于 divId  父节点 
				if (map.get("pId") != null) {
					predicate.getExpressions().add(cb.equal(root.get("pId"), map.get("pId")));
				}
				return predicate;
			}
		});
		return count;
	}
	
	@Override
	public Menu findById(Integer id) {
		return menuDao.findId(id);
	}
	
	@Override
	public Integer delete(Integer id) {
		menuDao.deleteById(id);
		return 1;
	}
	
	
	@Override
	public List<Menu> findByPId(Integer pId) {
		return menuDao.findByPId(pId);
	}
	
	
	
	/**
	 * @param curr      当前更新的数据
	 * @param origin   源数据  以前的数据
	 * @return  curr
	 */
	public Menu repalce(Menu curr,Menu origin){
		
		if(curr.getpId()==null){
			curr.setpId(origin.getpId());
		}
		if(curr.getName()==null){
			curr.setName(origin.getName());
		}
		if(curr.getUrl()==null){
			curr.setUrl(origin.getUrl());
		}
		if(curr.getState()==null){
			curr.setState(origin.getState());
		}
		if(curr.getIcon()==null){
			curr.setIcon(origin.getIcon());
		}
		
		if(curr.getPermissions()==null){
			curr.setPermissions(origin.getPermissions());
		}
		if(curr.getType()==null){
			curr.setType(origin.getType());
		}
		if(curr.getDivId()==null){
			curr.setDivId(origin.getDivId());
		}
		
		return curr;
	}

	
	
	

}
