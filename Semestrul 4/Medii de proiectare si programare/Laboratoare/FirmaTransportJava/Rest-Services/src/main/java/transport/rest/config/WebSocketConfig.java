package transport.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import transport.rest.security.JwtUtils;
import transport.rest.websocket.WebSocketAuthHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketAuthHandler webSocketAuthHandler;
    private final JwtUtils jwtUtils;

    public WebSocketConfig(WebSocketAuthHandler webSocketAuthHandler, JwtUtils jwtUtils) {
        this.webSocketAuthHandler = webSocketAuthHandler;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketAuthHandler, "/ws-transport")
                .setAllowedOrigins("http://localhost:5173","http://localhost:5174")
                .addInterceptors(new WebSocketHandshakeInterceptor(jwtUtils));
    }
}
