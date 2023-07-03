package kr.co.devhanjong.hjtb_coin_stream_daemon.mapper;

import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WebSocketMapper {

    List<VaspListDto> getVaspList(@Param("health") String health);

    List<MarketTrackingDto> selectSymbolFromTrackingList();

    List<VaspWebSocketEndPointDto> selectVaspWebSocketEndPoint();

    int updateLiveWebSocket(@Param("liveStreamWebSocketDto") LiveStreamWebSocketDto liveStreamWebSocketDto);
    int insertLiveWebSocket(@Param("liveStreamWebSocketDto") LiveStreamWebSocketDto liveStreamWebSocketDto);
    int deleteLiveWebSocket(@Param("liveStreamWebSocketDto") LiveStreamWebSocketDto liveStreamWebSocketDto);
    int deleteAllLiveWebSocket(@Param("liveStreamWebSocketDto") LiveStreamWebSocketDto liveStreamWebSocketDto);

    LiveStreamWebSocketDto selectLiveWebSocket(@Param("vaspSimpleName") String vaspSimpleName,
                                               @Param("mySymbol") String mySymbol);
    LiveStreamWebSocketDto selectLiveWebSocketByVaspSimpleName(@Param("vaspSimpleName") String vaspSimpleName);

    MarketTrackingDto selectMarketTrackingList(@Param("vaspSimpleName") String vaspSimpleName,
                                               @Param("mySymbol") String mySymbol);

    void insertWebSocketHistory(@Param("webSocketHistoryDto") WebSocketHistoryDto webSocketHistoryDto);
}
