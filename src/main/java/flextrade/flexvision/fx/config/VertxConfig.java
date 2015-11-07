package flextrade.flexvision.fx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

@Component
public class VertxConfig {
    @Bean
    public EventBus createEventBus() {
        return createVertx().eventBus();
    }

    @Bean
    public Vertx createVertx() {
        return Vertx.vertx();
    }
}
