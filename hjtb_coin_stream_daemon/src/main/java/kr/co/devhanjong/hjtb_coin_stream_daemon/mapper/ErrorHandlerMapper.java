package kr.co.devhanjong.hjtb_coin_stream_daemon.mapper;

import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.HandlerErrorLogDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ErrorHandlerMapper {
    int increaseErrorCntFromVaspSimpleNameAndMySymbol(
            @Param("vaspSimpleName") String vaspSimpleName,
            @Param("mySymbol") String mySymbol);


    int insertErrorLog(@Param("handlerErrorLogDto") HandlerErrorLogDto handlerErrorLogDto);
}
