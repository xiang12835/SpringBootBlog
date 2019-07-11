package com.java456.controller.pc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.java456.dao.blog.BlogDao;
import com.java456.dao.blog.BlogTypeDao;
import com.java456.entity.base.Config;
import com.java456.entity.blog.Blog;
import com.java456.entity.blog.BlogType;
import com.java456.lucene.BlogIndex;
import com.java456.service.blog.BlogService;
import com.java456.service.blog.BlogTypeService;
import com.java456.util.DateUtil;
import net.sf.json.JSONObject;

@Controller
public class Blog_Controller {
	
	
	@Resource
	private BlogTypeDao  blogTypeDao;
	@Resource
	private BlogDao  blogDao;
	@Resource
	private BlogService  blogService;
	@Resource
	private BlogTypeService  blogTypeService;
	@Autowired 
	private ServletContext servletContext;
	private BlogIndex blogIndex = new BlogIndex();
	
	/**
	 *  /blog/1
	 * @return springmvc会自动把这个id提出来 /a/shop/1   会自动 提出1当id
	 * @throws Exception
	 */
	@RequestMapping("/blog/{id}")
	public ModelAndView blog(@PathVariable("id") Integer id,HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView();
		Blog blog    = blogDao.findId(id);
		mav.addObject("blog", blog);
		
		//添加点击次数
		blog.setClickHit(blog.getClickHit()+1);
		blogDao.save(blog);
		//添加点击次数
		
		//日期对应  文章数量
		List<JSONObject> dateList = blogDao.group();
		//日期对应  文章数量
		
		//拿分类列表
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("useIt", 1);
		List<BlogType> blogTypeList = blogTypeService.list(map, 0, 500);
		//拿分类列表
		
		mav.addObject("dateList", dateList);
		mav.addObject("blogTypeList", blogTypeList);
		
		mav.setViewName("/pc/blog/view");
		return mav;
	}
	
	/**
	 *  /all?page=1
	 * @return page默认1
	 * @throws Exception
	 */
	@RequestMapping("/all")
	public ModelAndView all(@RequestParam(value = "page", required = false) Integer page) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		if(page==null) {
			page =1;
		}
		
		Config config = (Config) servletContext.getAttribute("config");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("state", 1);
		List<Blog> blogList = blogService.list(map, page-1, config.getAllpageSize());
		mav.addObject("blogList", blogList);
		Long total = blogService.getTotal(map);
		mav.addObject("total", total);
		mav.addObject("page", page);
		
		//日期对应  文章数量
		List<JSONObject> dateList = blogDao.group();
		mav.addObject("dateList", dateList);
		//日期对应  文章数量
		
		//拿分类列表
		map.clear();
		map.put("useIt", 1);
		List<BlogType> blogTypeList = blogTypeService.list(map, 0, 500);
		mav.addObject("blogTypeList", blogTypeList);
		//拿分类列表
		
		mav.addObject("title", "全部文章");
		
