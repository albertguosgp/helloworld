package flextrade.flexvision.fx.config;

import flextrade.flexvision.fx.base.feature.FeatureService;
import flextrade.flexvision.fx.base.feature.impl.CachedFeatureServiceImpl;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;
import org.togglz.core.manager.TogglzConfig;

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

	@Bean
	public RestTemplate createRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	public TogglzConfig createFeatureConfig() {
		return new FeaturesConfig();
	}

	/**
	 * FeatureManager should never be called by application logic to determine if a feature is active.
	 * Instead please use {@link flextrade.flexvision.fx.base.feature.FeatureService}
	 * */
	@Bean
	public FeatureManager createFeatureManager() {
		return new FeatureManagerBuilder().togglzConfig(createFeatureConfig()).build();
	}

	@Bean
	@ConditionalOnMissingBean
	public FeatureService createFeatureService() {
		return new CachedFeatureServiceImpl();
	}
}
