package kr.co.devhanjong.hjtb_coin_stream_daemon.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HandlerErrorLogDto {
    private String vaspSimpleName;

    @Nullable
    private String mySymbol;

    @Nullable
    private String memo;

    private String errorMsg;
    private String responseMsg;
    private String regDate;
}
