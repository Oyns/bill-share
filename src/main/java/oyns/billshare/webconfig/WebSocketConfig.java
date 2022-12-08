package oyns.billshare.webconfig;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import oyns.billshare.item.service.ItemServiceImpl;
import oyns.billshare.party.service.PartyServiceImpl;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final PartyServiceImpl partyService;
    private final ItemServiceImpl itemService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(new SocketTextHandler(partyService, itemService), "/ws")
                .setAllowedOriginPatterns("*");
    }
}