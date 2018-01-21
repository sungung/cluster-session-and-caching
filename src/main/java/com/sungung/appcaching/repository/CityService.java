package com.sungung.appcaching.repository;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.sungung.appcaching.entity.City;

@Component
public class CityService {

	@Autowired
	private CityRepository repo;
	
	@Cacheable("cities")
	public Collection<City> getCities() {
		simulateSlowService();
		return StreamSupport.stream(repo.findAll().spliterator(), false)
		.collect(Collectors.toList());
	}
	
	@Cacheable(value = "cities", key = "#code")
	public City getCachedCity(String code){
		simulateSlowService();
		return repo.findOneByCode(code).get();
	}
	
	@CacheEvict(value = "cities", allEntries = true)
	public void reload(){}
	
	public City getLiveCity(String code){
		simulateSlowService();
		return repo.findOneByCode(code).get();
	}
	
	private void simulateSlowService() {
		try {
			Thread.sleep(3000L);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}
}
