package kr.co.devhanjong.hjtb_coin_stream_daemon.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CoinQuoteDto {
    private String coinType;
    private BigDecimal usdt;
    private BigDecimal price;
    private String regDate;
}
