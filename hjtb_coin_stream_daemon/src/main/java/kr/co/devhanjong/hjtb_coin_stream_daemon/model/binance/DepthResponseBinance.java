package kr.co.devhanjong.hjtb_coin_stream_daemon.model.binance;


import kr.co.devhanjong.hjtb_coin_stream_daemon.model.base.DepthResponseBase;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepthResponseBinance extends DepthResponseBase {
    private String lastUpdateId;
//    private String bids;
//    private String asks;


    private List<List<BigDecimal>> bids;
    private List<List<BigDecimal>> asks;
}
