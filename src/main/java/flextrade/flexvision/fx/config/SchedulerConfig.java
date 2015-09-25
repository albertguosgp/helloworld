package flextrade.flexvision.fx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class SchedulerConfig {

	@Bean
	public SchedulerFactoryBean createScheduler() {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

		return schedulerFactoryBean;
	}

}
