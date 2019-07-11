package com.java456.controller.admin.base;


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

import com.java456.entity.base.Menu;
import com.java456.service.base.MenuService;
import com.java456.util.StringUtil;

import net.sf.json.JSONObject;


@Controller
@RequestMapping("/admin/menu")
public class Admin_Menu_Controller {
	
	@Resource
	private MenuService  menuService;
	
	/**
	 * /admin/menu/add
	 */
	@ResponseBody
	@RequestMapping("/add")
	public JSONObject add(@Valid Menu menu ,BindingResult bindingResult, HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject result = new JSONObject();
		
		if(bindingResult.hasErrors()){
			result.put("success", false);
			result.put("msg", bindingResult.getFieldError().getDefaultMessage());
			return result;
		}else{
			menuService.add(menu);
			result.put("success", true);
			result.put("msg", "添加成功");
			return result;
		}
	}
	
	/**
	 * /admin/menu/update
	 */
	@ResponseBody
	@RequestMapping("/update")
	public JSONObject update( Menu menu )throws Exception {
		JSONObject result = new JSONObject();
		menuService.update(menu);
		result.put("success", true);
		result.put("msg", "修改成功");
		return result;
	}
	
	/**
	 * /admin/menu/list
	 * @param page    默认1
	 * @param limit   数据多少
	 * @param pId   父节点  id
	 */
	@ResponseBody
	@RequestMapping("/list")
	public Map<String, Object> list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "limit", required = false) Integer limit, 
			@RequestParam(value = "pId", required = false) String pId, 
			HttpServletResponse response,
			HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(StringUtil.isNotEmpty(pId))
			map.put("pId", pId);
		
		List<Menu> list = menuService.list(map, page-1, limit);
		long total = menuService.getTotal(map);
		map.clear();
		map.put("data", list);
		map.put("count", total);
		map.put("code", 0);
		map.put("msg", "");
		return map;
	}
	
	
	/**
	 * /admin/menu/delete
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public JSONObject delete(@RequestParam(value = "ids", required = false) String ids, HttpServletResponse response)
			throws Exception {
		String[] idsStr = ids.split(",");
		JSONObject result = new JSONObject();
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < idsStr.length; i++) {
			try {
				map.put("pId", Integer.parseInt(idsStr[i]));
				//看看下面有没有菜单   一同删除
				List<Menu> menuList = menuService.list(map, 0, 100);
				for(Menu menu:menuList) {
					menuService.delete(menu.getId());
				}
				//看看下面有没有菜单   一同删除
				
				menuService.delete(Integer.parseInt(idsStr[i]));
			} catch (Exception e) {
				e.printStackTrace();
				result.put("success", false);
				result.put("msg", "有角色正在使用些菜单");
				return result;
			}
			
		}
		
		result.put("success", true);
		return result;
	}
	
	
	
	
}
