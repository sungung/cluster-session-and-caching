package com.sungung.appcaching;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.maxmind.geoip2.DatabaseReader;

@Configuration
public class GeoConfiguration {

	@Bean
	public DatabaseReader geoDatabaseReader(@Value("classpath:GeoLite2-City.mmdb") InputStream geoInputStream) throws IOException{
		return new DatabaseReader.Builder(geoInputStream).build();
	}
}
