package kr.co.devhanjong.hjtb_coin_stream_daemon.service;

import jakarta.annotation.PostConstruct;
import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.MarketTrackingDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketThread {


    private final WebSocketService webSocketService;

    private final WebSocketThreadSupport webSocketThreadSupport;

    private final WebsocketServiceSupport websocketServiceSupport;

    @PostConstruct
    public void initializeWebSocketConnections() {
        log.info("initializeWebSocketConnections start");

        List<MarketTrackingDto> initWebSocketList = webSocketService.getInitWebSocketList();

        for (MarketTrackingDto marketTrackingDto : initWebSocketList) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }

            MarketTrackingDto createWebSocketPossible = websocketServiceSupport.isCreateWebSocketPossible(marketTrackingDto.getVaspSimpleName(), marketTrackingDto.getMySymbol());

            if(createWebSocketPossible == null) continue;

            marketTrackingDto.setThreadKey(createWebSocketPossible.getThreadKey());
            marketTrackingDto.setNewWebSocket(createWebSocketPossible.isNewWebSocket());
            webSocketThreadSupport.createWebSocket(marketTrackingDto);
        }
    }

}
