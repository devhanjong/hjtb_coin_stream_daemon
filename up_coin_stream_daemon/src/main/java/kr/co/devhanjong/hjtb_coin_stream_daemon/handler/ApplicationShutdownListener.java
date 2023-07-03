package kr.co.devhanjong.hjtb_coin_stream_daemon.handler;

import kr.co.devhanjong.hjtb_coin_stream_daemon.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationShutdownListener implements ApplicationListener<ContextClosedEvent> {
    private final WebSocketService webSocketService;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        webSocketService.removeAllWebSocket();
    }
}
