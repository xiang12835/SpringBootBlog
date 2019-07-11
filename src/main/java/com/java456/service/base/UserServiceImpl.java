package com.java456.service.base;

 
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.java456.dao.base.UserDao;
import com.java456.entity.base.User;
import com.java456.util.FileUtil;
import com.java456.util.StringUtil;


@Service("userService")
public class UserServiceImpl implements UserService {
	
	@Resource
	private UserDao  userDao;
	
	
	@Override
	public Integer update(User user, String webPath) {
		User origin = userDao.findId(user.getId());
		
		//更换图片了原来有图片，删除掉
		if(StringUtil.isNotEmpty(user.getImageUrl())){
			if(StringUtil.isNotEmpty(origin.getImageUrl())){
				FileUtil.deleteFile(webPath+origin.getImageUrl());
			}
		}
		//更换图片了原来有图片，删除掉
				
		
		user = repalce(user, origin);
		userDao.save(user);
		return 1;
	}
	
	@Override
	public List<User> list(Map<String, Object> map, Integer page, Integer pageSize) {
		Pageable pageable=new PageRequest(0, 11, Sort.Direction.ASC,"orderNo");
		Page<User> list = userDao.findAll(pageable);
		return  list.getContent();//拿到list集合
	}
	
	@Override
	public Long getTotal(Map<String, Object> map) {
		return userDao.count();
	}
	
	
	/**
	 * @param curr  当前更新的数据
	 * @param origin   源数据  以前的数据
	 * @return  curr
	 */
	public User repalce(User curr,User origin){
		
		if(curr.getName()==null){
			curr.setName(origin.getName());
		}
		if(curr.getPwd()==null){
			curr.setPwd(origin.getPwd());
		}
		if(curr.getTrueName()==null){
			curr.setTrueName(origin.getTrueName());
		}
		if(curr.getRemark()==null){
			curr.setRemark(origin.getRemark());
		}
		if(curr.getOrderNo()==null){
			curr.setOrderNo(origin.getOrderNo());
		}
		if(curr.getCreateDateTime()==null){
			curr.setCreateDateTime(origin.getCreateDateTime());
		}
		if(curr.getRole()==null){
			curr.setRole(origin.getRole());
		}
		
		
		if(curr.getImageUrl()==null){
			curr.setImageUrl(origin.getImageUrl());
		}
		
		
		return curr;
	}

	
	
	
	
}
