package kr.co.devhanjong.hjtb_coin_stream_daemon.model.huobi;

import kr.co.devhanjong.hjtb_coin_stream_daemon.model.base.DepthResponseBase;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepthResponseHoubi extends DepthResponseBase {
    private String ch;
    private String status;
    private String ts;
    private DepthResponseTick tick;
}
