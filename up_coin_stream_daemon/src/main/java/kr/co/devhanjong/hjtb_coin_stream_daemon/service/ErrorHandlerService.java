package kr.co.devhanjong.hjtb_coin_stream_daemon.service;

import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.HandlerErrorLogDto;
import kr.co.devhanjong.hjtb_coin_stream_daemon.mapper.ErrorHandlerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ErrorHandlerService {

    private final ErrorHandlerMapper errorHandlerMapper;

    public void handlerErrorLog(HandlerErrorLogDto handlerErrorLogDto){
        // mySymbol 이 있으면 market_traking_list error를 늘린다.
        if(handlerErrorLogDto.getMySymbol() != null && !handlerErrorLogDto.getMySymbol().equalsIgnoreCase("")){
            // error_cnt++
            errorHandlerMapper.increaseErrorCntFromVaspSimpleNameAndMySymbol(handlerErrorLogDto.getVaspSimpleName(), handlerErrorLogDto.getMySymbol());
        }

        // error insert
        errorHandlerMapper.insertErrorLog(handlerErrorLogDto);
    }
}
