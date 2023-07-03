package kr.co.devhanjong.hjtb_coin_stream_daemon.model.huobi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepthResponseTick {
    private String ts;
    private String version;
    private List<List<BigDecimal>> bids;
    private List<List<BigDecimal>> asks;
}
