package kr.co.devhanjong.module_exception_monitoring.service;

import jakarta.validation.Valid;
import kr.co.devhanjong.module_exception_monitoring.dto.MonitoringDto;
import kr.co.devhanjong.module_exception_monitoring.mapper.MonitoringMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

// DB 테이블명
import static kr.co.devhanjong.module_exception_monitoring.config.CommonConsts.EXCEPTION_MONITORING;

@Slf4j
@RequiredArgsConstructor
@Service
public class MonitoringServiceImplException implements MonitoringService {

    private final MonitoringMapper monitoringMapper;

    // #########################################################################################
    //                                      [ PUBLIC ]
    // #########################################################################################

    /*******************************************************************************************
     * 에러 히스토리 조회 - 전체
     *******************************************************************************************/
    @Override
    public List<MonitoringDto.Select> fetchAllExceptionHistory() {
        return monitoringMapper.fetchAllExceptionHistory(EXCEPTION_MONITORING);
    }

    /*******************************************************************************************
     * 에러 히스토리 조회 - 검색
     *******************************************************************************************/
    @Override
    public List<MonitoringDto.Select> fetchExceptionHistoryBySearch(@Valid MonitoringDto.Select dto) {
        return monitoringMapper.fetchExceptionHistoryBySearch(EXCEPTION_MONITORING, dto);
    }

    /*******************************************************************************************
     * 에러 히스토리 조회 - 알람 미발송
     *******************************************************************************************/
    @Override
    public List<MonitoringDto.Select> fetchAllExceptionHistoryAlarmNotSent() {
        return monitoringMapper.fetchAllExceptionHistoryAlarmNotSent(EXCEPTION_MONITORING);
    }

    /*******************************************************************************************
     * 에러 등록
     *******************************************************************************************/
    @Override
    public void insertExceptionHistory(@Valid MonitoringDto.Insert dto) {
        monitoringMapper.insertExceptionHistory(EXCEPTION_MONITORING, dto);
    }

    /*******************************************************************************************
     * 에러 갱신 - 알람 발송 유무
     *******************************************************************************************/
    @Override
    public void updateExceptionHistoryByAlarmSent(long idx) {
        monitoringMapper.updateExceptionHistoryByAlarmSent(EXCEPTION_MONITORING, idx);
    }

    /*******************************************************************************************
     * INDEX 생성 ( alarm_yn, app_name, level )
     *******************************************************************************************/
    @Override
    public void createIndex() {
        monitoringMapper.createAlarmIndex(EXCEPTION_MONITORING);
        monitoringMapper.createAppNameIndex(EXCEPTION_MONITORING);
        monitoringMapper.createLevelIndex(EXCEPTION_MONITORING);
    }

    /*******************************************************************************************
     * INDEX 삭제 ( alarm_yn, app_name, level )
     *******************************************************************************************/
    @Override
    public void dropIndex() {
        monitoringMapper.dropAlarmIndex(EXCEPTION_MONITORING);
        monitoringMapper.dropAppNameIndex(EXCEPTION_MONITORING);
        monitoringMapper.dropLevelIndex(EXCEPTION_MONITORING);
    }


    // #########################################################################################
    //                                      [ PRIVATE ]
    // #########################################################################################

}
