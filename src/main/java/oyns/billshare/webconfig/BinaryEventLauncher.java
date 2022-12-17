package oyns.billshare.webconfig;

import io.socket.client.IO;
import io.socket.client.Socket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;

@Configuration
public class BinaryEventLauncher {
    @Bean
    public void setupSocket() throws URISyntaxException {
        Socket socket = IO.socket("http://localhost:8087");
        socket.on(Socket.EVENT_CONNECT_ERROR, objects -> System.out.println("connect"));
        socket.connect();
    }
}