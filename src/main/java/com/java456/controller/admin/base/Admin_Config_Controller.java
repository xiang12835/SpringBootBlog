package com.java456.controller.admin.base;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.java456.entity.base.Config;
import com.java456.entity.blog.BlogType;
import com.java456.service.base.ConfigService;
import com.java456.service.blog.BlogTypeService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/admin/config")
public class Admin_Config_Controller {
	
	@Resource
	private    ConfigService configService;
	@Autowired 
	private ServletContext servletContext;
	
	/**
	 * /admin/config/update
	 */
	@ResponseBody
	@RequestMapping("/update")
	public JSONObject update(Config config  )throws Exception {
		JSONObject result = new JSONObject();
		configService.update(config);
		
		//刷新缓存
		config = configService.findById(1);
		 servletContext.setAttribute("config", config);
		//刷新缓存
		result.put("success", true);
		result.put("msg", "修改成功");
		return result;
	}
	
}
