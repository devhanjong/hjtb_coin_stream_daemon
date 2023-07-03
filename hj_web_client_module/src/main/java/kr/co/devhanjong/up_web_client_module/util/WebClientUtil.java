package kr.co.devhanjong.up_web_client_module.util;

import kr.co.devhanjong.up_web_client_module.Exception.WebClientException;
import kr.co.devhanjong.up_web_client_module.config.WebClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

@Component
public class WebClientUtil {

    private final WebClientConfig webClientConfig;

    public WebClientUtil(@Autowired WebClientConfig webClientConfig) {
        this.webClientConfig = webClientConfig;
    }

    public <T> T get(String url, Class<T> responseDtoClass) {
        return webClientConfig.webClient().method(HttpMethod.GET)
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new WebClientException(error))))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new WebClientException(error))))
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new WebClientException("기타 에러")))
                .bodyToMono(responseDtoClass)
                .onErrorResume(WebClientRequestException.class, e -> Mono.error(new WebClientException("서버로 부터 응답이 없습니다. " + e)))
                .block();
    }

    public <T, V> T post(String url, V requestDto, Class<T> responseDtoClass) {
        return webClientConfig.webClient().method(HttpMethod.POST)
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new WebClientException(error))))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new WebClientException(error))))
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new WebClientException("기타 에러")))
                .bodyToMono(responseDtoClass)
                .onErrorResume(WebClientRequestException.class, e -> Mono.error(new WebClientException("서버로 부터 응답이 없습니다. " + e)))
                .block();
    }

    public <T, V> T postFile(String url, V requestDto, Class<T> responseDtoClass) {
        return webClientConfig.webClient().method(HttpMethod.POST)
                .uri(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(requestDto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new WebClientException(error))))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> clientResponse.bodyToMono(String.class).flatMap(error -> Mono.error(new WebClientException(error))))
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new WebClientException("기타 에러")))
                .bodyToMono(responseDtoClass)
                .onErrorResume(WebClientRequestException.class, e -> Mono.error(new WebClientException("서버로 부터 응답이 없습니다. " + e)))
                .block();
    }
}
