package kr.co.devhanjong.hjtb_coin_stream_daemon.service;

import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.LiveStreamWebSocketDto;
import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.MarketTrackingDto;
import kr.co.devhanjong.hjtb_coin_stream_daemon.mapper.WebSocketMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebsocketServiceSupport {

    private final WebSocketMapper webSocketMapper;

    public MarketTrackingDto isCreateWebSocketPossible(String vaspSimpleName, String mySymbol){
        // 검색 후 중복없으면 실행
        LiveStreamWebSocketDto liveStreamWebSocketDto = webSocketMapper.selectLiveWebSocket(vaspSimpleName, mySymbol);

        if(liveStreamWebSocketDto != null) return null;

        MarketTrackingDto marketTrackingDto = webSocketMapper.selectMarketTrackingList(vaspSimpleName, mySymbol);

        if(marketTrackingDto == null) return null;

        LiveStreamWebSocketDto liveStreamWebSocketVasp = webSocketMapper.selectLiveWebSocketByVaspSimpleName(vaspSimpleName);

        if (liveStreamWebSocketVasp == null) {
            marketTrackingDto.setNewWebSocket(true);
        }
        else {
            marketTrackingDto.setNewWebSocket(false);
            marketTrackingDto.setThreadKey(liveStreamWebSocketVasp.getThreadKey());
        }

        return marketTrackingDto;
    }
}
