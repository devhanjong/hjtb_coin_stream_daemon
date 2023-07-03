package kr.co.devhanjong.module_exception_monitoring.config;

public interface CommonConsts {

    // Exception 레벨 상수
    public static final String DEBUG    = "DEBUG";
    public static final String INFO     = "INFO";
    public static final String WARN     = "WARN";
    public static final String ERROR    = "ERROR";
    public static final String FATAL    = "FATAL";

    // Exception 레벨 상수 그룹
    public static final String[] LEVEL_LIST    = { DEBUG, INFO, WARN, ERROR, FATAL };

    // Monitoring 테이블 타입
    public static final String EXCEPTION_MONITORING     = "exception_monitoring";   // 코드 에러
    public static final String SYSTEM_MONITORING        = "system_monitoring";      // 시스템 에러
    public static final String DEBUG_MONITORING         = "debug_monitoring";       // 디버깅

}
