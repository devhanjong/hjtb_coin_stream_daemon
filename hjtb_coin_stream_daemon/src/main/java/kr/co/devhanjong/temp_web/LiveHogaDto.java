package kr.co.devhanjong.temp_web;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LiveHogaDto {
    private String aVaspSimpleName;
    private String bVaspSimpleName;
    private String mySymbol;
    private String regDate;
    private String modDate;
    private String bidGap;
    private String guessFee;
    private String buyGap;
    private String askGap;
    private String bidCanAmount;
    private String askCanAmount;
}
