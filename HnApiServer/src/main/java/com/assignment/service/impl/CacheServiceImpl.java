package com.assignment.service.impl;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.assignment.service.CacheService;

/**
 * Implementation class for CacheService using RedisTemplate for caching data
 * 
 * @author Maulik Patel
 *
 */
@Service
public class CacheServiceImpl implements CacheService {

	private final Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * Set a value for a key in cache
	 */
	public void set(String key, String value) {
		redisTemplate.opsForValue().set(key, value, 10, TimeUnit.MINUTES);
	}

	/**
	 * Get a value from a key in cache
	 */
	public Object get(String key) {
		try {
			return redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return null;
	}

}
