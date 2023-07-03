package kr.co.devhanjong.temp_web;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TempWebMapper {

    List<LiveHogaDto> selectLiveHogaList();
    List<FakeOrderDto> selectFakeOrderList();

}
