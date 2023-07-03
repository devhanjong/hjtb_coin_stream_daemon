package kr.co.devhanjong.hjtb_coin_stream_daemon.config.profile;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile(value="local")
@PropertySource(value = "classpath:/resources-local/application.properties",ignoreResourceNotFound = true)
@PropertySource(value = "classpath:/application.properties", ignoreResourceNotFound = true)
public class ProfileLocal {
}
