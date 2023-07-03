package kr.co.devhanjong.module_exception_monitoring.service;

import jakarta.validation.Valid;
import kr.co.devhanjong.module_exception_monitoring.dto.MonitoringDto;
import kr.co.devhanjong.module_exception_monitoring.mapper.MonitoringMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

// DB 테이블명
import static kr.co.devhanjong.module_exception_monitoring.config.CommonConsts.SYSTEM_MONITORING;

@Slf4j
@RequiredArgsConstructor
@Service
public class MonitoringServiceImplSystem implements MonitoringService {

    private final MonitoringMapper monitoringMapper;

    // #########################################################################################
    //                                      [ PUBLIC ]
    // #########################################################################################

    /*******************************************************************************************
     * 에러 히스토리 조회 - 전체
     *******************************************************************************************/
    @Override
    public List<MonitoringDto.Select> fetchAllExceptionHistory() {
        return monitoringMapper.fetchAllExceptionHistory(SYSTEM_MONITORING);
    }

    /*******************************************************************************************
     * 에러 히스토리 조회 - 검색
     *******************************************************************************************/
    @Override
    public List<MonitoringDto.Select> fetchExceptionHistoryBySearch(@Valid MonitoringDto.Select dto) {
        return monitoringMapper.fetchExceptionHistoryBySearch(SYSTEM_MONITORING, dto);
    }

    /*******************************************************************************************
     * 에러 히스토리 조회 - 알람 미발송
     *******************************************************************************************/
    @Override
    public List<MonitoringDto.Select> fetchAllExceptionHistoryAlarmNotSent() {
        return monitoringMapper.fetchAllExceptionHistoryAlarmNotSent(SYSTEM_MONITORING);
    }

    /*******************************************************************************************
     * 에러 등록
     *******************************************************************************************/
    @Override
    public void insertExceptionHistory(@Valid MonitoringDto.Insert dto) {
        monitoringMapper.insertExceptionHistory(SYSTEM_MONITORING, dto);
    }

    /*******************************************************************************************
     * 에러 갱신 - 알람 발송 유무
     *******************************************************************************************/
    @Override
    public void updateExceptionHistoryByAlarmSent(long idx) {
        monitoringMapper.updateExceptionHistoryByAlarmSent(SYSTEM_MONITORING, idx);
    }

    /*******************************************************************************************
     * INDEX 생성 ( alarm_yn, app_name, level )
     *******************************************************************************************/
    @Override
    public void createIndex() {
        monitoringMapper.createAlarmIndex(SYSTEM_MONITORING);
        monitoringMapper.createAppNameIndex(SYSTEM_MONITORING);
        monitoringMapper.createLevelIndex(SYSTEM_MONITORING);
    }

    /*******************************************************************************************
     * INDEX 삭제 ( alarm_yn, app_name, level )
     *******************************************************************************************/
    @Override
    public void dropIndex() {
        monitoringMapper.dropAlarmIndex(SYSTEM_MONITORING);
        monitoringMapper.dropAppNameIndex(SYSTEM_MONITORING);
        monitoringMapper.dropLevelIndex(SYSTEM_MONITORING);
    }


    // #########################################################################################
    //                                      [ PRIVATE ]
    // #########################################################################################

}
