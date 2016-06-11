package com.myee.niuroumian.config;

import com.myee.niuroumian.server.DefaultOrderServer;
import com.myee.niuroumian.server.OrderServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.List;

@Configuration
@EnableTransactionManagement
@EnableWebMvc
@EnableWebSocket
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer implements WebSocketConfigurer {

    @Autowired
    private OrderServer orderServer;

    /**
     * Enable STOMP over WebSocket
     * @param config
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic/", "/queue/");
        config.setApplicationDestinationPrefixes("/app");
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp").withSockJS()
                .setStreamBytesLimit(512 * 1024)
                .setHttpMessageCacheSize(1000)
                .setDisconnectDelay(3600 * 1000);
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        return true;
    }

    /**
     * Configure a WebSocketHandler
     * @param registry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(orderServer, "/order/orderOnline").addInterceptors(new HttpSessionHandshakeInterceptor());
        registry.addHandler(orderServer, "/order/orderOnline").withSockJS();
    }

    /**
     * Configuring the WebSocket Engine
     * @return
     */
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(100 * 1024);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }

    @Bean
    @Scope("singleton")
    public OrderServer orderServer() {
        return new DefaultOrderServer();
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
    }



}