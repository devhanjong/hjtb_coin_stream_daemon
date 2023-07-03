package kr.co.devhanjong.hjtb_coin_stream_daemon.model.websocket;

import kr.co.devhanjong.hjtb_coin_stream_daemon.handler.MyWebSocketHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ScheduledExecutorService;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketConnectionMapValue {
    private WebSocketSession webSocketSession;
    private ScheduledExecutorService scheduledPing;
    private ScheduledExecutorService scheduledRunning;
    private MyWebSocketHandler myWebSocketHandler;
}
