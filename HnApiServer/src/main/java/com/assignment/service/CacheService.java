package com.assignment.service;

public interface CacheService {

	void set(String key, String value);
	
	Object get(String key);
}
