package kr.co.devhanjong.hjtb_coin_stream_daemon.config;

import kr.co.devhanjong.hjtb_coin_stream_daemon.model.websocket.WebSocketConnectionMapValue;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketConnectionManager {
    private Map<String, WebSocketConnectionMapValue> connections = new ConcurrentHashMap<>();

    public void setConnection(String connectionId, WebSocketConnectionMapValue webSocketConnectionMapValue) {
        connections.put(connectionId, webSocketConnectionMapValue);
    }

    public void removeConnection(String connectionId) {
        connections.remove(connectionId);
    }

    public WebSocketConnectionMapValue getConnection(String connectionId) {
        return connections.get(connectionId);
    }

    public boolean isMyConnection(String connectionId){
        return connections.containsKey(connectionId);
    };
}
