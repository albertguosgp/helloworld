package flextrade.flexvision.fx.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;

@Configuration
@Import(value = { DatabaseConfig.class, SchedulerConfig.class})
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

	/**
	 * FeatureManager should never be called by application logic to determine if a feature is active.
	 * Instead please use {@link flextrade.flexvision.fx.base.feature.FeatureService}
	 * */
	@Bean
	public FeatureManager createFeatureManager() {
		return new FeatureManagerBuilder().togglzConfig(new FeaturesConfig()).build();
	}
}
