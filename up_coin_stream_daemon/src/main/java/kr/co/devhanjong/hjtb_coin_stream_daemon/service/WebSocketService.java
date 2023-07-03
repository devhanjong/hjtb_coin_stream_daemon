package kr.co.devhanjong.hjtb_coin_stream_daemon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.*;
import kr.co.devhanjong.hjtb_coin_stream_daemon.mapper.WebSocketMapper;
import kr.co.devhanjong.hjtb_coin_stream_daemon.memoryDb.HogaMemoryDb;
import kr.co.devhanjong.hjtb_coin_stream_daemon.model.huobi.WebSocketPingHuobi;
import kr.co.devhanjong.hjtb_coin_stream_daemon.model.huobi.WebSocketPongHuobi;
import kr.co.devhanjong.hjtb_coin_stream_daemon.model.huobi.WebSocketRequestHuobi;
import kr.co.devhanjong.hjtb_coin_stream_daemon.model.phemex.WebSocketRequestPhemex;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.devhanjong.hjtb_coin_stream_daemon.config.Const.*;
import static kr.co.devhanjong.hjtb_coin_stream_daemon.model.ApiResponse.mapper;
import static kr.co.devhanjong.hjtb_coin_stream_daemon.util.GzipUtils.decompress;
import static kr.co.devhanjong.hjtb_coin_stream_daemon.util.IpAddressUtil.getHostIp;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {

    private final WebSocketMapper webSocketMapper;

    private final HogaMemoryDb hogaMemoryDb;


    public List<MarketTrackingDto> getInitWebSocketList(){
        // TODO 지금은 모든 거래소가 하나의 서버에서 동작하도록 구현하지만 추후에는 거래소가 분기처리될 수 있음

        // 1. (추후추가) 대상 거래소를 선택한다.
        // 1. 지금은 health up인 거래소 전체를 가져온다
        List<VaspListDto> vaspList = webSocketMapper.getVaspList("up");

        // tracking_list 에서 데이터를 가져온다.
        // status 가 Y 이고 2개 이상 거래소에서 호출 가능한 tracking_list 를 가져온다.
        List<MarketTrackingDto> marketTrackingDtoList = webSocketMapper.selectSymbolFromTrackingList();
        marketTrackingDtoList.remove(1);

        return  marketTrackingDtoList.stream()
                    .filter(x -> vaspList.stream()
                        .anyMatch(y -> y.getVaspSimpleName().equalsIgnoreCase(x.getVaspSimpleName()))).collect(Collectors.toList());
    }

    public String createWebsocketUri(MarketTrackingDto marketTrackingDto){
        // vasp 별 websocket 엔드포인트를 조회한다.
        List<VaspWebSocketEndPointDto> vaspWebSocketEndPointDtoList = webSocketMapper.selectVaspWebSocketEndPoint();

        // 엔드포인트 DTO 를 찾는다.
        VaspWebSocketEndPointDto vaspWebSocketEndPointDto = vaspWebSocketEndPointDtoList.stream()
                .filter(x -> x.getVaspSimpleName().equalsIgnoreCase(marketTrackingDto.getVaspSimpleName()))
                .findFirst()
                .orElseThrow(RuntimeException::new);


        return makeWebSocketUriFromVasp(vaspWebSocketEndPointDto, marketTrackingDto);
    }


    private String makeWebSocketUriFromVasp(VaspWebSocketEndPointDto vaspWebSocketEndPointDto, MarketTrackingDto marketTrackingDto){
        String result = "";
        String vaspSimpleName = vaspWebSocketEndPointDto.getVaspSimpleName();
        String endPoint = vaspWebSocketEndPointDto.getEndPoint();

        switch (vaspSimpleName.toUpperCase()) {
            case BINANCE -> {
//                result = endPoint + marketTrackingDto.getVaspSymbol().toLowerCase() + "@depth5";
                result = endPoint + marketTrackingDto.getVaspSymbol().toLowerCase() + "@kline_5m";
            }
            case PHEMEX -> {
                result = endPoint;
            }
            case HUOBI -> {
                result = endPoint;
            }
        };

        return result;
    }


    public String createDepthSubscribeRequest(WebSocketSession session, MarketTrackingDto marketTrackingDto){
        String result = "";
        String vaspSimpleName = marketTrackingDto.getVaspSimpleName();

        switch (vaspSimpleName.toUpperCase()) {
            case BINANCE -> {
            }
            case PHEMEX -> {
                List<Object> paramList = new ArrayList<>();
                paramList.add("s" + marketTrackingDto.getVaspSymbol());
//                paramList.add(true);

                WebSocketRequestPhemex webSocketRequestPhemex = WebSocketRequestPhemex.builder()
                        .id(0)
                        .method("orderbook.subscribe")
                        .params(paramList).build();

                try {
                    result = mapper.writeValueAsString(webSocketRequestPhemex);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            case HUOBI -> {
                WebSocketRequestHuobi webSocketRequestHuobi = WebSocketRequestHuobi.builder()
                        .id(session.getId())
                        .sub("market." + marketTrackingDto.getVaspSymbol() + ".depth.step1")
                        .build();

                try {
                    result = mapper.writeValueAsString(webSocketRequestHuobi);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return result;
    }


    public String createWebSocketPingRequest(MarketTrackingDto marketTrackingDto){
        String result = "";
        String vaspSimpleName = marketTrackingDto.getVaspSimpleName();

        switch (vaspSimpleName.toUpperCase()) {
            case BINANCE -> {
            }
            case PHEMEX -> {
                List<Object> paramList = new ArrayList<>();
                WebSocketRequestPhemex webSocketPingRequestPhemex = WebSocketRequestPhemex.builder()
                        .id(0)
                        .method("server.ping")
                        .params(paramList).build();

                try {
                    result = mapper.writeValueAsString(webSocketPingRequestPhemex);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            case HUOBI -> {
            }
        }

        return result;
    }


    public boolean isHogaMessage(StreamHogaDto streamHogaDto){
        String vaspSimpleName = streamHogaDto.getVaspSimpleName();
        if(BINANCE.equalsIgnoreCase(vaspSimpleName)){
            return true;
        }
        else if(PHEMEX.equalsIgnoreCase(vaspSimpleName)){
            if(streamHogaDto.getMessage().contains("snapshot")
                    || streamHogaDto.getMessage().contains("incremental")){
                return true;
            }
        }
        else if(HUOBI.equalsIgnoreCase(vaspSimpleName)){
            if(streamHogaDto.getMessage().contains("tick")
                   && streamHogaDto.getMessage().contains("depth.step")){
                return true;
            }
        }

        return false;
    }

    public void hogaSeqManager(StreamHogaDto streamHogaDto){
        String vaspSimpleName = streamHogaDto.getVaspSimpleName();
        String mySymbol = streamHogaDto.getMySymbol();
        if(BINANCE.equalsIgnoreCase(vaspSimpleName)){
        }
        else if(PHEMEX.equalsIgnoreCase(vaspSimpleName)){
            if(streamHogaDto.getMessage().contains("snapshot")){
                hogaMemoryDb.setSortNo(vaspSimpleName, mySymbol, 0);
                streamHogaDto.setSortNo(0);
            } else if (streamHogaDto.getMessage().contains("incremental")) {
                Integer sortNo = hogaMemoryDb.getSortNo(vaspSimpleName, mySymbol);
                Integer nextSortNo = sortNo + 1;
                hogaMemoryDb.setSortNo(vaspSimpleName, mySymbol, nextSortNo);
                streamHogaDto.setSortNo(nextSortNo);
            }
        }
        else if(HUOBI.equalsIgnoreCase(vaspSimpleName)){
        }
    }

    public boolean isPingMessage(StreamHogaDto streamHogaDto){
        String vaspSimpleName = streamHogaDto.getVaspSimpleName();
        if(BINANCE.equalsIgnoreCase(vaspSimpleName)){
            return false;
        }
        else if(PHEMEX.equalsIgnoreCase(vaspSimpleName)){
            return false;
        }
        else if(HUOBI.equalsIgnoreCase(vaspSimpleName)){
            if(streamHogaDto.getMessage().contains("ping")){
                return true;
            }
        }

        return false;
    }



    public String createWebSocketPongResponse(StreamHogaDto streamHogaDto){
        String result = "";
        String vaspSimpleName = streamHogaDto.getVaspSimpleName();

        try {
            switch (vaspSimpleName.toUpperCase()) {
                case BINANCE -> {
                }
                case PHEMEX -> {
                }
                case HUOBI -> {
                    WebSocketPingHuobi webSocketPingHuobi = mapper.readValue(streamHogaDto.getMessage(), WebSocketPingHuobi.class);

                    WebSocketPongHuobi webSocketPongHuobi = WebSocketPongHuobi.builder()
                            .pong(webSocketPingHuobi.getPing())
                            .build();

                    result = mapper.writeValueAsString(webSocketPongHuobi);
                }
            }
        } catch (JsonProcessingException e) {
            result = "";
        }

        return result;
    }

    public String covertWebSocketOriginMessageToString(Object originMessage, String vaspSimpleName){
        String message = originMessage+"";
        if(BINANCE.equalsIgnoreCase(vaspSimpleName)){

        }
        else if(PHEMEX.equalsIgnoreCase(vaspSimpleName)){

        }
        else if(HUOBI.equalsIgnoreCase(vaspSimpleName)){
            if(originMessage instanceof ByteBuffer byteBuffer){
                byte[] compressedData = byteBuffer.array();
                message = decompress(compressedData);
            }
        }

        return message;
    }

    public void updateLiveWebSocket(String threadKey) {
        webSocketMapper.updateLiveWebSocket(LiveStreamWebSocketDto.builder()
                .threadKey(threadKey)
                .build());
    }

    public void insertLiveWebSocket(LiveStreamWebSocketDto liveStreamWebSocketDto) {
        webSocketMapper.insertLiveWebSocket(liveStreamWebSocketDto);
    }

    public void deleteLiveWebSocket(String threadKey, MarketTrackingDto marketTrackingDto, CloseStatus closeStatus) {
        webSocketMapper.deleteLiveWebSocket(LiveStreamWebSocketDto.builder()
                .threadKey(threadKey)
                .build());

        webSocketMapper.insertWebSocketHistory(WebSocketHistoryDto.builder()
                .vaspSimpleName(marketTrackingDto.getVaspSimpleName())
                .mySymbol(marketTrackingDto.getMySymbol())
                .reason(closeStatus.toString())
                .build());
    }

    public void removeAllWebSocket() {
        webSocketMapper.deleteAllLiveWebSocket(LiveStreamWebSocketDto.builder()
                .threadIp(getHostIp())
                .build());
    }


    public boolean isWebSocketSingleConnection(MarketTrackingDto marketTrackingDto){
        boolean result = false;
        String vaspSimpleName = marketTrackingDto.getVaspSimpleName();

        switch (vaspSimpleName.toUpperCase()) {
            case BINANCE -> {
                result = false;
            }
            case PHEMEX -> {
                result = true;
            }
            case HUOBI -> {
                result = false;
            }
        }

        return result;
    }
}
