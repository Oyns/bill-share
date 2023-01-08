package oyns.billshare.webconfig;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import oyns.billshare.exception.EntityNotFoundException;
import oyns.billshare.item.dto.ItemDto;
import oyns.billshare.item.service.ItemServiceImpl;
import oyns.billshare.party.dto.FullPartyDto;
import oyns.billshare.party.model.Party;
import oyns.billshare.party.repository.PartyRepository;
import oyns.billshare.party.service.PartyServiceImpl;
import oyns.billshare.user.dto.NewUserDto;
import oyns.billshare.user.dto.UserDto;
import oyns.billshare.user.model.User;
import oyns.billshare.user.repository.UserRepository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static oyns.billshare.user.mapper.UserMapper.toUserDto;
import static oyns.billshare.user.mapper.UserMapper.toUserFromNew;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {
    private final PartyServiceImpl partyService;
    private final ItemServiceImpl itemService;
    private final UserRepository userRepository;
    private final PartyRepository partyRepository;

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

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

            @Override
            public void afterConnectionEstablished(@NonNull WebSocketSession session) {
                log.info("Connected " + session.getUri());
                sessions.add(session);
            }

            @Override
            public void afterConnectionClosed(@NonNull WebSocketSession session,
                                              @NonNull CloseStatus status) {
                log.info(String.format("Session %s closed because of %s", session.getId(), status.getReason()));
            }

            public void handleTextMessage(@NonNull WebSocketSession session,
                                          @NonNull TextMessage message)
                    throws IOException {
                log.info("message received: " + message.getPayload());
                String partyId = session.getAttributes().get("partyId").toString();
                if (Objects.requireNonNull(session.getUri()).toString().contains(partyId)) {
                    String payload = message.getPayload();
                    JSONObject jsonObject = new JSONObject(payload);
                    switch (jsonObject.get("type").toString()) {
                        case "add user" -> addUser(jsonObject, message);
                        case "add item" -> addItem(jsonObject, message, session);
                        case "remove user" -> removeUser(jsonObject, message);
                        case "remove item" -> removeItem(jsonObject, message);
                        case "add user to item" -> addUserToItem(jsonObject, message);
                        case "remove user from item" -> removeUserFromItem(jsonObject, message);
                        case "update item" -> validateItemUpdate(jsonObject, message, session);
                    }
                }
            }


            public void packingToJson(TextMessage message) throws IOException {
                String payload = message.getPayload();
                JSONObject jsonObject = new JSONObject(payload);
                FullPartyDto fullPartyDto = partyService.getPartyById(jsonObject.get("partyId").toString());
                fullPartyDto.setType(jsonObject.get("type").toString());
                for (WebSocketSession webSocketSession : sessions) {
                    String path = Objects.requireNonNull(webSocketSession.getUri()).getPath();
                    String partyId = path.substring(path.lastIndexOf('/') + 1);
                    if (webSocketSession.isOpen() && partyId.equals(fullPartyDto.getId().toString())) {
                        webSocketSession.sendMessage(new TextMessage(new JSONObject(fullPartyDto).toString()));
                    }
                }
            }

            private void addUser(JSONObject jsonObject,
                                 TextMessage message) throws IOException {
                partyService.addUserToParty(UserDto.builder()
                        .name(jsonObject.get("userName").toString())
                        .build(), jsonObject.get("partyId").toString());
                packingToJson(message);
            }

            private void addItem(JSONObject jsonObject,
                                 TextMessage message,
                                 WebSocketSession session) throws IOException {
                validateItemPrice(jsonObject, session);
                validateItemAmount(jsonObject, session);
                validateItemDiscount(jsonObject, session);
                itemService.saveItem(ItemDto.builder()
                        .name(jsonObject.get("itemName").toString())
                        .price(jsonObject.getDouble("itemPrice"))
                        .amount(jsonObject.getInt("amount"))
                        .user(UUID.fromString(jsonObject.get("userId").toString()))
                        .build(), jsonObject.get("partyId").toString(), jsonObject.get("userId").toString());
                packingToJson(message);
            }

            private void removeUser(JSONObject jsonObject, TextMessage message) throws IOException {
                partyService.removeUserFromParty(jsonObject.get("userId").toString(),
                        jsonObject.get("partyId").toString());
                packingToJson(message);
            }

            private void removeItem(JSONObject jsonObject, TextMessage message) throws IOException {
                partyService.removeItemFromParty(jsonObject.get("itemId").toString(),
                        jsonObject.get("partyId").toString());
                packingToJson(message);
            }

            private void addUserToItem(JSONObject jsonObject,
                                       TextMessage message) throws IOException {
                partyService.addUserToItem(jsonObject.get("userId").toString(), jsonObject.get("partyId").toString(),
                        jsonObject.get("itemId").toString(), jsonObject.getInt("value"));
                packingToJson(message);
            }

            private void removeUserFromItem(JSONObject jsonObject,
                                            TextMessage message) throws IOException {
                partyService.removeUserFromItem(jsonObject.get("userId").toString(),
                        jsonObject.get("partyId").toString(),
                        jsonObject.get("itemId").toString());
                packingToJson(message);
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
                                            TextMessage message, WebSocketSession session) throws IOException {
                if (jsonObject.has("price")) {
                    validateItemPrice(jsonObject, session);
                    updateItemPrice(jsonObject);
                } else if (jsonObject.has("amount")) {
                    validateItemAmount(jsonObject, session);
                    updateItemAmount(jsonObject);
                } else if (jsonObject.has("discount")) {
                    validateItemDiscount(jsonObject, session);
                    updateItemDiscount(jsonObject);
                } else if (jsonObject.has("name")) {
                    updateItemName(jsonObject);
                } else if (jsonObject.has("equally")) {
                    updateItemEqually(jsonObject);
                }
                packingToJson(message);
            }
        };
    }

    private void validateItemPrice(JSONObject jsonObject,
                                   WebSocketSession session) throws IOException {
        if (jsonObject.has("price")) {
            if (jsonObject.getDouble("price") < 0) {
                session.sendMessage(new TextMessage(sendErrorMessage()));
            }
        }
    }

    private void validateItemAmount(JSONObject jsonObject,
                                    WebSocketSession session) throws IOException {
        if (jsonObject.has("amount")) {
            if (jsonObject.getInt("amount") < 0) {
                session.sendMessage(new TextMessage(sendErrorMessage()));
            }
        }
    }

    private void validateItemDiscount(JSONObject jsonObject,
                                      WebSocketSession session) throws IOException {
        if (jsonObject.has("discount")) {
            if (jsonObject.getDouble("discount") < 0 || jsonObject.getDouble("discount") >= 1) {
                session.sendMessage(new TextMessage(sendErrorMessage()));
            }
        }
    }

    private String sendErrorMessage() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "error");
        jsonObject.put("message", "Value must be positive or zero. Else in case of discount not more then 1.");
        return jsonObject.toString();
    }

    public UserDto saveUser(NewUserDto newUserDto) throws IOException {
        log.info("Save user {}", newUserDto);
        Party party = partyRepository.findById(newUserDto.getPartyId())
                .orElseThrow(() -> new EntityNotFoundException("Нет пати с таким id"));
        Set<User> users = party.getUsers();
        User user = userRepository.save(toUserFromNew(newUserDto));
        users.add(user);
        party.setUsers(users);
        partyRepository.save(party);
        FullPartyDto fullPartyDto = partyService.getPartyById(party.getId().toString());
        fullPartyDto.setType("add user");
        JSONObject jsonObject = new JSONObject(fullPartyDto);
        for (WebSocketSession webSocketSession : sessions) {
            String path = Objects.requireNonNull(webSocketSession.getUri()).getPath();
            String partyId = path.substring(path.lastIndexOf('/') + 1);
            if (webSocketSession.isOpen() && partyId.equals(fullPartyDto.getId().toString())) {
                webSocketSession.sendMessage(new TextMessage(jsonObject.toString()));
            }
        }
        return toUserDto(user);
    }
}