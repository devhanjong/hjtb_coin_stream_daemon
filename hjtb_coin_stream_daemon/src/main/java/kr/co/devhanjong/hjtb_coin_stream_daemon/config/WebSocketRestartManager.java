package kr.co.devhanjong.hjtb_coin_stream_daemon.config;

import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.RestartKeyDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketRestartManager {
    private Map<RestartKeyDto, String> statusMap = new ConcurrentHashMap<>();

    public void setStatus(RestartKeyDto restartKeyDto, String status) {
        statusMap.put(restartKeyDto, status);
    }

    public void removeStatus(RestartKeyDto restartKeyDto) {
        statusMap.remove(restartKeyDto);
    }

    public String getStatus(RestartKeyDto restartKeyDto) {
        return statusMap.get(restartKeyDto);
    }

    public boolean isRunning(RestartKeyDto restartKeyDto){
        return statusMap.containsKey(restartKeyDto);
    };
}
