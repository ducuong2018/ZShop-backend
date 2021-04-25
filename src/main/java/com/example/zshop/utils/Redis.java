package com.example.zshop.utils;

import com.example.zshop.models.SendOtpDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
@Log4j2
@Component
public class Redis {
    @Autowired
    RedisTemplate<Object,Object> redisTemplate;
    public void setRedis(String email, Object value) throws IOException {
        redisTemplate.opsForValue().set(email, JsonParser.toJson(value));
    }
    public <T> T getRedis(String email,Class<T> tClass){
        try {
            return JsonParser.entity((String) redisTemplate.opsForValue().get(email), tClass);
        }
        catch (Exception e){
            return null;
        }

    }
    public void del(String key) {
        redisTemplate.delete(key);
    }
}