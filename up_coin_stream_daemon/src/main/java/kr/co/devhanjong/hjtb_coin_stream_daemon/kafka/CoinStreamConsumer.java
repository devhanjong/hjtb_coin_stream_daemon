package kr.co.devhanjong.hjtb_coin_stream_daemon.kafka;

import kr.co.devhanjong.hjtb_coin_stream_daemon.service.CoinStreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static kr.co.devhanjong.hjtb_coin_stream_daemon.config.Const.*;


@Component
@Slf4j
@RequiredArgsConstructor
public class CoinStreamConsumer {


    private final CoinStreamService coinStreamService;

    @KafkaListener(topics = TOPIC_STREAM_DAEMON_START)
    public void coinStreamStart(String message){
        log.info("message :: {} ", message);
        coinStreamService.streamStart(message);
    }

    @KafkaListener(topics = TOPIC_STREAM_DAEMON_STOP)
    public void coinStreamStop(String message){
        log.info("message :: {} ", message);
        coinStreamService.streamStop(message);
    }

    @KafkaListener(topics = TOPIC_STREAM_DAEMON_RESTART, concurrency = "10")
    public void coinStreamRestart(String message){
        log.info("message :: {} ", message);
        coinStreamService.streamRestart(message);
    }
}
