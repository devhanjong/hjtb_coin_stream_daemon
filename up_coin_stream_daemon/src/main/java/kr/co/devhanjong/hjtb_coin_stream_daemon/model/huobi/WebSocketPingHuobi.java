package kr.co.devhanjong.hjtb_coin_stream_daemon.model.huobi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebSocketPingHuobi {
    private Long ping;
}
