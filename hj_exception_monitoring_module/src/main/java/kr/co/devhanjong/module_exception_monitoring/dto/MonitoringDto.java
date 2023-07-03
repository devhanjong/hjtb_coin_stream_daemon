package kr.co.devhanjong.module_exception_monitoring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Date;

public class MonitoringDto {

    // 조회
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Select {
        @Positive(message = "잘못된 idx 입니다.")
        public long idx;
        public String appName;
        public String level;
        public String status;
        public String type;
        public String detail;
        public String message;
        public String alarmYn;
        public Date regDate;
        public Date alarmDate;
        public String tableName;
    }

    // 등록
    @Builder
    @AllArgsConstructor
    public static class Insert {
        @NotBlank(message = "APP 이름은 필수값입니다.")
        public String appName;
        @NotBlank(message = "에러 레벨은 필수값입니다.")
        public String level;
        public String status;
        public String type;
        public String detail;
        public String message;
        public String tableName;
    }

    // 갱신
    @Builder
    @AllArgsConstructor
    public static class Update {
        @NotBlank(message = "잘못된 idx 입니다.")
        @Positive(message = "잘못된 idx 입니다.")
        public long idx;
        public String tableName;
    }

}