		mav.setViewName("/pc/blog/all");
		return mav;
	}
	
	
	/**
	 *  /type?id=1&page=1
	 * @return page默认1
	 * @throws Exception
	 */
	@RequestMapping("/type")
	public ModelAndView type(@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "page", required = false) Integer page
			) throws Exception {
		ModelAndView mav = new ModelAndView();
		BlogType blogType      = blogTypeDao.findId(id);
		mav.addObject("blogType", blogType);
		
		if(page==null) {
			page =1;
		}
		
		Config config = (Config) servletContext.getAttribute("config");
		System.out.println(config.getAllpageSize());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("blogType", blogType);
		map.put("state", 1);
		List<Blog> blogList = blogService.list(map, page-1, config.getAllpageSize());
		mav.addObject("blogList", blogList);
		Long total = blogService.getTotal(map);
		mav.addObject("total", total);
		mav.addObject("page", page);
		
		
		//日期对应  文章数量
		List<JSONObject> dateList = blogDao.group();
		mav.addObject("dateList", dateList);
		//日期对应  文章数量
		
		//拿分类列表
		map.clear();
		map.put("useIt", 1);
		List<BlogType> blogTypeList = blogTypeService.list(map, 0, 500);
		mav.addObject("blogTypeList", blogTypeList);
		//拿分类列表
		
		mav.setViewName("/pc/blog/type");
		return mav;
	}
	
	/**
	 *  /search?q=11&page=1
	 * @return page默认1
	 * @throws Exception
	 */
	//@RequestMapping("/search")
	public ModelAndView search(@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "page", required = false) Integer page) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		if(page==null) {
			page =1;
		}
		
		Config config = (Config) servletContext.getAttribute("config");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("state", 1);
		map.put("q", q);
		List<Blog> blogList = blogService.list(map, page-1, config.getAllpageSize());
		mav.addObject("blogList", blogList);
		Long total = blogService.getTotal(map);
		mav.addObject("total", total);
		mav.addObject("page", page);
		mav.addObject("q", q);
		mav.addObject("title", "搜索："+q);
		
		//日期对应  文章数量
		List<JSONObject> dateList = blogDao.group();
		mav.addObject("dateList", dateList);
		//日期对应  文章数量
		
		//拿分类列表
		map.clear();
		map.put("useIt", 1);
		List<BlogType> blogTypeList = blogTypeService.list(map, 0, 500);
		mav.addObject("blogTypeList", blogTypeList);
		//拿分类列表
		
		mav.setViewName("/pc/blog/search");
		return mav;
	}
	
	
	/**
	 *  /search?q=java what
	 * @throws Exception
	 */
	@RequestMapping("/search")
	public ModelAndView luceneSearch(@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "page", required = false) Integer page) throws Exception {
		ModelAndView mav = new ModelAndView();
		List<Blog> blogList = blogIndex.searchBlog(q);
		System.out.println(blogList.size());
		for(Blog blog : blogList) {
			Blog tempBlog = blogDao.findId(blog.getId());
			blog.setBlogType(tempBlog.getBlogType());
			blog.setUser(tempBlog.getUser());
			blog.setClickHit(tempBlog.getClickHit());
			blog.setCreateDateTime(tempBlog.getCreateDateTime());
		}
		
		mav.addObject("blogList", blogList);
		mav.addObject("q", q);
		mav.addObject("title", "搜索："+q);
		
		Map<String, Object> map = new HashMap<String, Object>();
		//日期对应  文章数量
		List<JSONObject> dateList = blogDao.group();
		mav.addObject("dateList", dateList);
		//日期对应  文章数量
		
		//拿分类列表
		map.clear();
		map.put("useIt", 1);
		List<BlogType> blogTypeList = blogTypeService.list(map, 0, 500);
		mav.addObject("blogTypeList", blogTypeList);
		//拿分类列表
		
		mav.setViewName("/pc/blog/luceneSearch");
		return mav;
	}
	
	
	
	
	
	/**
	 *  /date?date=2019-01-05&page=1
	 * @return page默认1
	 * @throws Exception
	 */
	@RequestMapping("/date")
	public ModelAndView date(@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "page", required = false) Integer page) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		if(page==null) {
			page =1;
		}
		mav.addObject("date", date);
		
		
		//把日期换成2019年05月    2019-05
		date = date.replaceAll("年", "-");
		date = date.replaceAll("月", "-");
		date = date.substring(0, date.length()-1);
		
		String date1= date+"-01";
		String date2= DateUtil.getDays(date);
		
		System.out.println(date1);
		System.out.println(date2);
		
		
		Config config = (Config) servletContext.getAttribute("config");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("state", 1);
		map.put("date1", date1 +" 00:00");
		map.put("date2", date2 +" 23:59");
		List<Blog> blogList = blogService.list(map, page-1, config.getAllpageSize());
		mav.addObject("blogList", blogList);
		Long total = blogService.getTotal(map);
		mav.addObject("total", total);
		mav.addObject("page", page);
		
		//日期对应  文章数量
		List<JSONObject> dateList = blogDao.group();
		mav.addObject("dateList", dateList);
		//日期对应  文章数量
		
		//拿分类列表
		map.clear();
		map.put("useIt", 1);
		List<BlogType> blogTypeList = blogTypeService.list(map, 0, 500);
		mav.addObject("blogTypeList", blogTypeList);
		//拿分类列表
		
		mav.setViewName("/pc/blog/date");
		return mav;
	}
	
	
	
}
