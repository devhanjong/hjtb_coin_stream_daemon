package kr.co.devhanjong.hjtb_coin_stream_daemon.model.phemex;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebSocketRequestPhemex {
    private Integer id;
    private String method;
    private List<Object> params;
}
