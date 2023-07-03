package kr.co.devhanjong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HjCoinStreamDaemonApplication {

    public static void main(String[] args) {
        SpringApplication.run(HjCoinStreamDaemonApplication.class, args);
    }

}
