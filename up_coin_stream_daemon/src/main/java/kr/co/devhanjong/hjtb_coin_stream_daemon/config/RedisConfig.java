package kr.co.devhanjong.hjtb_coin_stream_daemon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Value("${redis.hostName}")
    private String ip;

    @Value("${redis.port}")
    private int port;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        template.setEnableDefaultSerializer(false);
        template.setEnableTransactionSupport(true);
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(ip, port);
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(1000);
        jedisPoolConfig.setMinIdle(1000);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        // 커넥션 풀의 최대 생성 연결 값을 설정한다. (기본값은 8)
        jedisPoolConfig.setMaxTotal(1000);

        // 커넥션 풀이 가득 찼을 경우 준비된 연결이 도착하기를 기다린다.
        jedisPoolConfig.setBlockWhenExhausted(true);

        // 커넥션 풀이 가득 찼을 경우 새로운 연결을 기다리지 않고 NoSuchElementException 예외를 발생시킨다.
        jedisPoolConfig.setBlockWhenExhausted(false);
        return jedisPoolConfig;
    }
}
