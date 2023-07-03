package kr.co.devhanjong.hjtb_coin_stream_daemon.model.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisRunningTimeDto {
    private String time;
}
