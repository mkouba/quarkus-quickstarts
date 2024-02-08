package org.acme.websockets;

import io.quarkus.logging.Log;
import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnMessage;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;

@WebSocket("/chat/next/:username")
public class ChatSocketNext {

    @Inject
    WebSocketConnection connection;

    @OnOpen
    void onOpen() {
        Log.infof("OPEN: %s", connection.pathParam("username"));
    }

    @OnClose
    void onClose() {
        Log.infof("CLOSE: %s", connection.pathParam("username"));
    }

    @OnMessage
    public Uni<Void> onMessage(String message) {
        String username = connection.pathParam("username");
        if (message.equalsIgnoreCase("_ready_")) {
            return connection.broadcast()
                    .sendText("User " + username + " joined");
        } else {
            return connection.broadcast()
                    .sendText(">> " + username + ": " + message);
        }
    }

}
