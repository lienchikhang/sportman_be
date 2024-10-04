package com.sportman.services.interfaces;

public interface RedisService {

    public void saveKey(String key, String value, Long ttl);

    public void delete(String key);

    public boolean isExist(String key);

    public String getValue(String key);
}
