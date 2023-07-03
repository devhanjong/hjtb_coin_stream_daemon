package kr.co.devhanjong.hjtb_coin_stream_daemon.util;

import kr.co.devhanjong.hjtb_coin_stream_daemon.model.redis.RedisRunningTimeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import static kr.co.devhanjong.hjtb_coin_stream_daemon.model.ApiResponse.mapper;

@Slf4j
@Service("redisUtil")
public class RedisUtil {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public String makeStreamRunningTimeKey(String vaspSimpleName, String mySymbol){
        return vaspSimpleName + "::" + mySymbol + "::streamRunningTime";
    }

    public String makeOriginHogaTimeKey(String vaspSimpleName, String mySymbol){
        return vaspSimpleName + "::" + mySymbol + "::time";
    }

    public RedisRunningTimeDto getRedisDataOriginHogaTime(String vaspSimpleName, String mySymbol) {
        String redisKey =  makeOriginHogaTimeKey(vaspSimpleName, mySymbol);

        try {
            if(redisTemplate.hasKey(redisKey)) {
                String data = redisTemplate.opsForValue().get(redisKey);
                return mapper.readValue(data, RedisRunningTimeDto.class);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRedisData(String redisKey) {
        String data = "";
        try {
            if(redisTemplate.hasKey(redisKey)) {
                data = redisTemplate.opsForValue().get(redisKey);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    // Set Data
    public void setRedisData(String redisKey, String value) {
        try {
            redisTemplate.opsForValue().set(redisKey , value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publishMessage(String channel, String message) {
        redisTemplate.convertAndSend(channel, message);
    }

    // Delete Data
    public void removeRedisData(String redisKey) {
        redisTemplate.delete(redisKey);
    }
}
