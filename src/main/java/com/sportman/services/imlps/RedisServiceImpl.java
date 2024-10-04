package com.sportman.services.imlps;

import com.sportman.services.interfaces.RedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    RedisTemplate<String, Object> redis;

    @Override
    public void saveKey(String key, String value, Long ttl) {
        redis.opsForValue().set(key, value, ttl, TimeUnit.MILLISECONDS);
    }

    @Override
    public void delete(String key) {
        redis.opsForValue().getAndDelete(key);
    }

    @Override
    public boolean isExist(String key) {
        return redis.hasKey(key);
    }

    @Override
    public String getValue(String key) {
        return redis.opsForValue().get(key).toString();
    }
}
