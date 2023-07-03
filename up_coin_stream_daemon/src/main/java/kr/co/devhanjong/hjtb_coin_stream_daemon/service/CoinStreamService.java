package kr.co.devhanjong.hjtb_coin_stream_daemon.service;

import kr.co.devhanjong.hjtb_coin_stream_daemon.config.WebSocketConnectionManager;
import kr.co.devhanjong.hjtb_coin_stream_daemon.config.WebSocketRestartManager;
import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.LiveStreamWebSocketDto;
import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.MarketTrackingDto;
import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.RestartKeyDto;
import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.kafka.StreamRestartDto;
import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.kafka.StreamStartDto;
import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.kafka.StreamStopDto;
import kr.co.devhanjong.hjtb_coin_stream_daemon.mapper.WebSocketMapper;
import kr.co.devhanjong.hjtb_coin_stream_daemon.model.websocket.WebSocketConnectionMapValue;
import kr.co.devhanjong.hjtb_coin_stream_daemon.submodule.up_exception_monitoring_module.ExceptionMonitoringApiResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static kr.co.devhanjong.hjtb_coin_stream_daemon.model.ApiResponse.mapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class CoinStreamService {

    private final WebSocketMapper webSocketMapper;

    private final WebSocketThreadSupport webSocketThreadSupport;

    private final WebSocketConnectionManager webSocketConnectionManager;

    private final ExceptionMonitoringApiResponseUtil exceptionMonitoringApiResponseUtil;

    private final WebSocketRestartManager webSocketRestartManager;

    private final WebsocketServiceSupport websocketServiceSupport;

    public void streamStart(String message) {
        try {
            exceptionMonitoringApiResponseUtil.insertMonitoring("LOW", "streamStart 요청 받음", message);
            StreamStartDto streamStartDto = mapper.readValue(message, StreamStartDto.class);

            // 30초 이내의 요청이라면 실행
            Duration diff = Duration.between(LocalDateTime.parse(streamStartDto.getRequestTime()), LocalDateTime.now());
            if(!(diff.compareTo(Duration.ofMillis(30000)) <= 0)) return;

            MarketTrackingDto marketTrackingDto = websocketServiceSupport.isCreateWebSocketPossible(streamStartDto.getVaspSimpleName(), streamStartDto.getMySymbol());

            if(marketTrackingDto == null){
                return;
            }

            webSocketThreadSupport.createWebSocket(marketTrackingDto);
        } catch (Exception e) {
            exceptionMonitoringApiResponseUtil.insertMonitoring("HIGH", "streamStart 요청 받음 -> exception", ExceptionUtils.getMessage(e));
        }
    }

    public boolean streamStop(String message){
        try {
            exceptionMonitoringApiResponseUtil.insertMonitoring("LOW", "streamStop 요청 받음", message);
            StreamStopDto streamStopDto = mapper.readValue(message, StreamStopDto.class);

            // 30초 이내의 요청이라면 실행
            Duration diff = Duration.between(LocalDateTime.parse(streamStopDto.getRequestTime()), LocalDateTime.now());
            if(!(diff.compareTo(Duration.ofMillis(30000)) <= 0)) return false;

            LiveStreamWebSocketDto liveStreamWebSocketDto = webSocketMapper.selectLiveWebSocket(streamStopDto.getVaspSimpleName(), streamStopDto.getMySymbol());
            if(liveStreamWebSocketDto == null) return false;

            // 내 서비스인지 확인해보자
            if(webSocketConnectionManager.isMyConnection(liveStreamWebSocketDto.getThreadKey())){
                WebSocketConnectionMapValue connection = webSocketConnectionManager.getConnection(liveStreamWebSocketDto.getThreadKey());

                CloseStatus closeStatus = new CloseStatus(1000, "streamStop");
                connection.getWebSocketSession().close(closeStatus);
                return true;
            }
            else {
                // TODO IP가 다른곳에 있으면 post msg 전송
            }
        } catch (Exception e) {
            exceptionMonitoringApiResponseUtil.insertMonitoring("HIGH", "streamStop 요청 받음 -> exception", ExceptionUtils.getMessage(e));
        }
        return false;
    }

    public void streamRestart(String message){
        try {
            exceptionMonitoringApiResponseUtil.insertMonitoring("LOW", "streamRestart 요청 받음", message);

            StreamRestartDto streamRestartDto = mapper.readValue(message, StreamRestartDto.class);

            // 이미 재시작 중인게 있는지 확인해야함
            RestartKeyDto restartKeyDto = RestartKeyDto.builder()
                    .vaspSimpleName(streamRestartDto.getVaspSimpleName())
                    .mySymbol(streamRestartDto.getMySymbol()).build();
            boolean running = webSocketRestartManager.isRunning(restartKeyDto);

            if(running) {
                exceptionMonitoringApiResponseUtil.insertMonitoring("LOW", "streamRestart 요청 받음 -> 이미 restart 중이라 pass", message);
                return;
            }

            webSocketRestartManager.setStatus(restartKeyDto, "restart");

            StreamStopDto streamStopDto = StreamStopDto.builder()
                    .requestFrom("streamDaemon")
                    .requestReason("streamRestart 요청 받음 -> streamStop 호출 " + message)
                    .threadIp(streamRestartDto.getThreadIp())
                    .vaspSimpleName(streamRestartDto.getVaspSimpleName())
                    .mySymbol(streamRestartDto.getMySymbol())
                    .requestTime(streamRestartDto.getRequestTime())
                    .build();

            boolean stopResult = streamStop(mapper.writeValueAsString(streamStopDto));

            if(stopResult){
                // 자원 정리시간이 필요하므로 조금 기다려야함 10초 대기하자
                try {
                    TimeUnit.MICROSECONDS.sleep(10000);
//                    Thread.sleep(10000);
                } catch (Exception ignored) {}

                StreamStartDto streamStartDto = StreamStartDto.builder()
                        .requestFrom("streamDaemon")
                        .requestReason("streamRestart 요청 받음 -> streamStart 호출 " + message)
                        .threadIp(streamRestartDto.getThreadIp())
                        .vaspSimpleName(streamRestartDto.getVaspSimpleName())
                        .mySymbol(streamRestartDto.getMySymbol())
                        .requestTime(streamRestartDto.getRequestTime())
                        .build();

                streamStart(mapper.writeValueAsString(streamStartDto));
            }

            webSocketRestartManager.removeStatus(restartKeyDto);
        } catch (Exception e) {
            exceptionMonitoringApiResponseUtil.insertMonitoring("HIGH", "streamRestart 요청 받음 -> exception", ExceptionUtils.getMessage(e));
        }
    }


}
