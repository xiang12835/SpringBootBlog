package com.java456.controller.admin.base;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.java456.dao.base.UserDao;
import com.java456.entity.base.User;
import com.java456.service.base.UserService;
import com.java456.util.Base64Util;
import com.java456.util.CryptographyUtil;
import com.java456.util.DateUtil;
import com.java456.util.FileUtil;
import com.java456.util.StringUtil;
import net.sf.json.JSONObject;


@Controller
@RequestMapping("/admin/user")
public class Admin_User_Controller {
	@Resource
	private UserDao userDao;
	@Resource
	private UserService userService;
	
	/**
	 * /admin/user/add
	 */
	@ResponseBody
	@RequestMapping("/add")
	public JSONObject add(@Valid User user  ,BindingResult bindingResult, HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject result = new JSONObject();
		user.setCreateDateTime(new Date());
		
		if(bindingResult.hasErrors()){
			result.put("success", false);
			result.put("msg", bindingResult.getFieldError().getDefaultMessage());
			return result;
		}else{
			user.setCreateDateTime(new Date());
			user.setPwd(CryptographyUtil.md5(user.getPwd(), "java"));
			userDao.save(user);
			result.put("success", true);
			result.put("msg", "添加成功");
			return result;
		}
	}
	
	/**
	 * /admin/user/update
	 */
	@ResponseBody
	@RequestMapping("/update")
	public JSONObject update(@Valid User user ,BindingResult bindingResult, HttpServletRequest request)throws Exception {
		JSONObject result = new JSONObject();
		
		if(bindingResult.hasErrors()){
			result.put("success", false);
			result.put("msg", bindingResult.getFieldError().getDefaultMessage());
			return result;
		}else{
			
			String webPath=request.getServletContext().getRealPath("");
			user.setUpdateDateTime(new Date());
			userService.update(user,webPath);
			result.put("success", true);
			result.put("msg", "修改成功");
			return result;
		}
	}
	
	
	/**
	 * /admin/user/set_new_pwd
	 */
	@ResponseBody
	@RequestMapping("/set_new_pwd")
	public JSONObject set_new_pwd(User user, HttpServletRequest request)throws Exception {
		JSONObject result = new JSONObject();
	
		user.setUpdateDateTime(new Date());
		if(StringUtil.isNotEmpty(user.getPwd())){
			user.setPwd(CryptographyUtil.md5(user.getPwd(),"java"));
		}
		String webPath=request.getServletContext().getRealPath("");
		
		userService.update(user,webPath);
		result.put("success", true);
		result.put("msg", "修改成功");
		return result;
		
	}
	
	
	
	
	/**
	 * /admin/user/list
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
		
		List<User> userList = userService.list(map, page-1, limit);
		long total = userService.getTotal(map);
		map.put("data", userList);
		map.put("count", total);
		map.put("code", 0);
		map.put("msg", "");
		return map;
	}
	
	/**
	 * /admin/user/delete
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public JSONObject delete(@RequestParam(value = "ids", required = false) String ids, HttpServletResponse response)
			throws Exception {
		String[] idsStr = ids.split(",");
		JSONObject result = new JSONObject();

		for (int i = 0; i < idsStr.length; i++) {
			userDao.deleteById(Integer.parseInt(idsStr[i]));
		}
		result.put("success", true);
		return result;
	}
	
	
	/**
	 * 
	 * /admin/user/add_imageUrl
	 */
	@ResponseBody
	@RequestMapping("/add_imageUrl")
	public JSONObject add_imageUrl(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
	        JSONObject result = new JSONObject();
	        if(!file.isEmpty()){
	            String webPath=request.getServletContext().getRealPath("");
	            String filePath= "/static/upload_image/user_cover/"+DateUtil.formatDate(new Date(), "yyyyMMdd")+"/";
	            //把文件名子换成（时间搓.png）
	            // String imageName="houtai_logo."+file.getOriginalFilename().split("\\.")[1];
	            //检测   文件夹有没有创建 
	            FileUtil.makeDirs(webPath+filePath);
	            
	            String imageName=DateUtil.formatDate(new Date(), "yyyyMMddHHmmss")+".jpg";
	            file.transferTo(new File(webPath+filePath+imageName));
	            result.put("success", true);
	            result.put("path", filePath+imageName);
	        }
	        
	        return result;
	}
	
	
	/**
	 * 
	 * /admin/user/add_cropper_image
	 * imgData=  data:image/png;base64,iVBORw0KGgoAAAiVBORw0KGgoAAAiVBORw0KGgoAAA后面非常多的字符，就是imgData
	 * 
	 */
	@ResponseBody
	@RequestMapping("/add_cropper_image")
	public JSONObject add_cropper_image(@RequestParam(value="imgData",required=false)String imgData, HttpServletResponse response, HttpServletRequest request) throws Exception {
	        JSONObject result = new JSONObject();
	        
	        //取得根目录带d://dxxxxx/tomcat/ruzhou/
			String webPath=request.getServletContext().getRealPath("");
			//定义根目录下面的文件夹
			String  uploadFile = "/static/upload_image/user_cover/"+DateUtil.formatDate(new Date(), "yyyyMMdd")+"/";
			String fileName = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss")+".jpg";
			
			//调用产生文件夹的方法
			FileUtil.makeDirs(webPath+uploadFile);
			
			imgData = imgData.replace("data:image/png;base64,", "");
			
			Base64Util.GenerateImage(imgData, webPath+uploadFile+fileName);
			 result.put("success", true);
	         result.put("path", uploadFile+fileName);
	        return result;
	}
	
	
	
	
}
