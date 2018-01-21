package com.sungung.appcaching.cache;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.sungung.appcaching.entity.City;
import com.sungung.appcaching.repository.CityService;

import redis.clients.jedis.Jedis;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheTests {

	private final static Logger logger = LoggerFactory.getLogger(CacheTests.class);
	
	@Autowired
	private CityService cityService;
	
	@Autowired
	private CacheManager cacheManager;
	
	private Jedis jedis;
		
	@Before
	public void setup(){
		// clean up all cache
		jedis = new Jedis("localhost", 6379);
		jedis.flushAll();
	}
	
	@Test
	public void test(){
		logger.debug("Getting live data which is slow");
		logger.debug("" + cityService.getCities());
		
		logger.debug("Getting from cache");
		logger.debug("" + cityService.getCities());
		
		logger.debug("Evit cache");
		cityService.reload();
		
		logger.debug("Getting live data again");
		logger.debug("" + cityService.getCities());		
	}
	
	@Test
	public void validateCache(){
		// No cached data
		Cache cities = cacheManager.getCache("cities");
		assertNull(cities.get("MEL"));
		
		// invoke method, then fill cache
		City city = cityService.getCachedCity("MEL");
		// now there is cached data
		assertEquals("Melbourne", ((City)cities.get("MEL").get()).getCity());
		
		// evict cache
		cityService.reload();
		cities = cacheManager.getCache("cities");
		// now there is no cached data
		assertNull(cities.get("MEL"));
	}
	
}
