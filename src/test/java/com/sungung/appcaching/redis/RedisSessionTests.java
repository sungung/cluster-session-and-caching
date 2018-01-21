package com.sungung.appcaching.redis;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import redis.clients.jedis.Jedis;

public class RedisSessionTests {
	private Jedis jedis;
	private TestRestTemplate testRestTemplate;
	private TestRestTemplate testRestTemplateWithAuth;
	
	@Before
	public void setup(){
		testRestTemplateWithAuth = new TestRestTemplate("user", "password", null); 
		testRestTemplate = new TestRestTemplate();
		jedis = new Jedis("localhost", 6379);
		jedis.flushAll();
	}
	
	@Test
	public void testSession(){
		// invoke api with auth
		ResponseEntity<String> response = testRestTemplateWithAuth.getForEntity("http://localhost:8080/", String.class);
		assertEquals("Hello user", response.getBody());
		
		// should find new session entry in redis
		Set<String> redisCache = jedis.keys("*");
		assertTrue(redisCache.size() > 0);
		
		String sessionCookie = response.getHeaders().get("Set-Cookie").get(0).split(";")[0];
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cookie", sessionCookie);
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);
		
		// invoke api with cookie
		response = testRestTemplate.exchange("http://localhost:8080/", HttpMethod.GET, httpEntity, String.class);
		assertEquals("Hello user", response.getBody());
		
		// remove session from redis
		jedis.flushAll();
		
		// invoke api with cookie again, then you should get 401 error
		response = testRestTemplate.exchange("http://localhost:8080/", HttpMethod.GET, httpEntity, String.class);
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		
	}
}
