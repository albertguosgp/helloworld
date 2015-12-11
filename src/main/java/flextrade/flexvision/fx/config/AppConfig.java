package flextrade.flexvision.fx.config;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;
import org.togglz.core.manager.TogglzConfig;

import flextrade.flexvision.fx.base.feature.FeatureService;
import flextrade.flexvision.fx.base.feature.impl.CachedFeatureServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;

@Configuration
@Import(value = {DatabaseConfig.class})
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
     * FeatureManager should never be called by application logic to determine if a feature is
     * active. Instead please use {@link flextrade.flexvision.fx.base.feature.FeatureService}
     */
    @Bean
    public FeatureManager createFeatureManager() {
        return new FeatureManagerBuilder().togglzConfig(createFeatureConfig()).build();
    }

    @Bean
    @ConditionalOnMissingBean
    public FeatureService createFeatureService() {
        return new CachedFeatureServiceImpl();
    }

	@Bean
	public FormattingConversionService conversionService() {
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);
		conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());
		// Register date conversion with a specific global format
		DateFormatterRegistrar dateFormatterRegistrar = new DateFormatterRegistrar();
		DateFormatter dateFormatter = new DateFormatter();
		dateFormatter.setIso(DateTimeFormat.ISO.DATE_TIME);
		dateFormatterRegistrar.setFormatter(dateFormatter);
		dateFormatterRegistrar.registerFormatters(conversionService);

		return conversionService;
	}
}
