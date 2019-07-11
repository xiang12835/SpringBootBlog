package com.java456.controller.houtai.blog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.java456.dao.blog.BlogDao;
import com.java456.entity.blog.Blog;
import com.java456.entity.blog.BlogType;
import com.java456.service.blog.BlogService;
import com.java456.service.blog.BlogTypeService;

@Controller
@RequestMapping("/houtai/blog")
public class HouTai_Blog_Controller {
	
	@Resource
	private BlogDao  blogDao;
	@Resource
	private BlogService  blogService;
	@Resource
	private BlogTypeService  blogTypeService;
	
	/**
	 * /houtai/blog/add
	 */
	@RequestMapping("/add")
	public ModelAndView add() throws Exception {
		ModelAndView mav = new ModelAndView();
		Map<String, Object> map = new HashMap<String, Object>();
		List<BlogType> typeList = blogTypeService.list(map, 0, 100);
		mav.addObject("typeList", typeList);
		
		mav.addObject("title", "添加博文");
		mav.addObject("btn_text", "提交");
		mav.addObject("state", 0);
		mav.addObject("save_url", "/admin/blog/add");
		mav.addObject("isEdit", false);
		mav.setViewName("/admin/page/blog/add_update");
		return mav;
	}
	
	/**
	 * /houtai/blog/edit?id=1
	 */
	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam(value = "id", required = false) Integer id) throws Exception {
		ModelAndView mav = new ModelAndView();
		Blog blog   = blogDao.findId(id);
		mav.addObject("blog", blog);
		mav.addObject("title", "修改博文");
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<BlogType> typeList = blogTypeService.list(map, 0, 100);
		mav.addObject("typeList", typeList);
		
		mav.addObject("isEdit", true);
		
		mav.addObject("btn_text", "修改");
		mav.addObject("save_url", "/admin/blog/update?id=" + id);
		mav.setViewName("/admin/page/blog/add_update");
		return mav;
	}
	
	
	/**
	 * /houtai/blog/manage
	 */
	@RequestMapping("/manage")
	public ModelAndView manage() throws Exception {
		ModelAndView mav = new ModelAndView();
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<BlogType> typeList = blogTypeService.list(map, 0, 100);
		mav.addObject("typeList", typeList);
		
		mav.setViewName("/admin/page/blog/blog_manage");
		return mav;
	}
	
	
	
	
	
	
}
