package kr.co.devhanjong.hjtb_coin_stream_daemon.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VaspListDto {
    private Integer seq;
    private String health;
    private String country;
    private String vasp;
    private String vaspSimpleName;
    private String pubKey;
    private String expireDate;
    private String sort;
    private String modDate;
    private String regDate;
}
