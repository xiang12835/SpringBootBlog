package com.java456.controller.houtai.blog;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.java456.dao.blog.LinkDao;
import com.java456.entity.blog.BlogType;
import com.java456.entity.blog.Link;

@Controller
@RequestMapping("/houtai/link")
public class HouTai_Link_Controller {
	

	@Resource
	private  LinkDao linkDao;
	
	
	/**
	 * /houtai/link/manage
	 */
	@RequestMapping("/manage")
	public ModelAndView manage() throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/admin/page/link/link_manage");
		return mav;
	}
	
	/**
	 * /houtai/link/add
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/add")
	public ModelAndView add() throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.addObject("btn_text", "添加");
		mav.addObject("save_url", "/admin/link/add");
		mav.setViewName("/admin/page/link/add_update");
		return mav;
	}
	
	
	/**
	 * /houtai/link/edit?id=1
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam(value = "id", required = false) Integer id) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		Link link = linkDao.findId(id);
		mav.addObject("link", link);
		mav.addObject("btn_text", "修改");
		mav.addObject("save_url", "/admin/link/update?id=" + id);
		mav.setViewName("/admin/page/link/add_update");
		return mav;
	}
	
	
	
	
	
}
