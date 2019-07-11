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
import com.java456.dao.blog.BlogDao;
import com.java456.dao.blog.BlogTypeDao;
import com.java456.entity.base.User;
import com.java456.entity.blog.Blog;
import com.java456.entity.blog.BlogType;
import com.java456.lucene.BlogIndex;
import com.java456.service.blog.BlogService;
import com.java456.service.blog.BlogTypeService;
import com.java456.util.Base64Util;
import com.java456.util.DateUtil;
import com.java456.util.FileUtil;
import com.java456.util.StringUtil;
import net.sf.json.JSONObject;



@Controller
@RequestMapping("/admin/blog")
public class Admin_Blog_Controller {
	
	@Resource
	private BlogDao blogDao;
	@Resource
	private BlogTypeDao  blogTypeDao;
	@Resource
	private  BlogService blogService;
	@Resource
	private  BlogTypeService blogTypeService;
	private BlogIndex blogIndex=new BlogIndex();
	
	
	
	/**
	 * /admin/blog/add
	 */
	@ResponseBody
	@RequestMapping("/add")
	public JSONObject add(@Valid Blog blog ,BindingResult bindingResult
			, HttpServletResponse response, HttpServletRequest request,HttpSession session) throws Exception {
		JSONObject result = new JSONObject();
		if(bindingResult.hasErrors()){
			result.put("success", false);
			result.put("msg", bindingResult.getFieldError().getDefaultMessage());
			return result;
		}else{
			blog.setCreateDateTime(new Date());
			blog.setUseIt(0);
			blog.setUser((User)session.getAttribute("currentUser"));
			blog.setClickHit(0);
			blog.setReplyHit(0);
			blogDao.save(blog);
			
			
			//更新 类型的数量
			BlogType blogType = blogTypeDao.findId(blog.getBlogType().getId());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("blogType", blogType);
			blogType.setCount(blogService.getTotal(map));
			blogTypeDao.save(blogType);
			//更新 类型的数量
			
			//添加lucene索引
			blogIndex.addIndex(blog);
			//添加lucene索引
			
			
			result.put("success", true);
			result.put("msg", "添加成功");
			result.put("btn_disable", true);
			return result;
		}
	}
	
