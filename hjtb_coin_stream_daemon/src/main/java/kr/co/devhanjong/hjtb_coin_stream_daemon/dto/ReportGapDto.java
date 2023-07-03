package kr.co.devhanjong.hjtb_coin_stream_daemon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportGapDto {
    private String regDate;
    private String aVaspSimpleName;
    private String bVaspSimpleName;
    private String mySymbol;
    private BigDecimal aBidAvgPrice;
    private BigDecimal bBidAvgPrice;
    private BigDecimal bidGap;
    private BigDecimal bidCanAmount;
    private BigDecimal aAskAvgPrice;
    private BigDecimal bAskAvgPrice;
    private BigDecimal askGap;
    private BigDecimal askCanAmount;
}
