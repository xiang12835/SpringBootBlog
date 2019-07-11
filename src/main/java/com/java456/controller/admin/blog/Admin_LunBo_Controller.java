package com.java456.controller.admin.blog;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.java456.dao.blog.LunBoDao;
import com.java456.entity.blog.Blog;
import com.java456.entity.blog.LunBo;
import com.java456.service.blog.LunBoService;
import com.java456.util.Base64Util;
import com.java456.util.DateUtil;
import com.java456.util.FileUtil;

import net.sf.json.JSONObject;



@Controller
@RequestMapping("/admin/lunbo")
public class Admin_LunBo_Controller {
	

	@Resource
	private LunBoDao lunBoDao;
	@Resource
	private  LunBoService lunBoService;
	
	
	/**
	 * /admin/lunbo/add
	 */
	@ResponseBody
	@RequestMapping("/add")
	public JSONObject add(@Valid LunBo lunbo ,BindingResult bindingResult
			, HttpServletResponse response, HttpServletRequest request,HttpSession session) throws Exception {
		JSONObject result = new JSONObject();
		if(bindingResult.hasErrors()){
			result.put("success", false);
			result.put("msg", bindingResult.getFieldError().getDefaultMessage());
			return result;
		}else{
			lunbo.setCreateDateTime(new Date());
			lunbo.setUseIt(0);
			lunBoDao.save(lunbo);
			result.put("success", true);
			result.put("msg", "添加成功");
			result.put("btn_disable", true);
			return result;
		}
	}
	
	
	/**
	 * /admin/lunbo/update
	 */
	@ResponseBody
	@RequestMapping("/update")
	public JSONObject update( LunBo lunbo, HttpServletResponse response, HttpServletRequest request)throws Exception {
		JSONObject result = new JSONObject();
		String webPath=request.getServletContext().getRealPath("");
		
		lunbo.setUpdateDateTime(new Date());
		lunBoService.update(lunbo,webPath);
		result.put("success", true);
		result.put("msg", "修改成功");
		return result;
	}
	
	
	/**
	 * /admin/lunbo/list
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
		
		List<LunBo> list = lunBoService.list(map, page-1, limit);
		Long total = lunBoService.getTotal(map);
		map.clear();
		map.put("data", list);
		map.put("count", total);
		map.put("code", 0);
		map.put("msg", "");
		return map;
	}
	
	
	/**
	 * /admin/lunbo/delete
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public JSONObject delete(@RequestParam(value = "ids", required = false) String ids, HttpServletRequest request)
			throws Exception {
		String[] idsStr = ids.split(",");
		String webPath=request.getServletContext().getRealPath("");
		JSONObject result = new JSONObject();
		for (int i = 0; i < idsStr.length; i++) {
			lunBoService.delete(Integer.parseInt(idsStr[i]), webPath);
		}
		result.put("success", true);
		return result;
	}
	
	
	

	/**
	 * 
	 * /admin/lunbo/add_imageUrl
	 */
	@ResponseBody
	@RequestMapping("/add_imageUrl")
	public JSONObject add_imageUrl(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) throws Exception {
	        JSONObject result = new JSONObject();
	        if(!file.isEmpty()){
	            String webPath=request.getServletContext().getRealPath("");
	            System.out.println(webPath);
	            String filePath= "/static/upload_image/lunbo_cover/"+DateUtil.formatDate(new Date(), "yyyyMMdd")+"/";
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
	 * /admin/lunbo/add_cropper_image
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
			String  uploadFile = "/static/upload_image/lunbo_cover/"+DateUtil.formatDate(new Date(), "yyyyMMdd")+"/";
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
