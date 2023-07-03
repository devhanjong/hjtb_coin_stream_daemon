package kr.co.devhanjong.hjtb_coin_stream_daemon.mapper;

import jakarta.annotation.Nullable;
import kr.co.devhanjong.hjtb_coin_stream_daemon.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CoinHogaMapper {
    int insertCoinHoga(@Param("coinHogaTempDto") CoinHogaTempDto coinHogaTempDto, @Param("regDate") String regDate);

    List<VaspListDto> selectVaspList(@Param("health") @Nullable String health);

    List<MarketTrackingDto> selectSymbolFromTrackingList();

    List<VaspApiEndPointDto> selectVaspEndPoint();

    int insertReportGap(@Param("reportGapDto") ReportGapDto reportGapDto);
    int insertLiveHoga(@Param("reportGapDto") ReportGapDto reportGapDto);
}
