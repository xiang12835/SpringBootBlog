package com.java456.controller.admin.blog;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.java456.dao.blog.LinkDao;
import com.java456.entity.blog.BlogType;
import com.java456.entity.blog.Link;
import com.java456.service.blog.LinkService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/admin/link")
public class Admin_Link_Controller {
	

	@Resource
	private LinkDao linkDao;
	@Resource
	private LinkService linkService;
	@Autowired 
	private ServletContext servletContext;
	
	
	/**
	 * /admin/link/add
	 */
	@ResponseBody
	@RequestMapping("/add")
	public JSONObject add(@Valid Link link    ,BindingResult bindingResult, HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject result = new JSONObject();
		
		if(bindingResult.hasErrors()){
			result.put("success", false);
			result.put("msg", bindingResult.getFieldError().getDefaultMessage());
			return result;
		}else{
			link.setCreateDateTime(new Date());
			linkDao.save(link);
			result.put("success", true);
			result.put("msg", "添加成功");
			
			//刷新linkList缓存
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("useIt", 1);
			List<Link> linkList = linkService.list(map , 0, 101);
			servletContext.setAttribute("linkList", linkList);
			//刷新linkList缓存\
			
			
			return result;
		}
	}
	
	/**
	 * /admin/link/update
	 */
	@ResponseBody
	@RequestMapping("/update")
	public JSONObject update(Link link )throws Exception {
		JSONObject result = new JSONObject();
		link.setUpdateDateTime(new Date());
		linkService.update(link);
		result.put("success", true);
		result.put("msg", "修改成功");
		
		//刷新linkList缓存
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("useIt", 1);
		List<Link> linkList = linkService.list(map , 0, 101);
		servletContext.setAttribute("linkList", linkList);
		//刷新linkList缓存\
		
		
		return result;
	}
	
	
	/**
	 * /admin/link/list
	 * @param page    默认1
	 * @param limit   数据多少
	 */
	@ResponseBody
	@RequestMapping("/list")
	public Map<String, Object> list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "limit", required = false) Integer limit, 
			HttpServletResponse response,
			HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Link> list = linkService.list(map, page-1, limit);
		long total = linkService.getTotal(map);
		map.put("data", list);
		map.put("count", total);
		map.put("code", 0);
		map.put("msg", "");
		return map;
	}
	
	/**
	 * /admin/link/delete
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public JSONObject delete(@RequestParam(value = "ids", required = false) String ids, HttpServletResponse response)
			throws Exception {
		String[] idsStr = ids.split(",");
		JSONObject result = new JSONObject();
		for (int i = 0; i < idsStr.length; i++) {
			linkDao.deleteById(Integer.parseInt(idsStr[i]));
		}
		
		//刷新linkList缓存
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("useIt", 1);
		List<Link> linkList = linkService.list(map , 0, 101);
		servletContext.setAttribute("linkList", linkList);
		//刷新linkList缓存\
		
		result.put("success", true);
		return result;
	}
	
	
	
	
	
	
	
	
}
