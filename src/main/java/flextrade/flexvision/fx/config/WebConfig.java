package flextrade.flexvision.fx.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Value("${websocket.message.size.limit : 16000}")
    private int messageSizeLimit;

    @Value("${websocket.send.buffer.size : 160000}")
    private int sendBufferSizePerWebSocketSession;

    /**
     * web socket client message to /ws/stomp/topic will be forward to broker web socket client
     * message to /ws/stomp/app will be handled by spring controller
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Web socket client connect to web socket endpoint /ws/stomp
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/stomp");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(messageSizeLimit);
        registration.setSendBufferSizeLimit(sendBufferSizePerWebSocketSession);

        log.info("Set web socket message size limit to {}", messageSizeLimit);
        log.info("Set web socket send buffer size to {}", sendBufferSizePerWebSocketSession);
    }
}