//package oyns.billshare.webconfig;
//
//import io.socket.client.IO;
//import io.socket.client.Socket;
//import io.socket.emitter.Emitter;
//import io.socket.engineio.client.transports.Polling;
//import io.socket.engineio.client.transports.WebSocket;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//
//import java.net.URI;
//@Configuration
//@EnableWebSocket
//public class IOSocketConfig {
//    URI uri = URI.create("http://localhost:8087");
//    IO.Options options = IO.Options.builder()
//            // IO factory options
//            .setForceNew(false)
//            .setMultiplex(true)
//
//            // low-level engine options
//            .setTransports(new String[]{Polling.NAME, WebSocket.NAME})
//            .setUpgrade(true)
//            .setRememberUpgrade(false)
//            .setPath("/socket.io/")
//            .setQuery(null)
//            .setExtraHeaders(null)
//
//            // Manager options
//            .setReconnection(true)
//            .setReconnectionAttempts(Integer.MAX_VALUE)
//            .setReconnectionDelay(1_000)
//            .setReconnectionDelayMax(5_000)
//            .setRandomizationFactor(0.5)
//            .setTimeout(20_000)
//
//            // Socket options
//            .setAuth(null)
//            .build();
//
//    Socket socket = IO.socket(uri, options);
//
//    private void testMethod() {
//        socket.on("add user", new Emitter.Listener() {
//            @Override
//            public void call(Object... objects) {
//                System.out.println("контакт"); // world
//            }
//        });
//    }
//}
