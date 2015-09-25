package flextrade.flexvision.fx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class SchedulerConfig {

	@Bean
	public AsyncTaskExecutor createAsyncTaskExecutor() {
		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor("async-task-executor");
		asyncTaskExecutor.setConcurrencyLimit(50);
		asyncTaskExecutor.setDaemon(true);

		return asyncTaskExecutor;
	}
}
