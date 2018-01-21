package com.sungung.appcaching.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.sungung.appcaching.entity.City;
import com.sungung.appcaching.repository.CityService;

@RestController
public class AppController {
	
	@Autowired
	FindByIndexNameSessionRepository<? extends Session> sessions;
	
	@Autowired
	private CityService cityService;
	
	@GetMapping("/")
	public String hello(Principal user){
		return "Hello " + user.getName();
	}
	
	@GetMapping("/sessions")
	public Collection<? extends Session> getSessions(Principal user) {
		return sessions.findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, user.getName()).values();		
	}
	
	@DeleteMapping("/sessions/{sessionId}")
	public void removeSession(Principal user, @PathVariable String sessionId){
		Set<String> userSessions = sessions.findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, user.getName()).keySet();
		if (userSessions.contains(sessionId)) {
			sessions.delete(sessionId);
		}
	}
	
	@GetMapping("/cities/cache-bust")
	public void cacheBust(){
		cityService.reload();
	}
	
	@GetMapping("/city/{code}")
	public ResponseEntity<?> getCityByCode(@PathVariable String code){
		City city = cityService.getCachedCity(code);
		if (city == null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else{
			return ResponseEntity.ok(city);
		}
	}
	
	@GetMapping("/cities")
	public Collection<City> getAll(){
		return cityService.getCities();
	}
}
