package kr.co.devhanjong.module_exception_monitoring.service;

import jakarta.validation.Valid;
import kr.co.devhanjong.module_exception_monitoring.dto.MonitoringDto;
import kr.co.devhanjong.module_exception_monitoring.mapper.MonitoringMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

// DB 테이블명
import static kr.co.devhanjong.module_exception_monitoring.config.CommonConsts.DEBUG_MONITORING;

@Slf4j
@RequiredArgsConstructor
@Service
public class MonitoringServiceImplDebug implements MonitoringService {

    private final MonitoringMapper monitoringMapper;

    // #########################################################################################
    //                                      [ PUBLIC ]
    // #########################################################################################

    /*******************************************************************************************
     * 에러 히스토리 조회 - 전체
     *******************************************************************************************/
    @Override
    public List<MonitoringDto.Select> fetchAllExceptionHistory() {
        return monitoringMapper.fetchAllExceptionHistory(DEBUG_MONITORING);
    }

    /*******************************************************************************************
     * 에러 히스토리 조회 - 검색
     *******************************************************************************************/
    @Override
    public List<MonitoringDto.Select> fetchExceptionHistoryBySearch(@Valid MonitoringDto.Select dto) {
        return monitoringMapper.fetchExceptionHistoryBySearch(DEBUG_MONITORING, dto);
    }

    /*******************************************************************************************
     * 에러 히스토리 조회 - 알람 미발송
     *******************************************************************************************/
    @Override
    public List<MonitoringDto.Select> fetchAllExceptionHistoryAlarmNotSent() {
        return monitoringMapper.fetchAllExceptionHistoryAlarmNotSent(DEBUG_MONITORING);
    }

    /*******************************************************************************************
     * 에러 등록
     *******************************************************************************************/
    @Override
    public void insertExceptionHistory(@Valid MonitoringDto.Insert dto) {
        monitoringMapper.insertExceptionHistory(DEBUG_MONITORING, dto);
    }

    /*******************************************************************************************
     * 에러 갱신 - 알람 발송 유무
     *******************************************************************************************/
    @Override
    public void updateExceptionHistoryByAlarmSent(long idx) {
        monitoringMapper.updateExceptionHistoryByAlarmSent(DEBUG_MONITORING, idx);
    }

    /*******************************************************************************************
     * INDEX 생성 ( alarm_yn, app_name, level )
     *******************************************************************************************/
    @Override
    public void createIndex() {
        monitoringMapper.createAlarmIndex(DEBUG_MONITORING);
        monitoringMapper.createAppNameIndex(DEBUG_MONITORING);
        monitoringMapper.createLevelIndex(DEBUG_MONITORING);
    }

    /*******************************************************************************************
     * INDEX 삭제 ( alarm_yn, app_name, level )
     *******************************************************************************************/
    @Override
    public void dropIndex() {
        monitoringMapper.dropAlarmIndex(DEBUG_MONITORING);
        monitoringMapper.dropAppNameIndex(DEBUG_MONITORING);
        monitoringMapper.dropLevelIndex(DEBUG_MONITORING);
    }


    // #########################################################################################
    //                                      [ PRIVATE ]
    // #########################################################################################

}
