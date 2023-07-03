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
public class CoinHogaTempDto {
    private String mySymbol;
    private String regDate;
    private String vaspSimpleName;
    private BigDecimal bid1Price;
    private BigDecimal bid1Qty;
    private BigDecimal bid2Price;
    private BigDecimal bid2Qty;
    private BigDecimal bid3Price;
    private BigDecimal bid3Qty;
    private BigDecimal bid4Price;
    private BigDecimal bid4Qty;
    private BigDecimal bid5Price;
    private BigDecimal bid5Qty;
    private BigDecimal ask1Price;
    private BigDecimal ask1Qty;
    private BigDecimal ask2Price;
    private BigDecimal ask2Qty;
    private BigDecimal ask3Price;
    private BigDecimal ask3Qty;
    private BigDecimal ask4Price;
    private BigDecimal ask4Qty;
    private BigDecimal ask5Price;
    private BigDecimal ask5Qty;
}
