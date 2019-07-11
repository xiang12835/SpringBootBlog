package com.java456.service.blog;



import java.util.Date;
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
import com.java456.dao.blog.BlogDao;
import com.java456.entity.blog.Blog;
import com.java456.util.DateUtil;
import com.java456.util.FileUtil;
import com.java456.util.HtmlUtil;
import com.java456.util.StringUtil;


@Service("blogService")
public class BlogServiceImpl implements BlogService {
	
	@Resource
	private BlogDao  blogDao;
	
	/**
	 * @param curr  当前更新的数据
	 * @param origin   源数据  以前的数据
	 * @return  curr
	 */
	public Blog repalce(Blog curr,Blog origin){
		if(curr.getTitle()==null){
			curr.setTitle(origin.getTitle());
		}
		if(curr.getContent()==null){
			curr.setContent(origin.getContent());
		}
		if(curr.getCreateDateTime()==null){
			curr.setCreateDateTime(origin.getCreateDateTime());
		}
		if(curr.getUpdateDateTime()==null){
			curr.setUpdateDateTime(origin.getUpdateDateTime());
		}
		
		
		if(curr.getUser() ==null){
			curr.setUser(origin.getUser());
		}
		if(curr.getState() ==null){
			curr.setState(origin.getState());
		}
		if(curr.getImageUrl() ==null){
			curr.setImageUrl(origin.getImageUrl());
		}
		if(curr.getUseIt()==null){
			curr.setUseIt(origin.getUseIt());
		}
		if(curr.getContentNoTag()==null){
			curr.setContentNoTag(origin.getContentNoTag());
		}
		
		
		if(curr.getBlogType()==null){
			curr.setBlogType(origin.getBlogType());
		}
		if(curr.getClickHit()==null){
			curr.setClickHit(origin.getClickHit());
		}
		if(curr.getReplyHit()==null){
			curr.setReplyHit(origin.getReplyHit());
		}
		if(curr.getSummary() ==null){
			curr.setSummary(origin.getSummary());
		}
		
		
		return curr;
	}
	
	@Override
	public Integer update(Blog blog,String webPath ) {
		Blog origin = blogDao.findId(blog.getId());
		
		//更换图片了原来有图片，删除掉
		if(StringUtil.isNotEmpty(blog.getImageUrl())){
			if(StringUtil.isNotEmpty(origin.getImageUrl())){
				FileUtil.deleteFile(webPath+origin.getImageUrl());
			}
		}
		//更换图片了原来有图片，删除掉
		
		//防止 更新isUse top时把图片删除掉    更新内容删除多余的图片
		String this_html = "";
		if(blog.getContent()==null){
			this_html = origin.getContent();
		}else{
			this_html = blog.getContent();
		}
		
		HtmlUtil.update_html_del_img(origin.getContent(), this_html, webPath);
		//防止 更新isUse top时把图片删除掉   更新内容删除多余的图片
		
		
		blog = repalce(blog, origin);
		blogDao.save(blog);
		
		return 1;
	}
	
	
	
	@Override
	public List<Blog> list(Map<String, Object> map, Integer page, Integer pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
		Page<Blog> pages = blogDao.findAll(new Specification<Blog>() {
			
			@Override
			public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				
				// 加入 等于 
				if (map.get("useIt") != null) {
					predicate.getExpressions().add(cb.equal(root.get("useIt"), map.get("useIt")));
				}
				// 加入 等于 
				if (map.get("state") != null) {
					predicate.getExpressions().add(cb.equal(root.get("state"), map.get("state")));
				}
				
				// 加入 等于 
				if (map.get("user") != null) {
					predicate.getExpressions().add(cb.equal(root.get("user"), map.get("user")));
				}
				
				// 加入 等于 
				if (map.get("blogType") != null) {
					predicate.getExpressions().add(cb.equal(root.get("blogType"), map.get("blogType")));
				}
				
				
				//日期  范围 
				if(map.get("date1") != null && map.get("date2") != null) {
					Date start = DateUtil.formatString(map.get("date1").toString(), "yyyy-MM-dd HH:mm");
					Date end = DateUtil.formatString(map.get("date2").toString(), "yyyy-MM-dd HH:mm");
					predicate.getExpressions().add(cb.between(root.get("createDateTime"),start,end));
				}
				
				// 模糊查询
				if (map.get("q") != null) {
					predicate.getExpressions().add(cb.or(cb.like(root.get("title"),"%"+map.get("q").toString()+"%"), cb.like(root.get("contentNoTag"),"%"+map.get("q").toString()+"%")));
				}
				
				return predicate;
			}
		}, pageable);
		return pages.getContent();
	}
	
	
	@Override
	public Long getTotal(Map<String, Object> map) {
		Long count=blogDao.count(new Specification<Blog>() {
			@Override
			public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				 
				// 加入 等于 
				if (map.get("useIt") != null) {
					predicate.getExpressions().add(cb.equal(root.get("useIt"), map.get("useIt")));
				}
				// 加入 等于 
				if (map.get("state") != null) {
					predicate.getExpressions().add(cb.equal(root.get("state"), map.get("state")));
				}
				
				// 加入 等于 
				if (map.get("user") != null) {
					predicate.getExpressions().add(cb.equal(root.get("user"), map.get("user")));
				}
				
				// 加入 等于 
				if (map.get("blogType") != null) {
					predicate.getExpressions().add(cb.equal(root.get("blogType"), map.get("blogType")));
				}
				
				//日期  范围 
				if(map.get("date1") != null && map.get("date2") != null) {
					Date start = DateUtil.formatString(map.get("date1").toString(), "yyyy-MM-dd HH:mm");
					Date end = DateUtil.formatString(map.get("date2").toString(), "yyyy-MM-dd HH:mm");
					predicate.getExpressions().add(cb.between(root.get("createDateTime"),start,end));
				}
				
				// 模糊查询
				if (map.get("q") != null) {
					predicate.getExpressions().add(cb.or(cb.like(root.get("title"),"%"+map.get("q").toString()+"%"), cb.like(root.get("contentNoTag"),"%"+map.get("q").toString()+"%")));
				}
				
				
				return predicate;
			}
		});
		return count;
	}
	
	
	@Override
	public void delete(Integer id,String webPath) {
		Blog origin = blogDao.findId(id);
		blogDao.deleteById(id);
		
		//删除全部图片
		HtmlUtil.delete_html_all_img(origin.getContent(), webPath);
		
		//删除封面
		if(origin.getImageUrl()!=null){
			FileUtil.deleteFile(webPath+origin.getImageUrl());
		}
		
	}
	
	
	
	
	
	
}
