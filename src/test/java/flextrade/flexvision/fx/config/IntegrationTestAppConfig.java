package flextrade.flexvision.fx.config;

import com.icegreen.greenmail.spring.GreenMailBean;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class IntegrationTestAppConfig {

    @Value("${spring.mail.host: localhost}")
    private String smtpHost;

    @Value("${spring.mail.port: 25}")
    private int smtpPort;

    @Value("${spring.mail.default-encoding: UTF-8}")
    private String defaultEmailEncoding;


    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations("classpath:db/migration");

        return flyway;
    }

    @Bean
    public GreenMailBean greenMail() {
        GreenMailBean greenMail = new GreenMailBean();

        return greenMail;
    }
}
