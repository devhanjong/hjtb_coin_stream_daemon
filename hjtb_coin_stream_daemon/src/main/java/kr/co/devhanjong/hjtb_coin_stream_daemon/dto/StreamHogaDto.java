package kr.co.devhanjong.hjtb_coin_stream_daemon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreamHogaDto {
    private String streamTime;
    private String vaspSimpleName;
    private String mySymbol;
    private String message;
    private Integer sortNo;
}