	/**
	 * /admin/blog/update
	 */
	@ResponseBody
	@RequestMapping("/update")
	public JSONObject update(Blog blog, HttpServletResponse response, HttpServletRequest request)throws Exception {
		JSONObject result = new JSONObject();
		String webPath=request.getServletContext().getRealPath("");
		blog.setUpdateDateTime(new Date());
		blogService.update(blog,webPath);
		
		
		//更新 类型的数量
		BlogType blogType = blogTypeDao.findId(blog.getBlogType().getId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("blogType", blogType);
		blogType.setCount(blogService.getTotal(map));
		blogTypeDao.save(blogType);
		//更新 类型的数量
		
		
		//更新lucene索引
		blogIndex.updateIndex(blog);
		//更新lucene索引
		
		
		result.put("success", true);
		result.put("msg", "修改成功");
		return result;
	}
	
	
	/**
	 * /admin/blog/list  layuitable
	 * @param page    默认1
	 * @param limit   数据多少
	 */
	@ResponseBody
	@RequestMapping("/list")
	public Map<String, Object> list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "state", required = false) Integer state,
			@RequestParam(value = "useIt", required = false) Integer useIt,
			@RequestParam(value = "blogTypeId", required = false) Integer blogTypeId,
			@RequestParam(value = "q", required = false) String q,
			HttpServletResponse response,
			HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(state!=null)
			map.put("state", state);
		if(useIt!=null)
			map.put("useIt", useIt);
		if(blogTypeId!=null) {
			BlogType blogType = new BlogType();
			blogType.setId(blogTypeId);
			map.put("blogType", blogType);
		}
		if(StringUtil.isNotEmpty(q))
			map.put("q", q);
		
		
		List<Blog> list = blogService.list(map, page-1, limit);
		Long total = blogService.getTotal(map);
		map.clear();
		map.put("data", list);
		map.put("count", total);
		map.put("code", 0);
		map.put("msg", "");
		return map;
	}
	
	
	/**
	 * /admin/blog/delete
	 * 
	 * 15     15,55,66
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public JSONObject delete(@RequestParam(value = "ids", required = false) String ids, HttpServletRequest request)
			throws Exception {
		String[] idsStr = ids.split(",");
		String webPath=request.getServletContext().getRealPath("");
		JSONObject result = new JSONObject();
		for (int i = 0; i < idsStr.length; i++) {
			blogService.delete(Integer.parseInt(idsStr[i]), webPath);
			
			//删除lucene索引
			blogIndex.deleteIndex(idsStr[i]);
			//删除lucene索引
		}
		
		
		//更新所有类型
		Map<String, Object> map = new HashMap<String, Object>();
		List<BlogType> blogTypeList=blogTypeService.list(map, 0, 500);
		for(BlogType blogType :blogTypeList) {
			//更新 类型的数量
			map.put("blogType", blogType);
			blogType.setCount(blogService.getTotal(map));
			blogTypeDao.save(blogType);
			//更新 类型的数量
		}
		//更新所有类型
		
		result.put("success", true);
		return result;
	}
	
	
	/**
	 * 
	 * /admin/blog/add_imageUrl
	 */
	@ResponseBody
	@RequestMapping("/add_imageUrl")
	public JSONObject add_imageUrl(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) throws Exception {
	        JSONObject result = new JSONObject();
	        if(!file.isEmpty()){
	            String webPath=request.getServletContext().getRealPath("");
	            System.out.println(webPath);
	            String filePath= "/static/upload_image/blog_cover/"+DateUtil.formatDate(new Date(), "yyyyMMdd")+"/";
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
	 * /admin/blog/add_cropper_image
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
			String  uploadFile = "/static/upload_image/blog_cover/"+DateUtil.formatDate(new Date(), "yyyyMMdd")+"/";
			String fileName = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss")+".jpg";
			
			//调用产生文件夹的方法
			FileUtil.makeDirs(webPath+uploadFile);
			
			imgData = imgData.replace("data:image/png;base64,", "");
			
			Base64Util.GenerateImage(imgData, webPath+uploadFile+fileName);
			 result.put("success", true);
	         result.put("path", uploadFile+fileName);
	        return result;
	}
	
	/**
	 * /admin/blog/findById
	 * 通过Id查找实体
	 * @param id
	 */
	@ResponseBody	
	@RequestMapping("/findById")
	public Blog findById(@RequestParam(value="id")Integer id,HttpServletResponse response)throws Exception{
		Blog blog  =blogDao.findId(id);
		return blog;
	}
	
	/**
	 * #重新索引    
	 * 1#删除   
	 * 2#再添加索引
	 */
	@ResponseBody
	@RequestMapping("/updateLucene")
	public JSONObject updateLucene(@RequestParam(value = "id", required = false) Integer id, HttpServletRequest request)
			throws Exception {
		JSONObject result = new JSONObject();
		
		//删除lucene索引
		blogIndex.deleteIndex(id.toString());
		//删除lucene索引
		
		//重新索引
		Blog blog = blogDao.findId(id);
		blogIndex.addIndex(blog);
		//重新索引
		
		result.put("success", true);
		result.put("msg", "重新索引,成功");
		return result;
	}
	
	
	/**
	 * 
	 * 1#删除   索引
	 */
	@ResponseBody
	@RequestMapping("/deleteLucene")
	public JSONObject deleteLucene(@RequestParam(value = "id", required = false) Integer id, HttpServletRequest request)
			throws Exception {
		JSONObject result = new JSONObject();
		
		//删除lucene索引
		blogIndex.deleteIndex(id.toString());
		//删除lucene索引
		
		result.put("success", true);
		result.put("msg", "删除索引,成功");
		return result;
	}
	
	
	
	
	
}
