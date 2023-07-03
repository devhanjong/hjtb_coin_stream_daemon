package kr.co.devhanjong.hjtb_coin_stream_daemon.submodule.up_error_code_module;

import org.springframework.stereotype.Component;

@Component
public class ErrorCodeApiResponseUtil {
//    /**
//     *  client Api 에서는 이 유틸로 error_code_module 통신한다.
//     */
//    private final ErrorCodeService errorCodeService;
//
//    public ErrorCodeApiResponseUtil(@Autowired ErrorCodeServiceImpl errorCodeService) {
//        this.errorCodeService = errorCodeService;
//    }
//
//    public ApiResponse getApiResponse(ErrorCodeBaseDTO errorCodeBaseDTO){
//        return new ApiResponse(errorCodeBaseDTO.getMsg(), errorCodeBaseDTO.getCode(), errorCodeBaseDTO.getHttpStatus());
//    }
//
//
//    public ApiResponse getApiResponse(String errorCode, @Nullable String ...args){
//        ErrorCodeBaseDTO formatErrorCode = errorCodeService.getFormatErrorCode(errorCode, args);
//        return new ApiResponse(formatErrorCode.getMsg(), formatErrorCode.getCode(), formatErrorCode.getHttpStatus());
//    }
//
//    public ApiResponse getApiResponse(Object data, String errorCode, @Nullable String ...args){
//        ErrorCodeBaseDTO formatErrorCode = errorCodeService.getFormatErrorCode(errorCode, args);
//        return new ApiResponse(formatErrorCode.getMsg(), formatErrorCode.getCode(), formatErrorCode.getHttpStatus(), data);
//    }
//
//
//    public ErrorCodeBaseDTO getFormatErrorCode(String code, @Nullable String ...args){
//        return errorCodeService.getFormatErrorCode(code, args);
//    }
}
