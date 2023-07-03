package kr.co.devhanjong.hjtb_coin_stream_daemon.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.CoinHogaTempDto;
import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.HandlerErrorLogDto;
import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.MarketTrackingDto;
import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.VaspApiEndPointDto;
import kr.co.devhanjong.hjtb_coin_stream_daemon.model.binance.DepthResponseBinance;
import kr.co.devhanjong.hjtb_coin_stream_daemon.model.huobi.DepthResponseHoubi;
import kr.co.devhanjong.hjtb_coin_stream_daemon.model.phemex.DepthResponsePhemex;
import kr.co.devhanjong.hjtb_coin_stream_daemon.service.ErrorHandlerService;
import kr.co.devhanjong.up_web_client_module.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static kr.co.devhanjong.hjtb_coin_stream_daemon.config.Const.*;
import static kr.co.devhanjong.hjtb_coin_stream_daemon.model.ApiResponse.mapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaspCallUtil {

    private final ErrorHandlerService errorHandlerService;
    private final WebClientUtil webClientUtil;

    public void sendVaspCall(MarketTrackingDto marketTrackingDto, List<VaspApiEndPointDto> vaspApiEndPointDtoList, String path, List<CoinHogaTempDto> coinHogaTempDtoList) {
        // 엔드포인트 DTO 를 찾는다.
        VaspApiEndPointDto vaspApiEndPointDto = vaspApiEndPointDtoList.stream()
                .filter(x -> x.getVaspSimpleName().equalsIgnoreCase(marketTrackingDto.getVaspSimpleName()))
                .findFirst()
                .orElseThrow(RuntimeException::new);

        // URI 빌드한다.
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromUriString(buildApiUrl(vaspApiEndPointDto, path));

        buildApiQueryParam(uriComponentsBuilder, vaspApiEndPointDto, marketTrackingDto, path);

        // 전송한다. 무조건 get만 할거임
        String callResponse = webClientUtil.get(uriComponentsBuilder.toUriString(), String.class);

        // 결과를 파싱해서 리턴
        try {
            coinHogaTempDtoList.add(parseResponse(callResponse, path, marketTrackingDto));
        } catch (RuntimeException | JsonProcessingException e) {
            log.error("Api Response parse 실패");

            // 에러 핸들링
            errorHandlerService.handlerErrorLog(HandlerErrorLogDto.builder()
                    .vaspSimpleName(marketTrackingDto.getVaspSimpleName())
                    .mySymbol(marketTrackingDto.getMySymbol())
                    .memo("sendVaspCall " + path + " Api Response parse 실패")
                    .errorMsg(ExceptionUtils.getStackTrace(e))
                    .responseMsg(callResponse)
                    .build());
        }
    }

    private String buildApiUrl(VaspApiEndPointDto vaspApiEndPointDto, String path){
        return switch (path) {
            case PATH_DEPTH -> vaspApiEndPointDto.getEndPoint() + vaspApiEndPointDto.getPathDepth();
            case PATH_HEALTH -> vaspApiEndPointDto.getEndPoint() + vaspApiEndPointDto.getPathHealth();
            default -> throw new IllegalArgumentException("Invalid Value path : " + path );
        };
    }

    private void buildApiQueryParam(UriComponentsBuilder uriComponentsBuilder, VaspApiEndPointDto vaspApiEndPointDto, MarketTrackingDto marketTrackingDto, String path){
        String vaspSimpleName = vaspApiEndPointDto.getVaspSimpleName();
        if(vaspSimpleName.equalsIgnoreCase(BINANCE)){
            switch (path) {
                case PATH_DEPTH -> uriComponentsBuilder.queryParam("symbol", marketTrackingDto.getVaspSymbol()).queryParam("limit", 5);
//                case PATH_HEALTH -> vaspApiEndPointDto.getEndPoint() + vaspApiEndPointDto.getPathHealth();
                default -> throw new IllegalArgumentException("Invalid Value path : " + path );
            };
        }
        else if(vaspSimpleName.equalsIgnoreCase(HUOBI)){
            switch (path) {
                case PATH_DEPTH -> uriComponentsBuilder.queryParam("symbol", marketTrackingDto.getVaspSymbol()).queryParam("depth", 5).queryParam("type", "step0");
//                case PATH_HEALTH -> vaspApiEndPointDto.getEndPoint() + vaspApiEndPointDto.getPathHealth();
                default -> throw new IllegalArgumentException("Invalid Value path : " + path );
            };
        }
        else if(vaspSimpleName.equalsIgnoreCase(PHEMEX)){
            switch (path) {
                case PATH_DEPTH -> uriComponentsBuilder.queryParam("symbol", marketTrackingDto.getVaspSymbol());
//                case PATH_HEALTH -> vaspApiEndPointDto.getEndPoint() + vaspApiEndPointDto.getPathHealth();
                default -> throw new IllegalArgumentException("Invalid Value path : " + path );
            };
        }
    }

    private CoinHogaTempDto parseResponse(String response, String path, MarketTrackingDto marketTrackingDto) throws JsonProcessingException {
        String vaspSimpleName = marketTrackingDto.getVaspSimpleName();
        CoinHogaTempDto coinHogaTempDto = new CoinHogaTempDto();
        coinHogaTempDto.setMySymbol(marketTrackingDto.getMySymbol());

        if(vaspSimpleName.equalsIgnoreCase(BINANCE)){
            coinHogaTempDto.setVaspSimpleName(BINANCE.toLowerCase());

            if(path.equalsIgnoreCase(PATH_DEPTH)){
                DepthResponseBinance depthResponseBinance = mapper.readValue(response, DepthResponseBinance.class);
                for (int i = 0; i < 5; i++) {
                    switch (i) {
                        case 0 -> {
                            coinHogaTempDto.setBid1Price(depthResponseBinance.getBids().get(i).get(0));
                            coinHogaTempDto.setBid1Qty(depthResponseBinance.getBids().get(i).get(1));
                            coinHogaTempDto.setAsk1Price(depthResponseBinance.getAsks().get(i).get(0));
                            coinHogaTempDto.setAsk1Qty(depthResponseBinance.getAsks().get(i).get(1));
                        }
                        case 1 -> {
                            coinHogaTempDto.setBid2Price(depthResponseBinance.getBids().get(i).get(0));
                            coinHogaTempDto.setBid2Qty(depthResponseBinance.getBids().get(i).get(1));
                            coinHogaTempDto.setAsk2Price(depthResponseBinance.getAsks().get(i).get(0));
                            coinHogaTempDto.setAsk2Qty(depthResponseBinance.getAsks().get(i).get(1));
                        }
                        case 2 -> {
                            coinHogaTempDto.setBid3Price(depthResponseBinance.getBids().get(i).get(0));
                            coinHogaTempDto.setBid3Qty(depthResponseBinance.getBids().get(i).get(1));
                            coinHogaTempDto.setAsk3Price(depthResponseBinance.getAsks().get(i).get(0));
                            coinHogaTempDto.setAsk3Qty(depthResponseBinance.getAsks().get(i).get(1));
                        }
                        case 3 -> {
                            coinHogaTempDto.setBid4Price(depthResponseBinance.getBids().get(i).get(0));
                            coinHogaTempDto.setBid4Qty(depthResponseBinance.getBids().get(i).get(1));
                            coinHogaTempDto.setAsk4Price(depthResponseBinance.getAsks().get(i).get(0));
                            coinHogaTempDto.setAsk4Qty(depthResponseBinance.getAsks().get(i).get(1));
                        }
                        case 4 -> {
                            coinHogaTempDto.setBid5Price(depthResponseBinance.getBids().get(i).get(0));
                            coinHogaTempDto.setBid5Qty(depthResponseBinance.getBids().get(i).get(1));
                            coinHogaTempDto.setAsk5Price(depthResponseBinance.getAsks().get(i).get(0));
                            coinHogaTempDto.setAsk5Qty(depthResponseBinance.getAsks().get(i).get(1));
                        }
                    };
                }
            }
        }
        else if(vaspSimpleName.equalsIgnoreCase(HUOBI)){
            coinHogaTempDto.setVaspSimpleName(HUOBI.toLowerCase());

            if(path.equalsIgnoreCase(PATH_DEPTH)){
                DepthResponseHoubi depthResponseHoubi = mapper.readValue(response, DepthResponseHoubi.class);
                for (int i = 0; i < 5; i++) {
                    switch (i) {
                        case 0 -> {
                            coinHogaTempDto.setBid1Price(depthResponseHoubi.getTick().getBids().get(i).get(0));
                            coinHogaTempDto.setBid1Qty(depthResponseHoubi.getTick().getBids().get(i).get(1));
                            coinHogaTempDto.setAsk1Price(depthResponseHoubi.getTick().getAsks().get(i).get(0));
                            coinHogaTempDto.setAsk1Qty(depthResponseHoubi.getTick().getAsks().get(i).get(1));
                        }
                        case 1 -> {
                            coinHogaTempDto.setBid2Price(depthResponseHoubi.getTick().getBids().get(i).get(0));
                            coinHogaTempDto.setBid2Qty(depthResponseHoubi.getTick().getBids().get(i).get(1));
                            coinHogaTempDto.setAsk2Price(depthResponseHoubi.getTick().getAsks().get(i).get(0));
                            coinHogaTempDto.setAsk2Qty(depthResponseHoubi.getTick().getAsks().get(i).get(1));
                        }
                        case 2 -> {
                            coinHogaTempDto.setBid3Price(depthResponseHoubi.getTick().getBids().get(i).get(0));
                            coinHogaTempDto.setBid3Qty(depthResponseHoubi.getTick().getBids().get(i).get(1));
                            coinHogaTempDto.setAsk3Price(depthResponseHoubi.getTick().getAsks().get(i).get(0));
                            coinHogaTempDto.setAsk3Qty(depthResponseHoubi.getTick().getAsks().get(i).get(1));
                        }
                        case 3 -> {
                            coinHogaTempDto.setBid4Price(depthResponseHoubi.getTick().getBids().get(i).get(0));
                            coinHogaTempDto.setBid4Qty(depthResponseHoubi.getTick().getBids().get(i).get(1));
                            coinHogaTempDto.setAsk4Price(depthResponseHoubi.getTick().getAsks().get(i).get(0));
                            coinHogaTempDto.setAsk4Qty(depthResponseHoubi.getTick().getAsks().get(i).get(1));
                        }
                        case 4 -> {
                            coinHogaTempDto.setBid5Price(depthResponseHoubi.getTick().getBids().get(i).get(0));
                            coinHogaTempDto.setBid5Qty(depthResponseHoubi.getTick().getBids().get(i).get(1));
                            coinHogaTempDto.setAsk5Price(depthResponseHoubi.getTick().getAsks().get(i).get(0));
                            coinHogaTempDto.setAsk5Qty(depthResponseHoubi.getTick().getAsks().get(i).get(1));
                        }
                    }
                }
            }
        }
        else if(vaspSimpleName.equalsIgnoreCase(PHEMEX)){
            coinHogaTempDto.setVaspSimpleName(PHEMEX.toLowerCase());

            if(path.equalsIgnoreCase(PATH_DEPTH)){
                DepthResponsePhemex depthResponsePhemex = mapper.readValue(response, DepthResponsePhemex.class);
                for (int i = 0; i < 5; i++) {
                    switch (i) {
                        case 0 -> {
                            coinHogaTempDto.setBid1Price(depthResponsePhemex.getResult().getOrderbook_p().getBids().get(i).get(0));
                            coinHogaTempDto.setBid1Qty(depthResponsePhemex.getResult().getOrderbook_p().getBids().get(i).get(1));
                            coinHogaTempDto.setAsk1Price(depthResponsePhemex.getResult().getOrderbook_p().getAsks().get(i).get(0));
                            coinHogaTempDto.setAsk1Qty(depthResponsePhemex.getResult().getOrderbook_p().getAsks().get(i).get(1));
                        }
                        case 1 -> {
                            coinHogaTempDto.setBid2Price(depthResponsePhemex.getResult().getOrderbook_p().getBids().get(i).get(0));
                            coinHogaTempDto.setBid2Qty(depthResponsePhemex.getResult().getOrderbook_p().getBids().get(i).get(1));
                            coinHogaTempDto.setAsk2Price(depthResponsePhemex.getResult().getOrderbook_p().getAsks().get(i).get(0));
                            coinHogaTempDto.setAsk2Qty(depthResponsePhemex.getResult().getOrderbook_p().getAsks().get(i).get(1));
                        }
                        case 2 -> {
                            coinHogaTempDto.setBid3Price(depthResponsePhemex.getResult().getOrderbook_p().getBids().get(i).get(0));
                            coinHogaTempDto.setBid3Qty(depthResponsePhemex.getResult().getOrderbook_p().getBids().get(i).get(1));
                            coinHogaTempDto.setAsk3Price(depthResponsePhemex.getResult().getOrderbook_p().getAsks().get(i).get(0));
                            coinHogaTempDto.setAsk3Qty(depthResponsePhemex.getResult().getOrderbook_p().getAsks().get(i).get(1));
                        }
                        case 3 -> {
                            coinHogaTempDto.setBid4Price(depthResponsePhemex.getResult().getOrderbook_p().getBids().get(i).get(0));
                            coinHogaTempDto.setBid4Qty(depthResponsePhemex.getResult().getOrderbook_p().getBids().get(i).get(1));
                            coinHogaTempDto.setAsk4Price(depthResponsePhemex.getResult().getOrderbook_p().getAsks().get(i).get(0));
                            coinHogaTempDto.setAsk4Qty(depthResponsePhemex.getResult().getOrderbook_p().getAsks().get(i).get(1));
                        }
                        case 4 -> {
                            coinHogaTempDto.setBid5Price(depthResponsePhemex.getResult().getOrderbook_p().getBids().get(i).get(0));
                            coinHogaTempDto.setBid5Qty(depthResponsePhemex.getResult().getOrderbook_p().getBids().get(i).get(1));
                            coinHogaTempDto.setAsk5Price(depthResponsePhemex.getResult().getOrderbook_p().getAsks().get(i).get(0));
                            coinHogaTempDto.setAsk5Qty(depthResponsePhemex.getResult().getOrderbook_p().getAsks().get(i).get(1));
                        }
                    };
                }
            }
        }

        return coinHogaTempDto;

    }




    //TODO 현재 USDT 만 지원한다.
    public String convertVaspSymbolToMySymbol(String symbol, String vaspSimpleName){
        String result = "";
        symbol = symbol.trim();
        if(vaspSimpleName.equalsIgnoreCase(BINANCE)){
            if(symbol.toUpperCase().endsWith("USDT")){
                result = symbol.substring(0, symbol.length() - 4).toLowerCase() + "_" + symbol.substring(symbol.length() - 4).toLowerCase();
            }
        }
        else if(vaspSimpleName.equalsIgnoreCase(HUOBI)){
            if(symbol.toUpperCase().endsWith("USDT")){
                result = symbol.substring(0, symbol.length() - 4).toLowerCase() + "_" + symbol.substring(symbol.length() - 4).toLowerCase();
            }
        }

        return result;
    }

    //TODO 현재 USDT 만 지원한다.
    public String converMySymbolToVaspSymbol(String symbol, String vaspSimpleName){
        String result = "";
        symbol = symbol.trim();
        if(vaspSimpleName.equalsIgnoreCase(BINANCE)){
            if(symbol.toUpperCase().endsWith("USDT") && symbol.contains("_")){
                result = symbol.substring(0, symbol.indexOf("_")).toUpperCase() + symbol.substring(symbol.indexOf("_")+1).toUpperCase();
            }
        }
        else if(vaspSimpleName.equalsIgnoreCase(HUOBI)){
            if(symbol.toUpperCase().endsWith("USDT") && symbol.contains("_")){
                result = symbol.substring(0, symbol.indexOf("_")).toLowerCase() + symbol.substring(symbol.indexOf("_")+1).toLowerCase();
            }
        }

        return result;
    }
}
