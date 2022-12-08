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
import oyns.billshare.party.dto.PartyCreationDto;
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
        if (jsonObject.get("type").equals("add user")) {
            partyService.saveNewUserToParty(UserDto.builder()
                    .userName(jsonObject.get("userName").toString())
                    .build(), jsonObject.get("partyId").toString());
            session.sendMessage(new TextMessage(packingToJson(message).toString()));
        } else if (jsonObject.get("type").equals("add item")) {
            itemService.saveItem(ItemDto.builder()
                    .name(jsonObject.get("itemName").toString())
                    .price(Double.valueOf(jsonObject.get("itemPrice").toString()))
                    .user(UUID.fromString(jsonObject.get("userId").toString()))
                    .build(), jsonObject.get("partyId").toString(), jsonObject.get("userId").toString());
            session.sendMessage(new TextMessage(packingToJson(message).toString()));
        } else if (jsonObject.get("type").equals("remove user")) {
            partyService.deleteUserFromParty(jsonObject.get("userId").toString(),
                    jsonObject.get("partyId").toString());
            session.sendMessage(new TextMessage(packingToJson(message).toString()));
        } else if (jsonObject.get("type").equals("remove item")) {
            partyService.deleteItemFromParty(jsonObject.get("itemId").toString(),
                    jsonObject.get("partyId").toString());
            session.sendMessage(new TextMessage(packingToJson(message).toString()));
        }
    }

    private JSONObject packingToJson(TextMessage message) {
        String payload = message.getPayload();
        JSONObject jsonObject = new JSONObject(payload);
        PartyCreationDto partyCreationDto = partyService.getPartyById(jsonObject.get("partyId").toString());
        partyCreationDto.setType(jsonObject.get("type").toString());
        return new JSONObject(partyCreationDto);
    }
}