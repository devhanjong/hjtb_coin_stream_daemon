package kr.co.devhanjong.hjtb_coin_stream_daemon.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VaspApiEndPointDto {
    private String vaspSimpleName;
    private String modDate;
    private String regDate;
    private String endPoint;
    private String pathHealth;
    private String pathDepth;
}
