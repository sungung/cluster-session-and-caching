package com.sungung.appcaching;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

@Configuration
@EnableCaching
public class CacheConfiguration extends CachingConfigurerSupport  {

	@Bean
	@Override
	public KeyGenerator keyGenerator() {
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

	@Bean
	@Primary
	RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory rcf){
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(rcf);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new JsonRedisSerializer());
		return template;
	}
	
	static class JsonRedisSerializer implements RedisSerializer<Object>{
		private final ObjectMapper om;
		public JsonRedisSerializer() {
			this.om = new ObjectMapper().enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
		}
		@Override
		public Object deserialize(byte[] bytes) throws SerializationException {
			if (bytes == null) {
				return null;
			}
			try {
				return om.readValue(bytes,  Object.class);
			} catch(Exception e){
				throw new SerializationException(e.getMessage(), e);
			}
		}
		@Override
		public byte[] serialize(Object obj) throws SerializationException {
			try {
				return om.writeValueAsBytes(obj);
			} catch(JsonProcessingException e){
				throw new SerializationException(e.getMessage(), e);
			}
		}		
	}	
}
