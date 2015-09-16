package flextrade.flexvision.fx.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class IntegrationTestAppConfig {

    /**
     * With profile integration-test, flyway will clean and migrate database.
     */
    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations("classpath:db/migration");
        flyway.clean();
        flyway.migrate();

        return flyway;
    }
}
