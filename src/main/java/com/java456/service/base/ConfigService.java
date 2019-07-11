package com.java456.service.base;

import com.java456.entity.base.Config;

public interface ConfigService {
	
	public Integer update(Config config);
	
	/**
	 * init 方法用这个
	 */
	public Config findById(Integer id);
	
	
}
