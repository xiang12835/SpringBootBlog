package com.java456.service.blog;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.java456.dao.blog.LinkDao;
import com.java456.entity.blog.Blog;
import com.java456.entity.blog.BlogType;
import com.java456.entity.blog.Link;
import com.java456.entity.blog.LunBo;
import com.java456.util.DateUtil;

@Service("linkService")
public class LinkServiceImpl implements LinkService {

	@Resource
	private LinkDao   linkDao ;
	
	@Autowired 
	private ServletContext servletContext;
	
	
	/**
	 * @param curr  当前更新的数据
	 * @param origin   源数据  以前的数据
	 * @return  curr
	 */
	public Link repalce(Link curr,Link origin){
		if(curr.getName()==null){
			curr.setName(origin.getName());
		}
		if(curr.getUrl()==null){
			curr.setUrl(origin.getUrl());
		}
		if(curr.getRemark()==null){
			curr.setRemark(origin.getRemark());
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
		
		return curr;
	}
	
	
	@Override
	public Integer update(Link link) {
		Link origin = linkDao.findId(link.getId());
		link = repalce(link, origin);
		linkDao.save(link);
		return 1;
	}

	@Override
	public List<Link> list(Map<String, Object> map, Integer page, Integer pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC, "orderNo");
		Page<Link> pages = linkDao.findAll(new Specification<Link>() {
			
			@Override
			public Predicate toPredicate(Root<Link> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				
				// 加入 等于 
				if (map.get("useIt") != null) {
					predicate.getExpressions().add(cb.equal(root.get("useIt"), map.get("useIt")));
				}
				
				// 模糊查询
				if (map.get("q") != null) {
					predicate.getExpressions().add(cb.or(cb.like(root.get("name"),"%"+map.get("q").toString()+"%"), cb.like(root.get("remark"),"%"+map.get("q").toString()+"%")));
				}
				
				return predicate;
			}
		}, pageable);
		return pages.getContent();
	}

	@Override
	public Long getTotal(Map<String, Object> map) {
		Long count=linkDao.count(new Specification<Link>() {
			@Override
			public Predicate toPredicate(Root<Link> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				 
				// 加入 等于 
				if (map.get("useIt") != null) {
					predicate.getExpressions().add(cb.equal(root.get("useIt"), map.get("useIt")));
				}
				
				// 模糊查询
				if (map.get("q") != null) {
					predicate.getExpressions().add(cb.or(cb.like(root.get("name"),"%"+map.get("q").toString()+"%"), cb.like(root.get("remark"),"%"+map.get("q").toString()+"%")));
				}
				
				
				return predicate;
			}
		});
		return count;
	}

	
	
	
}
