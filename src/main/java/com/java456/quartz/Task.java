package com.java456.quartz;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Configurable
@EnableScheduling
public class Task  {
	
	/**
	 *  每秒  输出一次
	 */
	@Scheduled(cron="0/3 * * * * ?")
	public void test() {
		//System.out.println("我是定时器，");
	}
	
	
}
