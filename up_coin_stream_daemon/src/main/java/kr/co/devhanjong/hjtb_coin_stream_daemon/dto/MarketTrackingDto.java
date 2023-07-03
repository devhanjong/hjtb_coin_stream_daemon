package kr.co.devhanjong.hjtb_coin_stream_daemon.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MarketTrackingDto {
    private Integer seq;
    private String vaspSimpleName;
    private String modDate;
    private String regDate;
    private String vaspSymbol;
    private String mySymbol;
    private String status;
    private String threadKey;
    private boolean newWebSocket;
}
