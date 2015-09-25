package flextrade.flexvision.fx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Import(value = { DatabaseConfig.class, SchedulerConfig.class, ServletConfig.class })
@EnableTransactionManagement
@Slf4j
public class AppConfig {
	@Bean
	public AsyncTaskExecutor createAsyncTaskExecutor() {
		ThreadPoolTaskExecutor asyncTaskExecutor = new ThreadPoolTaskExecutor();
		asyncTaskExecutor.setDaemon(true);
		asyncTaskExecutor.setCorePoolSize(25);
		asyncTaskExecutor.setMaxPoolSize(50);

		return asyncTaskExecutor;
	}
}
