package com.java456.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.java456.entity.base.Config;
import com.java456.entity.blog.Link;
import com.java456.lucene.BlogIndex;
import com.java456.properties.Proper;
import com.java456.service.base.ConfigService;
import com.java456.service.blog.LinkService;

/**
 * #初始化加载数据
 * @author Administrator
 *
 */
@Component
public class InitSystem implements ServletContextListener,ApplicationContextAware{
	
	private static ApplicationContext applicationContext;
	
	@Resource
	Proper proper;
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext=applicationContext;
	}
	
	/**
	 * 加载数据到application缓存中
	 */
	public void loadData(ServletContext application){
		ConfigService configService=(ConfigService) applicationContext.getBean("configService");
		Config config = configService.findById(1);
		application.setAttribute("config", config);
		
		
		
		//初始化 友情 链接
		LinkService linkService=(LinkService) applicationContext.getBean("linkService");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("useIt", 1);
		List<Link> linkList = linkService.list(map , 0, 200);
		application.setAttribute("linkList", linkList);
		//初始化 友情 链接
		
		BlogIndex.path=proper.getPath();
		
	}
	
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		loadData(sce.getServletContext());
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	
}
