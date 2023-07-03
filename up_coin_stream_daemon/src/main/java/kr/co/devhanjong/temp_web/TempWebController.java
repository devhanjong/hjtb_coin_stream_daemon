package kr.co.devhanjong.temp_web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import static kr.co.devhanjong.hjtb_coin_stream_daemon.model.ApiResponse.returnByApiResponse;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TempWebController {

    private final TempWebService tempWebService;


    @PostMapping("/liveHoga")
    public ResponseEntity<?> liveHoga(){
        return returnByApiResponse(tempWebService.getLiveHoga());
    }


    @PostMapping("/fakeOrder")
    public ResponseEntity<?> fakeOrder(){
        return returnByApiResponse(tempWebService.getFakeOrder());
    }


}
