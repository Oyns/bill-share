package oyns.billshare.webconfig;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import oyns.billshare.item.dto.ItemDto;
import oyns.billshare.item.service.ItemServiceImpl;
import oyns.billshare.party.dto.FullPartyDto;
import oyns.billshare.party.service.PartyServiceImpl;
import oyns.billshare.user.dto.UserDto;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final PartyServiceImpl partyService;
    private final ItemServiceImpl itemService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(partyHandler(), "/ws/*")
                .addInterceptors(partyInterceptor())
                .setAllowedOrigins("*");
    }

    @Bean
    public HandshakeInterceptor partyInterceptor() {
        return new HandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(@NonNull ServerHttpRequest request,
                                           @NonNull ServerHttpResponse response,
                                           @NonNull WebSocketHandler wsHandler,
                                           @NonNull Map<String, Object> attributes) {
                String path = request.getURI().getPath();
                String partyId = path.substring(path.lastIndexOf('/') + 1);
                attributes.put("partyId", partyId);
                return true;
            }

            @Override
            public void afterHandshake(@NonNull ServerHttpRequest request,
                                       @NonNull ServerHttpResponse response,
                                       @NonNull WebSocketHandler wsHandler,
                                       Exception exception) {
            }
        };
    }

    @Bean
    public WebSocketHandler partyHandler() {
        return new TextWebSocketHandler() {
            public void handleTextMessage(@NonNull WebSocketSession session,
                                          @NonNull TextMessage message)
                    throws IOException {
                String partyId = session.getAttributes().get("partyId").toString();
                if (Objects.requireNonNull(session.getUri()).toString().contains(partyId)) {
                    String payload = message.getPayload();
                    JSONObject jsonObject = new JSONObject(payload);
                    switch (jsonObject.get("type").toString()) {
                        case "add user" -> addUser(jsonObject, session, message);
                        case "add item" -> addItem(jsonObject, session, message);
                        case "remove user" -> removeUser(jsonObject, session, message);
                        case "remove item" -> removeItem(jsonObject, session, message);
                        case "add user to item" -> addUserToItem(jsonObject, session, message);
                        case "remove user from item" -> removeUserFromItem(jsonObject, session, message);
                        case "update item" -> validateItemUpdate(jsonObject, session, message);
                    }
                }
            }

            private JSONObject packingToJson(TextMessage message) {
                String payload = message.getPayload();
                JSONObject jsonObject = new JSONObject(payload);
                FullPartyDto fullPartyDto = partyService.getPartyById(jsonObject.get("partyId").toString());
                fullPartyDto.setType(jsonObject.get("type").toString());
                return new JSONObject(fullPartyDto);
            }

            private void addUser(JSONObject jsonObject, WebSocketSession session, TextMessage message) throws IOException {
                partyService.addUserToParty(UserDto.builder()
                        .name(jsonObject.get("userName").toString())
                        .build(), jsonObject.get("partyId").toString());
                session.sendMessage(new TextMessage(packingToJson(message).toString()));
            }

            private void addItem(JSONObject jsonObject, WebSocketSession session, TextMessage message) throws IOException {
                itemService.saveItem(ItemDto.builder()
                        .name(jsonObject.get("itemName").toString())
                        .price(jsonObject.getDouble("itemPrice"))
                        .amount(jsonObject.getInt("amount"))
                        .user(UUID.fromString(jsonObject.get("userId").toString()))
                        .build(), jsonObject.get("partyId").toString(), jsonObject.get("userId").toString());
                session.sendMessage(new TextMessage(packingToJson(message).toString()));
            }

            private void removeUser(JSONObject jsonObject, WebSocketSession session, TextMessage message) throws IOException {
                partyService.removeUserFromParty(jsonObject.get("userId").toString(),
                        jsonObject.get("partyId").toString());
                session.sendMessage(new TextMessage(packingToJson(message).toString()));
            }

            private void removeItem(JSONObject jsonObject, WebSocketSession session, TextMessage message) throws IOException {
                partyService.removeItemFromParty(jsonObject.get("itemId").toString(),
                        jsonObject.get("partyId").toString());
                session.sendMessage(new TextMessage(packingToJson(message).toString()));
            }

            private void addUserToItem(JSONObject jsonObject,
                                       WebSocketSession session,
                                       TextMessage message) throws IOException {
                partyService.addUserToItem(jsonObject.get("userId").toString(), jsonObject.get("partyId").toString(),
                        jsonObject.get("itemId").toString(), jsonObject.getInt("value"));
                session.sendMessage(new TextMessage(packingToJson(message).toString()));
            }

            private void removeUserFromItem(JSONObject jsonObject,
                                            WebSocketSession session,
                                            TextMessage message) throws IOException {
                partyService.removeUserFromItem(jsonObject.get("userId").toString(),
                        jsonObject.get("partyId").toString(),
                        jsonObject.get("itemId").toString());
                session.sendMessage(new TextMessage(packingToJson(message).toString()));
            }

            private void updateItemPrice(JSONObject jsonObject) {
                partyService.updateItemInParty(jsonObject.get("userId").toString(),
                        jsonObject.get("partyId").toString(),
                        jsonObject.get("id").toString(),
                        jsonObject.getDouble("price"),
                        null,
                        null,
                        null,
                        null);
            }

            private void updateItemAmount(JSONObject jsonObject) {
                partyService.updateItemInParty(jsonObject.get("userId").toString(),
                        jsonObject.get("partyId").toString(),
                        jsonObject.get("id").toString(),
                        null,
                        jsonObject.getInt("amount"),
                        null,
                        null,
                        null);
            }

            private void updateItemDiscount(JSONObject jsonObject) {
                partyService.updateItemInParty(jsonObject.get("userId").toString(),
                        jsonObject.get("partyId").toString(),
                        jsonObject.get("id").toString(),
                        null,
                        null,
                        jsonObject.getDouble("discount"),
                        null,
                        null);
            }

            private void updateItemName(JSONObject jsonObject) {
                partyService.updateItemInParty(jsonObject.get("userId").toString(),
                        jsonObject.get("partyId").toString(),
                        jsonObject.get("id").toString(),
                        null,
                        null,
                        null,
                        jsonObject.getString("name"),
                        null);
            }

            private void updateItemEqually(JSONObject jsonObject) {
                partyService.updateItemInParty(jsonObject.get("userId").toString(),
                        jsonObject.get("partyId").toString(),
                        jsonObject.get("id").toString(),
                        null,
                        null,
                        null,
                        null,
                        jsonObject.getBoolean("equally"));
            }

            private void validateItemUpdate(JSONObject jsonObject,
                                            WebSocketSession session,
                                            TextMessage message) throws IOException {
                if (jsonObject.has("price")) {
                    updateItemPrice(jsonObject);
                } else if (jsonObject.has("amount")) {
                    updateItemAmount(jsonObject);
                } else if (jsonObject.has("discount")) {
                    updateItemDiscount(jsonObject);
                } else if (jsonObject.has("name")) {
                    updateItemName(jsonObject);
                } else if (jsonObject.has("equally")) {
                    updateItemEqually(jsonObject);
                }
                session.sendMessage(new TextMessage(packingToJson(message).toString()));
            }
        };
    }
}