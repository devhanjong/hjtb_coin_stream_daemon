package kr.co.devhanjong.hjtb_coin_stream_daemon.handler;

import kr.co.devhanjong.hjtb_coin_stream_daemon.config.WebSocketConnectionManager;
import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.LiveStreamWebSocketDto;
import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.MarketTrackingDto;
import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.StreamHogaDto;
import kr.co.devhanjong.hjtb_coin_stream_daemon.kafka.KafkaProducer;
import kr.co.devhanjong.hjtb_coin_stream_daemon.model.websocket.WebSocketConnectionMapValue;
import kr.co.devhanjong.hjtb_coin_stream_daemon.service.WebSocketService;
import kr.co.devhanjong.hjtb_coin_stream_daemon.service.WebSocketThreadSupport;
import kr.co.devhanjong.hjtb_coin_stream_daemon.submodule.up_exception_monitoring_module.ExceptionMonitoringApiResponseUtil;
import kr.co.devhanjong.hjtb_coin_stream_daemon.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static kr.co.devhanjong.hjtb_coin_stream_daemon.config.Const.*;

@Slf4j
public class MyWebSocketHandler implements WebSocketHandler {
    private final MarketTrackingDto marketTrackingDto;
    private final WebSocketConnectionManager webSocketConnectionManager;
    private final KafkaProducer kafkaProducer;
    private final WebSocketThreadSupport webSocketThreadSupport;

    private final WebSocketService webSocketService;
    private final String hostIp;

    private final RedisUtil redisUtil;

    private final ExceptionMonitoringApiResponseUtil exceptionMonitoringApiResponseUtil;

