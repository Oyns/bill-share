package oyns.billshare.socket;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oyns.billshare.party.dto.PartyCreationDto;
import oyns.billshare.party.service.PartyServiceImpl;
import oyns.billshare.user.dto.UserDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocketService {
    private final PartyServiceImpl partyService;

    public void sendSocketMessage(SocketIOClient senderClient, PartyCreationDto partyCreationDto, String room) {
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent("read_message",
                        partyCreationDto);
            }
        }
    }

    public void saveParty(SocketIOClient senderClient, UserDto userDto) {
        PartyCreationDto storedParty = partyService.saveParty(userDto);
        sendSocketMessage(senderClient, storedParty, storedParty.getName());
    }
}
