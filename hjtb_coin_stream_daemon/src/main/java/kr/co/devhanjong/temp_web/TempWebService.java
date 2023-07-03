package kr.co.devhanjong.temp_web;

import kr.co.devhanjong.hjtb_coin_stream_daemon.model.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TempWebService {


    private final TempWebMapper tempWebMapper;


    public ApiResponse<?> getLiveHoga(){
        return new ApiResponse("성공","0000",200, tempWebMapper.selectLiveHogaList());
    }

    public ApiResponse<?> getFakeOrder(){
        List<FakeOrderDto> fakeOrderDtos = tempWebMapper.selectFakeOrderList();
        return new ApiResponse("성공","0000",200, fakeOrderDtos);
    }
}
