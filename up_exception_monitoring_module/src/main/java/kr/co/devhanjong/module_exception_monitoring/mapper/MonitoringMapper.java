package kr.co.devhanjong.module_exception_monitoring.mapper;

import kr.co.devhanjong.module_exception_monitoring.dto.MonitoringDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MonitoringMapper {

    /*******************************************************************************************
     * 에러 히스토리 조회 - 전체
     *******************************************************************************************/
    List<MonitoringDto.Select> fetchAllExceptionHistory(@Param("tableName") String tableName);

    /*******************************************************************************************
     * 에러 히스토리 조회 - 검색
     *******************************************************************************************/
    List<MonitoringDto.Select> fetchExceptionHistoryBySearch(@Param("tableName") String tableName,
                                                             @Param("dto") MonitoringDto.Select dto);

    /*******************************************************************************************
     * 에러 히스토리 조회 - 알람 미발송
     *******************************************************************************************/
    List<MonitoringDto.Select> fetchAllExceptionHistoryAlarmNotSent(@Param("tableName") String tableName);

    /*******************************************************************************************
     * 에러 등록
     *******************************************************************************************/
    void insertExceptionHistory(@Param("tableName") String tableName,
                                @Param("dto") MonitoringDto.Insert dto);

    /*******************************************************************************************
     * 에러 갱신 - 알람 발송 유무
     *******************************************************************************************/
    void updateExceptionHistoryByAlarmSent(@Param("tableName") String tableName,
                                           @Param("idx") long idx);

    /*******************************************************************************************
     * INDEX 생성 ( alarm_yn, app_name, level )
     *******************************************************************************************/
    void createAlarmIndex(@Param("tableName") String tableName);
    void createAppNameIndex(@Param("tableName") String tableName);
    void createLevelIndex(@Param("tableName") String tableName);

    /*******************************************************************************************
     * INDEX 삭제 ( alarm_yn, app_name, level )
     *******************************************************************************************/
    void dropAlarmIndex(@Param("tableName") String tableName);
    void dropAppNameIndex(@Param("tableName") String tableName);
    void dropLevelIndex(@Param("tableName") String tableName);

}
