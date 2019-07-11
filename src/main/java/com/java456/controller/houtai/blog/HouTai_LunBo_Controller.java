package com.java456.controller.houtai.blog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.java456.dao.blog.BlogDao;
import com.java456.dao.blog.LunBoDao;
import com.java456.entity.blog.Blog;
import com.java456.entity.blog.BlogType;
import com.java456.entity.blog.LunBo;
import com.java456.service.blog.BlogService;
import com.java456.service.blog.LunBoService;

@Controller
@RequestMapping("/houtai/lunbo")
public class HouTai_LunBo_Controller {
	@Resource
	private LunBoDao  lunBoDao;
	@Resource
	private LunBoService  lunBoService;
	
	
	/**
	 * /houtai/lunbo/manage
	 */
	@RequestMapping("/manage")
	public ModelAndView manage() throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/admin/page/lunbo/lunbo_manage");
		return mav;
	}
	
	/**
	 * /houtai/lunbo/add
	 */
	@RequestMapping("/add")
	public ModelAndView add() throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.addObject("btn_text", "添加");
		mav.addObject("save_url", "/admin/lunbo/add");
		mav.setViewName("/admin/page/lunbo/add_update");
		return mav;
	}
	
	/**
	 * /houtai/lunbo/edit?id=1
	 */
	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam(value = "id", required = false) Integer id) throws Exception {
		ModelAndView mav = new ModelAndView();
		LunBo lunbo   = lunBoDao.findId(id);
		mav.addObject("lunbo", lunbo);
		mav.addObject("btn_text", "修改");
		mav.addObject("save_url", "/admin/lunbo/update?id=" + id);
		mav.setViewName("/admin/page/lunbo/add_update");
		return mav;
	}
	
	
	
	
	
}
