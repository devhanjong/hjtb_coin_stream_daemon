package kr.co.devhanjong.up_web_client_module.Exception;

public class WebClientException extends RuntimeException{

    public WebClientException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
