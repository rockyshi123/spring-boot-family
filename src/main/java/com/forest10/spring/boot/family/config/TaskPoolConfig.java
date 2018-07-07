package com.forest10.spring.boot.family.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Forest10
 * @date 2018/7/7 下午7:16
 */
@Configuration
public class TaskPoolConfig {

	@Bean("taskExecutor")
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor
				executor = new ThreadPoolTaskExecutor
				();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(100);
		executor.setKeepAliveSeconds(60);
		executor.setThreadNamePrefix("taskExecutor-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		//防止任务未执行完毕就关闭线程池
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setAwaitTerminationSeconds(60);
		return executor;
	}

}
