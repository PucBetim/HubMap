package br.com.pucminas.hubmap.rest.configuration;

import static br.com.pucminas.hubmap.utils.LoggerUtils.getLoggerFromClass;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration {

	public Executor taskExecutor() {
		getLoggerFromClass(AsyncConfiguration.class).debug("Creating Async Task Executor");
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("HubMapCustomTh-");
        executor.initialize();
        return executor;
	}
	
}
