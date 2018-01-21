package com.sungung.appcaching.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.sungung.appcaching.entity.City;

public interface CityRepository extends CrudRepository<City, Long>{
	Optional<City> findOneByCode(String code);
	Collection<City> findByState(String state);
}
