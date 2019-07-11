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

import com.java456.dao.blog.LunBoDao;
import com.java456.entity.blog.Blog;
import com.java456.entity.blog.LunBo;
import com.java456.util.DateUtil;
import com.java456.util.FileUtil;
import com.java456.util.HtmlUtil;
import com.java456.util.StringUtil;



@Service("lunBoService")
public class LunBoServiceImpl implements LunBoService {
	

	@Resource
	private LunBoDao   lunBoDao ;
	
	
	/**
	 * @param curr  当前更新的数据
	 * @param origin   源数据  以前的数据
	 * @return  curr
	 */
	public LunBo repalce(LunBo curr,LunBo origin){
		if(curr.getName()==null){
			curr.setName(origin.getName());
		}
		if(curr.getUrl()==null){
			curr.setUrl(origin.getUrl());
		}
		if(curr.getImageUrl()==null){
			curr.setImageUrl(origin.getImageUrl());
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
	public Integer update(LunBo lunbo, String webPath) {
		LunBo origin = lunBoDao.findId(lunbo.getId());
		
		//更换图片了原来有图片，删除掉
		if(StringUtil.isNotEmpty(lunbo.getImageUrl())){
			if(StringUtil.isNotEmpty(origin.getImageUrl())){
				FileUtil.deleteFile(webPath+origin.getImageUrl());
			}
		}
		//更换图片了原来有图片，删除掉
		
		lunbo = repalce(lunbo, origin);
		lunBoDao.save(lunbo);
		return 1;
	}


	@Override
	public List<LunBo> list(Map<String, Object> map, Integer page, Integer pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC, "orderNo");
		Page<LunBo> pages = lunBoDao.findAll(new Specification<LunBo>() {
			
			@Override
			public Predicate toPredicate(Root<LunBo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
		Long count=lunBoDao.count(new Specification<LunBo>() {
			@Override
			public Predicate toPredicate(Root<LunBo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
	
	
	@Override
	public void delete(Integer id, String webPath) {
		LunBo origin = lunBoDao.findId(id);
		lunBoDao.deleteById(id);
		
		//删除封面
		if(origin.getImageUrl()!=null){
			FileUtil.deleteFile(webPath+origin.getImageUrl());
		}
		//删除封面
	}
	
	
	
	
}
