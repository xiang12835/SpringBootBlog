package com.java456.service.blog;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.java456.dao.blog.BlogTypeDao;
import com.java456.entity.base.User;
import com.java456.entity.blog.BlogType;


@Service("blogTypeService")
public class BlogTypeServiceImpl implements BlogTypeService {
	
	@Resource
	private BlogTypeDao  blogTypeDao;
	
	
	/**
	 * @param curr  当前更新的数据
	 * @param origin   源数据  以前的数据
	 * @return  curr
	 */
	public BlogType repalce(BlogType curr,BlogType origin){
		if(curr.getName()==null){
			curr.setName(origin.getName());
		}
		if(curr.getUrl()==null){
			curr.setUrl(origin.getUrl());
		}
		if(curr.getUseIt()==null){
			curr.setUseIt(origin.getUseIt());
		}
		if(curr.getOrderNo()==null){
			curr.setOrderNo(origin.getOrderNo());
		}
		
		
		if(curr.getCreateDateTime()==null){
			curr.setCreateDateTime(origin.getCreateDateTime());
		}
		if(curr.getUpdateDateTime()==null){
			curr.setUpdateDateTime(origin.getUpdateDateTime());
		}
		
		
		if(curr.getCount()==null){
			curr.setCount(origin.getCount());
		}
		return curr;
	}
	
	@Override
	public Integer update(BlogType blogType) {
		BlogType origin = blogTypeDao.findId(blogType.getId());
		blogType = repalce(blogType, origin);
		blogTypeDao.save(blogType);
		return 1;
	}
	
	
	@Override
	public List<BlogType> list(Map<String, Object> map, Integer page, Integer pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC, "orderNo");
		Page<BlogType> pages = blogTypeDao.findAll(new Specification<BlogType>() {
			
			@Override
			public Predicate toPredicate(Root<BlogType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				
				
				// 加入 等于 
				if (map.get("useIt") != null) {
					predicate.getExpressions().add(cb.equal(root.get("useIt"), map.get("useIt")));
				}
				 
				
				return predicate;
			}
		}, pageable);
		return pages.getContent();
	}
	
	@Override
	public Long getTotal(Map<String, Object> map) {
		Long count=blogTypeDao.count(new Specification<BlogType>() {
			@Override
			public Predicate toPredicate(Root<BlogType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				 
				// 加入 等于 
				if (map.get("useIt") != null) {
					predicate.getExpressions().add(cb.equal(root.get("useIt"), map.get("useIt")));
				}
				
				return predicate;
			}
		});
		return count;
	}
	
	
	
}
