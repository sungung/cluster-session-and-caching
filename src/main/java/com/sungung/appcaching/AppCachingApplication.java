package com.sungung.appcaching;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.sungung.appcaching.entity.City;
import com.sungung.appcaching.repository.CityRepository;

@SpringBootApplication
@EnableCaching
public class AppCachingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppCachingApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner demo(CityRepository repo){
		return (args) -> {
			repo.save(new City("BNE", "Brisbane", "QLD"));
			repo.save(new City("SYD", "Sydney", "NSW"));
			repo.save(new City("MEL", "Melbourne", "VIC"));
			repo.save(new City("ADL", "Adelaide", "SA"));
			repo.save(new City("GEE", "Geelong", "VIC"));
			repo.save(new City("BAL", "Ballarat", "VIC"));
		};
	}
	
	@Bean
	public KeyGenerator simpleKeyGenerator(){
		return (target, method, params) -> {
			StringBuilder sb = new StringBuilder();
			sb.append(target.getClass().getName());
			sb.append(method.getName());
			for (Object obj : params){
				sb.append(obj.toString());
			}
			return sb.toString();
		};
	}
	

}
