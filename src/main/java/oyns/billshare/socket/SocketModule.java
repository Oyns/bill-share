package oyns.billshare.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import oyns.billshare.user.dto.UserDto;

@Component
@Slf4j
public class SocketModule {
    private final SocketService socketService;

    public SocketModule(SocketIOServer server, SocketService socketService) {
        this.socketService = socketService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send", UserDto.class, onChatReceived());
    }

    private DataListener<UserDto> onChatReceived() {
        return (senderClient, userDto, ackSender) -> {
            log.info(userDto.toString());
            socketService.saveParty(senderClient, userDto);
        };
    }

    private ConnectListener onConnected() {
        return (client) -> {
            client.getHandshakeData();
            log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("Socket ID{}  disconnected to chat module through",
                    client.getSessionId().toString());
        };
    }


}
