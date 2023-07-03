package kr.co.devhanjong.hjtb_coin_stream_daemon.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebSocketHistoryDto {
    private String vaspSimpleName;
    private String mySymbol;
    private String reason;
    private String regDate;
}
