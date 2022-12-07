package oyns.billshare.webconfig;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import oyns.billshare.item.dto.ItemDto;
import oyns.billshare.item.service.ItemServiceImpl;
import oyns.billshare.party.service.PartyServiceImpl;
import oyns.billshare.user.dto.UserDto;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SocketTextHandler extends TextWebSocketHandler {
    private final PartyServiceImpl partyService;
    private final ItemServiceImpl itemService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws IOException {

        String payload = message.getPayload();
        JSONObject jsonObject = new JSONObject(payload);
        if (jsonObject.get("type").equals("add user")) {
            partyService.saveNewUserToParty(UserDto.builder()
                    .userName(jsonObject.get("userName").toString())
                    .build(), jsonObject.get("partyId").toString());
            session.sendMessage(new TextMessage("Hi " + jsonObject.get("userName") + " how may we help you?"));
        } else if (jsonObject.get("type").equals("add item")) {
            itemService.saveItem(ItemDto.builder()
                    .name("вещь блять")
                    .build(), jsonObject.get("partyId").toString(), jsonObject.get("userId").toString());
            session.sendMessage(new TextMessage("Hi " + jsonObject.get("partyId") + " how may we help you?"));
        }
    }
}