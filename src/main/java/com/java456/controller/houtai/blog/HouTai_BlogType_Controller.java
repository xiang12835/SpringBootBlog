package com.java456.controller.houtai.blog;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.java456.dao.base.UserDao;
import com.java456.dao.blog.BlogTypeDao;
import com.java456.entity.base.Role;
import com.java456.entity.base.User;
import com.java456.entity.blog.BlogType;

@Controller
@RequestMapping("/houtai/blog/type")
public class HouTai_BlogType_Controller {
	
	@Resource
	private BlogTypeDao blogTypeDao;
	
	
	/**
	 * /houtai/blog/type/manage
	 */
	@RequestMapping("/manage")
	public ModelAndView manage() throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.addObject("title", "博客类型管理");
		mav.setViewName("/admin/page/blog_type/blog_type_manage");
		return mav;
	}
	
	
	
	/**
	 * /houtai/blog/type/add
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/add")
	public ModelAndView add() throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.addObject("btn_text", "添加");
		mav.addObject("save_url", "/admin/blog/type/add");
		mav.setViewName("/admin/page/blog_type/add_update");
		return mav;
	}
	
	/**
	 * /houtai/blog/type/edit?id=1
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam(value = "id", required = false) Integer id) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		BlogType blogType = blogTypeDao.findId(id);
		mav.addObject("blogType", blogType);
		mav.addObject("btn_text", "修改");
		mav.addObject("save_url", "/admin/blog/type/update?id=" + id);
		mav.setViewName("/admin/page/blog_type/add_update");
		return mav;
	}
	
	
}
