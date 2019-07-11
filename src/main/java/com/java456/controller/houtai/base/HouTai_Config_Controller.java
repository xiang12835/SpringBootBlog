package com.java456.controller.houtai.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.java456.entity.blog.BlogType;

@Controller
@RequestMapping("/houtai/config")
public class HouTai_Config_Controller {
	
	
	/**
	 * /houtai/config/manage
	 */
	@RequestMapping("/manage")
	public ModelAndView manage() throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/admin/page/config/config_manage");
		return mav;
	}
	
	
	
}