    public MyWebSocketHandler(MarketTrackingDto marketTrackingDto, WebSocketConnectionManager webSocketConnectionManager,
                              KafkaProducer kafkaProducer, WebSocketThreadSupport webSocketThreadSupport, WebSocketService webSocketService,
                              String hostIp, RedisUtil redisUtil, ExceptionMonitoringApiResponseUtil exceptionMonitoringApiResponseUtil){
        this.marketTrackingDto = marketTrackingDto;
        this.webSocketConnectionManager = webSocketConnectionManager;
        this.kafkaProducer = kafkaProducer;
        this.webSocketThreadSupport = webSocketThreadSupport;
        this.webSocketService = webSocketService;
        this.hostIp = hostIp;
        this.redisUtil = redisUtil;
        this.exceptionMonitoringApiResponseUtil = exceptionMonitoringApiResponseUtil;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // memory
        webSocketConnectionManager.setConnection(session.getId(), WebSocketConnectionMapValue.builder()
                .webSocketSession(session)
                .scheduledPing(null)
                .scheduledRunning(null)
                .myWebSocketHandler(this)
                .build());

        // db
        webSocketService.insertLiveWebSocket(LiveStreamWebSocketDto.builder()
                .threadKey(session.getId())
                .threadIp(this.hostIp)
                .vaspSimpleName(this.marketTrackingDto.getVaspSimpleName())
                .mySymbol(this.marketTrackingDto.getMySymbol())
                .status("Y")
                .build());

        log.info("connect !! " + this.marketTrackingDto.getVaspSimpleName());
        log.info("connect websocket session :: " + session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> originMessage) throws Exception {
        // 받은 데이터 처리
//        log.info("######### message");
//        log.info("session = " + session);
//        log.info("[{}] {} :: message = {}", session.getId(),
//                this.marketTrackingDto.getVaspSimpleName() + "-" + this.marketTrackingDto.getMySymbol(),
//                originMessage.getPayload().toString());

        // 시간, 거래소, 데이터 던진다
        LocalDateTime streamTime = LocalDateTime.now();
//        log.info("기준 시각 streamTime = " + streamTime.toString());

        String message = webSocketService.covertWebSocketOriginMessageToString(originMessage.getPayload(), this.marketTrackingDto.getVaspSimpleName());

        log.info("[{}] {} :: message = {}", session.getId(),
                this.marketTrackingDto.getVaspSimpleName() + "-" + this.marketTrackingDto.getMySymbol(),
                message.toString());

        StreamHogaDto streamHogaDto = StreamHogaDto.builder()
                .vaspSimpleName(this.marketTrackingDto.getVaspSimpleName())
                .streamTime(streamTime.toString())
                .mySymbol(this.marketTrackingDto.getMySymbol())
                .message(message)
                .build();


        if(webSocketService.isHogaMessage(streamHogaDto)){
            webSocketService.hogaSeqManager(streamHogaDto);
            int ranInt = ThreadLocalRandom.current().nextInt(100000);
//            kafkaProducer.sendMessage(Const.TOPIC_STREAM_HOGA, ranInt + "", ApiResponse.mapper.writeValueAsString(streamHogaDto));

            // DB 너무 많은 부하가 있으므로 대략 20 요청당 한번만 업데이트 해주자
            if(ranInt < 5000) {
                webSocketService.updateLiveWebSocket(session.getId());
                log.info("[{}] {} :: message = {}", session.getId(),
                        this.marketTrackingDto.getVaspSimpleName() + "-" + this.marketTrackingDto.getMySymbol(),
                        message.toString());
            }
        } else if (webSocketService.isPingMessage(streamHogaDto)) {
            String webSocketPongResponse = webSocketService.createWebSocketPongResponse(streamHogaDto);

//            log.info("webSocketPongResponse :: " + webSocketPongResponse);

            if(!"".equalsIgnoreCase(webSocketPongResponse)){
                sendMessage(webSocketPongResponse, session);
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // 에러 처리
        log.info("handleTransportError");
        log.info(ExceptionUtils.getStackTrace(exception));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        // 웹소켓 연결이 닫힌 후에 수행할 작업을 처리합니다.
        log.info("close websocket");

        WebSocketConnectionMapValue connection = webSocketConnectionManager.getConnection(session.getId());
        if(connection.getScheduledPing() != null){
            connection.getScheduledPing().shutdown();
        }

        if(connection.getScheduledRunning() != null){
            connection.getScheduledRunning().shutdown();
        }

        // memory
        webSocketConnectionManager.removeConnection(session.getId());

        // db
        webSocketService.deleteLiveWebSocket(session.getId(), marketTrackingDto, closeStatus);

        //TODO
        // webSokcetHandler에대한 자원관리는 스스로 해야함x
        // 소켓 연결마다 새로운 handler가 new 되고있는 구조라서 추후에 정리가 필요하다.

        if(closeStatus.getReason() != null && closeStatus.getReason().equalsIgnoreCase("streamStop")){
            // 내가 종료요청한거임
            exceptionMonitoringApiResponseUtil.insertMonitoring("LOW", "websocket close handler -> streamStop request", session.getId() + " :: " + marketTrackingDto + " :: " + closeStatus);
        }
        else if(closeStatus.getCode() == 1011){
            // server error
            exceptionMonitoringApiResponseUtil.insertMonitoring("MEDIUM", "websocket close handler -> server error", session.getId() + " :: " + marketTrackingDto + " :: " + closeStatus);
        }
        else {
            // 별다른 상황이 아니면 retry 되어야함
            log.warn("retry webSocket");
            exceptionMonitoringApiResponseUtil.insertMonitoring("LOW", "websocket close handler -> retry websocket", session.getId() + " :: " + marketTrackingDto + " :: " + closeStatus);

            // 기존 자원 정리를 위해 대기하자
            TimeUnit.MILLISECONDS.sleep(10000);
            webSocketThreadSupport.createWebSocket(this.marketTrackingDto);
        }

        // 주기적으로 배치를 돌면서 설정과 다른 웹소켓을 다시 생성한다

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendMessage(String message, WebSocketSession session) {
        try {
//            log.info("sendMessage :: " + message);
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {

        }
    }

    public void updateRedisStreamRunningTime(){
        redisUtil.setRedisData(redisUtil.makeStreamRunningTimeKey(this.marketTrackingDto.getVaspSimpleName(),
                this.marketTrackingDto.getMySymbol()), LocalDateTime.now().toString());
    }

    public void maybeNeedSubscribe(WebSocketSession session, MarketTrackingDto marketTrackingDto){
        String depthSubscribeRequest = webSocketService.createDepthSubscribeRequest(session, marketTrackingDto);

        if(!"".equalsIgnoreCase(depthSubscribeRequest)){
            sendMessage(depthSubscribeRequest, session);
        }
    }


    public ScheduledExecutorService maybeNeedPing(WebSocketSession session){
        if(BINANCE.equalsIgnoreCase(this.marketTrackingDto.getVaspSimpleName())){
            return null;
        } else if (PHEMEX.equalsIgnoreCase(this.marketTrackingDto.getVaspSimpleName())) {
            return createPingSchedule(session, 5,  webSocketService.createWebSocketPingRequest(this.marketTrackingDto));
        } else if (HUOBI.equalsIgnoreCase(this.marketTrackingDto.getVaspSimpleName())) {
            return null;
        }

        return null;
    }

    public ScheduledExecutorService createPingSchedule(WebSocketSession session, int period, String message){
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            sendMessage(message, session);
        }, 0, period, TimeUnit.SECONDS);

        return executorService;
    }

    // 5초 고정
    public ScheduledExecutorService makeRunningHealthCheckScheduledExecutor(WebSocketSession session){
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            updateRedisStreamRunningTime();
        }, 0, 5, TimeUnit.SECONDS);

        return executorService;
    }

    public void insertLiveWebSocket(){

    }
}
