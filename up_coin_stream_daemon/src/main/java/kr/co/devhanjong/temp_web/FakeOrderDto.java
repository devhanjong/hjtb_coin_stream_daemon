package kr.co.devhanjong.temp_web;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FakeOrderDto {
    private String seq;
    private String ordNo;
    @JsonProperty("aVaspSimpleName")
    private String aVaspSimpleName;
    @JsonProperty("bVaspSimpleName")
    private String bVaspSimpleName;
    private String mySymbol;
    private String startDate;
    private String regDate;
    private String modDate;
    private BigDecimal startGap;
    private BigDecimal targetGap;
//    private BigDecimal aBidAvgPrice;
    @JsonProperty("bBidAvgPrice")
    private BigDecimal bBidAvgPrice;
    @JsonProperty("aAskAvgPrice")
    private BigDecimal aAskAvgPrice;
//    private BigDecimal bAskAvgPrice;
    @JsonProperty("aCoinAmount")
    private BigDecimal aCoinAmount;
    @JsonProperty("bCoinAmount")
    private BigDecimal bCoinAmount;
}
