package flextrade.flexvision.fx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Import(value = {DatabaseConfig.class, SchedulerConfig.class})
@EnableTransactionManagement
@Slf4j
public class AppConfig {
}
