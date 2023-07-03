package kr.co.devhanjong.hjtb_coin_stream_daemon.dto.function.coin_hoga_service;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class GetAvgPriceResponse {
    private BigDecimal avgPrice;
    private BigDecimal totalAmount;
}
