package com.java456.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.java456.dao.blog.BlogDao;
import com.java456.dao.blog.LunBoDao;
import com.java456.entity.base.Config;
import com.java456.entity.base.Menu;
import com.java456.entity.base.RoleMenu;
import com.java456.entity.base.User;
import com.java456.entity.blog.Blog;
import com.java456.entity.blog.BlogType;
import com.java456.entity.blog.LunBo;
import com.java456.service.base.MenuService;
import com.java456.service.base.RoleMenuService;
import com.java456.service.blog.BlogService;
import com.java456.service.blog.BlogTypeService;
import com.java456.util.BrowserUtil;
import net.sf.json.JSONObject;


@Controller
public class IndexController {
	
	@Resource
	private RoleMenuService roleMenuService;
	@Resource
	private MenuService menuService;
	@Resource
	private BlogService blogService;
	@Resource
	private BlogDao blogDao;
	@Resource
	private LunBoDao  lunBoDao;
	@Resource
	private BlogTypeService  blogTypeService;
	@Autowired 
	private ServletContext servletContext;
	
	/**
	 * 请求首页
	 */
	@RequestMapping("/")
	public ModelAndView  index_1(HttpServletResponse  res,HttpServletRequest req) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		//加载首页数据
		Map<String,Object> data  = getIndexData();
		List<Blog> topList =(List<Blog>) data.get("topList");
		List<Blog>  homeList = (List<Blog>) data.get("homeList");
		
		
		//日期对应  文章数量
		List<JSONObject> dateList = blogDao.group();
		//日期对应  文章数量
		
		//拿分类列表
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("useIt", 1);
		List<BlogType> blogTypeList = blogTypeService.list(map, 0, 500);
		//拿分类列表
		
		//拿 轮播图片
		List<LunBo> lunboList = lunBoDao.list();
		//拿 轮播图片
		
		
		mav.addObject("topList", topList);
		mav.addObject("homeList", homeList);
		mav.addObject("dateList", dateList);
		mav.addObject("lunboList", lunboList);
		mav.addObject("blogTypeList", blogTypeList);
		//加载首页数据
		mav.addObject("total", 500);
		
		mav.setViewName("/pc/index");
		return mav;
	}
	
	/**
	 * 请求首页
	 */
	@RequestMapping("/index")
	public ModelAndView index(HttpServletResponse  res,HttpServletRequest req) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		//加载首页数据
		Map<String,Object> data  = getIndexData();
		List<Blog> topList =(List<Blog>) data.get("topList");
		List<Blog>  homeList = (List<Blog>) data.get("homeList");
		
		
		//日期对应  文章数量
		List<JSONObject> dateList = blogDao.group();
		//日期对应  文章数量
		
		//拿分类列表
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("useIt", 1);
		List<BlogType> blogTypeList = blogTypeService.list(map, 0, 500);
		//拿分类列表
		
		//拿 轮播图片
		List<LunBo> lunboList = lunBoDao.list();
		//拿 轮播图片
		
		mav.addObject("topList", topList);
		mav.addObject("homeList", homeList);
		mav.addObject("dateList", dateList);
		mav.addObject("lunboList", lunboList);
		mav.addObject("blogTypeList", blogTypeList);
		//加载首页数据
		
		mav.setViewName("/pc/index");
		return mav;
	}
	
	
	
	/**
	 * 电脑登陆
	 */
	@RequestMapping("/login")
	public ModelAndView login(HttpServletResponse  res,HttpServletRequest req) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		String UserAgent = req.getHeader("User-Agent");
		//判断AppleWebKit 和  Firefox
		if(BrowserUtil.checkUserAgent(UserAgent)){
			mav.setViewName("/pc/login/login");
		}else{
			mav.setViewName("/common/s_mode");
		}
		return mav;
	}
	
	
	/**
	 * 后台主页
	 */
	@RequestMapping("/admin/main")
	public ModelAndView admin_main(HttpServletResponse  res,HttpServletRequest req) throws Exception {
		ModelAndView mav = new ModelAndView();
		String UserAgent = req.getHeader("User-Agent");
		if(BrowserUtil.checkUserAgent(UserAgent)){
			mav.setViewName("/admin/main");
		}else{
			mav.setViewName("/common/s_mode");
			return mav;
		}
		
		User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute("currentUser"); 
		if(currentUser.getRole()==null){
			return mav;
		}
		
		List<JSONObject>  list =  new ArrayList<JSONObject>();
		List<Menu> menuList = menuService.findByPId(-1);
		for (Menu menu : menuList) {
			RoleMenu roleMenu=	roleMenuService.findByRoleIdAndMenuId(currentUser.getRole().getId(), menu.getId());
			if(roleMenu!=null){
				JSONObject node = new JSONObject();
				node.put("id", menu.getId());
				node.put("text", menu.getName());
				node.put("url", menu.getUrl());
				node.put("type", menu.getType());
				node.put("icon", menu.getIcon());
				node.put("divId", menu.getDivId());
				node.put("children", getChildren(menu.getId(),currentUser.getRole().getId()));
				list.add(node);
			}
		}
		mav.addObject("list", list);
		return mav;
	}
	
	/**
	 * 辅助方法  辅助 上面的 admin_main（getCheckedTreeMenu）
	 * @param pId
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public List<JSONObject> getChildren(Integer pId, Integer roleId)  throws Exception {
		List<Menu> menuList = menuService.findByPId(pId);
		List<JSONObject>  list =  new ArrayList<JSONObject>();
		for (Menu menu : menuList) {
			RoleMenu roleMenu=	roleMenuService.findByRoleIdAndMenuId(roleId, menu.getId());
			if(roleMenu!=null){
				JSONObject node = new JSONObject();
				node.put("id", menu.getId());
				node.put("text", menu.getName());
				node.put("url", menu.getUrl());
				node.put("type", menu.getType());
				node.put("icon", menu.getIcon());
				node.put("divId", menu.getDivId());
				list.add(node);
			}
		}
		return list;
	}
	
	
	public Map<String,Object> getIndexData(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("useIt", 1);
		map.put("state", 1);
		// state  //0未审校   1已审核
		List<Blog> topList = blogService.list(map, 0, 100);
		
		map.clear();
		map.put("state",1);
		Config config = (Config) servletContext.getAttribute("config");
		System.out.println(config.getIndexPageSize());
		List<Blog> homeList = blogService.list(map, 0, config.getIndexPageSize());
		
		map.clear();
		map.put("topList", topList);
		map.put("homeList", homeList);
		
		
		return map;
	}
	
}
