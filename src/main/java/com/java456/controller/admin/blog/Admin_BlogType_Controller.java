package com.java456.controller.admin.blog;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.java456.dao.blog.BlogTypeDao;
import com.java456.entity.base.User;
import com.java456.entity.blog.BlogType;
import com.java456.service.blog.BlogTypeService;
import com.java456.util.CryptographyUtil;
import com.java456.util.StringUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/admin/blog/type")
public class Admin_BlogType_Controller {
	
	@Resource
	private BlogTypeDao blogTypeDao;
	@Resource
	private BlogTypeService blogTypeService;
	
	
	/**
	 * /admin/blog/type/add
	 */
	@ResponseBody
	@RequestMapping("/add")
	public JSONObject add(@Valid BlogType blogType  ,BindingResult bindingResult, HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject result = new JSONObject();
		
		if(bindingResult.hasErrors()){
			result.put("success", false);
			result.put("msg", bindingResult.getFieldError().getDefaultMessage());
			return result;
		}else{
			blogType.setCreateDateTime(new Date());
			blogType.setCount(0L);
			blogTypeDao.save(blogType);
			result.put("success", true);
			result.put("msg", "添加成功");
			return result;
		}
	}
	
	/**
	 * /admin/blog/type/update
	 */
	@ResponseBody
	@RequestMapping("/update")
	public JSONObject update(BlogType blogType)throws Exception {
		JSONObject result = new JSONObject();
		blogType.setUpdateDateTime(new Date());
		blogTypeService.update(blogType);
		result.put("success", true);
		result.put("msg", "修改成功");
		return result;
	}
	
	
	/**
	 * /admin/blog/type/list
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
		List<BlogType> list = blogTypeService.list(map, page-1, limit);
		long total = blogTypeService.getTotal(map);
		map.put("data", list);
		map.put("count", total);
		map.put("code", 0);
		map.put("msg", "");
		return map;
	}
	
	/**
	 * /admin/blog/type/delete
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public JSONObject delete(@RequestParam(value = "ids", required = false) String ids, HttpServletResponse response)
			throws Exception {
		String[] idsStr = ids.split(",");
		JSONObject result = new JSONObject();
		for (int i = 0; i < idsStr.length; i++) {
			blogTypeDao.deleteById(Integer.parseInt(idsStr[i]));
		}
		result.put("success", true);
		return result;
	}
	
	
	
	
	
}
