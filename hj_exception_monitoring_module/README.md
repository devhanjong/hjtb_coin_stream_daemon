# java-module-exception-monitoring
@ControllerAdvice, @ExceptionHandler 에서 확인되는 에러 관리 모듈

## 실행환경
+ 프로그래밍 언어 : Java 17.0.7
+ 프레임워크 : Spring Boot 3.0.5
+ 저장소 관리 : Gradle 7.4


## 프로젝트 구성
```shell
module-exception-monitoring
└── src
    ├── main.java
    │   └── kr.co.ejyang
    │       └── module_exception_monitoring
    │           ├── config
    │           │   └── CommonConsts.java    
    │           ├── dto
    │           │   └── MonitoringDto.java
    │           ├── mapper
    │           │   └── MonitoringMapper.java
    │           └── service
    │               ├── MonitoringService.java
    │               ├── MonitoringServiceImplDebug.java
    │               ├── MonitoringServiceImplException.java
    │               └── MonitoringServiceImplSystem.java
    └── resources
        └── sql
            └── monitoring_SQL.xml
```


## 호출 서비스
+ fetchAllExceptionHistory : 전체 조회
+ fetchExceptionHistoryBySearch : 검색 리스트
+ fetchAllExceptionHistoryAlarmNotSent : 알람 미발송 리스트
+ insertExceptionHistory : 에러 등록
+ updateExceptionHistoryByAlarmSent : 에러 갱신 (알람 발송 시, 발송 일자/유무 변경 )
+ createIndex : INDEX 생성 ( alarm_yn, app_name, level )
+ dropIndex : INDEX 삭제 ( alarm_yn, app_name, level )


## 멀티모듈 사용시 참고사항
- 모니터링 유형에 따라 MonitoringService 구현체 개별 호출/사용 ( Debug, Exception, System )
- 베이스가 되는 프로젝트 build.gradle에 아래 설정 추가 ( SQL Resources 등록 )

```gradle
project(':module_exception_monitoring') {
    sourceSets {
        main {
            resources {
                srcDirs "src/main/resources/sql"
            }
        }
    }
}
```

## 테이블 DDL

```sql
CREATE TABLE `debug_monitoring` (
  `idx` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(20) NOT NULL,
  `level` varchar(10) NOT NULL,
  `status` varchar(3) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `detail` varchar(100) DEFAULT NULL,
  `message` text DEFAULT NULL,
  `alarm_yn` char(1) DEFAULT NULL DEFAULT 'N',
  `reg_date` datetime DEFAULT NULL,
  `alarm_date` datetime DEFAULT NULL,
  PRIMARY KEY (`idx`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```

### 예외 코드 모니터링
```sql
CREATE TABLE `exception_monitoring` (
  `idx` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(20) NOT NULL,
  `level` varchar(10) NOT NULL,
  `status` varchar(3) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `detail` varchar(100) DEFAULT NULL,
  `message` text DEFAULT NULL,
  `alarm_yn` char(1) DEFAULT NULL DEFAULT 'N',
  `reg_date` datetime DEFAULT NULL,
  `alarm_date` datetime DEFAULT NULL,
  PRIMARY KEY (`idx`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```

### 시스템 에러 모니터링
```sql
CREATE TABLE `system_monitoring` (
 `idx` bigint(20) NOT NULL AUTO_INCREMENT,
 `app_name` varchar(20) NOT NULL,
 `level` varchar(10) NOT NULL,
 `status` varchar(3) DEFAULT NULL,
 `type` varchar(50) DEFAULT NULL,
 `detail` varchar(100) DEFAULT NULL,
 `message` text DEFAULT NULL,
 `alarm_yn` char(1) DEFAULT NULL DEFAULT 'N',
 `reg_date` datetime DEFAULT NULL,
 `alarm_date` datetime DEFAULT NULL,
 PRIMARY KEY (`idx`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```
