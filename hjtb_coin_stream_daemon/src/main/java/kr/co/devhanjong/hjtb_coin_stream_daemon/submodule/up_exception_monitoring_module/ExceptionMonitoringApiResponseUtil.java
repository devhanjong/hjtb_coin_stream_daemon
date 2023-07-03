package kr.co.devhanjong.hjtb_coin_stream_daemon.submodule.up_exception_monitoring_module;

import kr.co.devhanjong.module_exception_monitoring.dto.MonitoringDto;
import kr.co.devhanjong.module_exception_monitoring.service.MonitoringService;
import kr.co.devhanjong.module_exception_monitoring.service.MonitoringServiceImplDebug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExceptionMonitoringApiResponseUtil {
    private final MonitoringService monitoringService;

    public ExceptionMonitoringApiResponseUtil(@Autowired MonitoringServiceImplDebug monitoringService) {
        this.monitoringService = monitoringService;
    }

    public void insertMonitoring(String level, String detail, String message){

        MonitoringDto.Insert monitoringInsert = MonitoringDto.Insert.builder()
                .appName("stream_daemon")
                .level(level)
                .status("N")
                .detail(detail)
                .message(message)
                .build();
        monitoringService.insertExceptionHistory(monitoringInsert);
    }
}
