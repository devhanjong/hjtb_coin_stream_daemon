package kr.co.devhanjong.hjtb_coin_stream_daemon.service;

import kr.co.devhanjong.hjtb_coin_stream_daemon.config.WebSocketConnectionManager;
import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.LiveStreamWebSocketDto;
import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.MarketTrackingDto;
import kr.co.devhanjong.hjtb_coin_stream_daemon.handler.MyWebSocketHandler;
import kr.co.devhanjong.hjtb_coin_stream_daemon.kafka.KafkaProducer;
import kr.co.devhanjong.hjtb_coin_stream_daemon.model.websocket.WebSocketConnectionMapValue;
import kr.co.devhanjong.hjtb_coin_stream_daemon.submodule.up_exception_monitoring_module.ExceptionMonitoringApiResponseUtil;
import kr.co.devhanjong.hjtb_coin_stream_daemon.util.IpAddressUtil;
import kr.co.devhanjong.hjtb_coin_stream_daemon.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketThreadSupport {

    private final ThreadPoolTaskExecutor socketThread;

    private final WebSocketConnectionManager webSocketConnectionManager;
    private final WebSocketClient webSocketClient;

    private final WebSocketService webSocketService;

    private final KafkaProducer kafkaProducer;

    private final IpAddressUtil ipAddressUtil;

    private final RedisUtil redisUtil;

    private final ExceptionMonitoringApiResponseUtil exceptionMonitoringApiResponseUtil;

    public void createWebSocket(MarketTrackingDto marketTrackingDto){
        log.info("try make uri :: " + marketTrackingDto.getVaspSimpleName() + " :: " + marketTrackingDto.getMySymbol());
        String websocketUri = webSocketService.createWebsocketUri(marketTrackingDto);

        // 1커넥션으로 관리되는지 아닌지 구분해야함
        if(!webSocketService.isWebSocketSingleConnection(marketTrackingDto)){
            if(!"".equalsIgnoreCase(websocketUri)){
                log.info("now start :: " + websocketUri);
                newConnection(marketTrackingDto, websocketUri);
            }
            else {
                log.warn("uri 빌드 실패");
            }
        }
        else {
            if(marketTrackingDto.isNewWebSocket()){
                newConnection(marketTrackingDto, websocketUri);
            }
            else {
                newSubScribe(marketTrackingDto, websocketUri);
            }
        }
    }

    private void newConnection(MarketTrackingDto marketTrackingDto, String websocketUri){
        String hostIp = IpAddressUtil.getHostIp();

        MyWebSocketHandler myWebSocketHandler = new MyWebSocketHandler(marketTrackingDto, webSocketConnectionManager, kafkaProducer,
                this, webSocketService, hostIp, redisUtil, exceptionMonitoringApiResponseUtil);
        CompletableFuture.runAsync(() -> {
            Future<WebSocketSession> execute = webSocketClient.execute(myWebSocketHandler, websocketUri);

            try {
                WebSocketSession webSocketSession = execute.get();

                myWebSocketHandler.maybeNeedSubscribe(webSocketSession, marketTrackingDto);

                ScheduledExecutorService scheduledRunning = myWebSocketHandler.makeRunningHealthCheckScheduledExecutor(webSocketSession);
                ScheduledExecutorService scheduledPing = myWebSocketHandler.maybeNeedPing(webSocketSession);

                WebSocketConnectionMapValue connection = webSocketConnectionManager.getConnection(webSocketSession.getId());
                if(scheduledPing != null) connection.setScheduledPing(scheduledPing);
                if(scheduledRunning != null) connection.setScheduledRunning(scheduledRunning);

                webSocketConnectionManager.setConnection(webSocketSession.getId(), connection);
            } catch (InterruptedException| ExecutionException e) {
                throw new RuntimeException(e);
            }
        }, socketThread);
    }

    private void newSubScribe(MarketTrackingDto marketTrackingDto, String websocketUri){
        String hostIp = IpAddressUtil.getHostIp();
        WebSocketConnectionMapValue connection = webSocketConnectionManager.getConnection(marketTrackingDto.getThreadKey());

        connection.getMyWebSocketHandler().maybeNeedSubscribe(connection.getWebSocketSession(), marketTrackingDto);

        webSocketService.insertLiveWebSocket(LiveStreamWebSocketDto.builder()
                .threadKey(marketTrackingDto.getThreadKey())
                .threadIp(hostIp)
                .vaspSimpleName(marketTrackingDto.getVaspSimpleName())
                .mySymbol(marketTrackingDto.getMySymbol())
                .status("Y")
                .build());
    }
}
