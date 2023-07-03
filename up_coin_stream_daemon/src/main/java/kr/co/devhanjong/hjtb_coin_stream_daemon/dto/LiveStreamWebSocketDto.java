package kr.co.devhanjong.hjtb_coin_stream_daemon.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LiveStreamWebSocketDto {
    private String threadKey;
    private String threadIp;
    private String vaspSimpleName;
    private String mySymbol;
    private String modDate;
    private String regDate;
    private String status;
    private Integer errorCnt;
}
