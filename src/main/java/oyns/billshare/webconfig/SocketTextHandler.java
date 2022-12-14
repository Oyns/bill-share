package oyns.billshare.webconfig;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import oyns.billshare.item.dto.ItemDto;
import oyns.billshare.item.service.ItemServiceImpl;
import oyns.billshare.party.dto.FullPartyDto;
import oyns.billshare.party.service.PartyServiceImpl;
import oyns.billshare.user.dto.UserDto;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SocketTextHandler extends TextWebSocketHandler {
    private final PartyServiceImpl partyService;
    private final ItemServiceImpl itemService;

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, TextMessage message)
            throws IOException {

        String payload = message.getPayload();
        JSONObject jsonObject = new JSONObject(payload);
        switch (jsonObject.get("type").toString()) {
            case "add user" -> {
                partyService.addUserToParty(UserDto.builder()
                        .name(jsonObject.get("userName").toString())
                        .build(), jsonObject.get("partyId").toString());
                session.sendMessage(new TextMessage(packingToJson(message).toString()));
            }
            case "add item" -> {
                itemService.saveItem(ItemDto.builder()
                        .name(jsonObject.get("itemName").toString())
                        .price(Double.valueOf(jsonObject.get("itemPrice").toString()))
                        .user(UUID.fromString(jsonObject.get("userId").toString()))
                        .build(), jsonObject.get("partyId").toString(), jsonObject.get("userId").toString());
                session.sendMessage(new TextMessage(packingToJson(message).toString()));
            }
            case "remove user" -> {
                partyService.removeUserFromParty(jsonObject.get("userId").toString(),
                        jsonObject.get("partyId").toString());
                session.sendMessage(new TextMessage(packingToJson(message).toString()));
            }
            case "remove item" -> {
                partyService.removeItemFromParty(jsonObject.get("itemId").toString(),
                        jsonObject.get("partyId").toString());
                session.sendMessage(new TextMessage(packingToJson(message).toString()));
            }
            case "add user to item" -> {
                partyService.addUserToItem(jsonObject.get("userId").toString(), jsonObject.get("partyId").toString(),
                        jsonObject.get("itemId").toString(), jsonObject.getInt("value"));
                session.sendMessage(new TextMessage(packingToJson(message).toString()));
            }
            case "remove user from item" -> {
                partyService.removeUserFromItem(jsonObject.get("userId").toString(),
                        jsonObject.get("partyId").toString(),
                        jsonObject.get("itemId").toString());
                session.sendMessage(new TextMessage(packingToJson(message).toString()));
            }
            case "update item" -> {
                if (jsonObject.has("price")) {
                    partyService.updateItemInParty(jsonObject.get("userId").toString(),
                            jsonObject.get("partyId").toString(),
                            jsonObject.get("id").toString(),
                            jsonObject.getDouble("price"),
                            null,
                            null,
                            null,
                            null);
                } else if (jsonObject.has("amount")) {
                    partyService.updateItemInParty(jsonObject.get("userId").toString(),
                            jsonObject.get("partyId").toString(),
                            jsonObject.get("id").toString(),
                            null,
                            jsonObject.getInt("amount"),
                            null,
                            null,
                            null);
                } else if (jsonObject.has("discount")) {
                    partyService.updateItemInParty(jsonObject.get("userId").toString(),
                            jsonObject.get("partyId").toString(),
                            jsonObject.get("id").toString(),
                            null,
                            null,
                            jsonObject.getDouble("discount"),
                            null,
                            null);
                } else if (jsonObject.has("name")) {
                    partyService.updateItemInParty(jsonObject.get("userId").toString(),
                            jsonObject.get("partyId").toString(),
                            jsonObject.get("id").toString(),
                            null,
                            null,
                            null,
                            jsonObject.getString("name"),
                            null);
                } else if (jsonObject.has("equally")) {
                    partyService.updateItemInParty(jsonObject.get("userId").toString(),
                            jsonObject.get("partyId").toString(),
                            jsonObject.get("id").toString(),
                            null,
                            null,
                            null,
                            null,
                            jsonObject.getBoolean("equally"));
                }
                session.sendMessage(new TextMessage(packingToJson(message).toString()));
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
}