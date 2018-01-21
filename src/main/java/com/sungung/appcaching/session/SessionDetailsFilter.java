package com.sungung.appcaching.session;

import java.io.IOException;
import java.net.InetAddress;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 101)
public class SessionDetailsFilter extends OncePerRequestFilter {

	static final String UNKNOWN = "Unknown";

	private DatabaseReader reader;

	@Autowired
	public SessionDetailsFilter(DatabaseReader reader) {
		this.reader = reader;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		chain.doFilter(request, response);
		HttpSession session = request.getSession(false);
		if (session != null){
			String remoteAddr = getRemoteAddress(request);
			String geoLocation = getGeoLocation(remoteAddr);
			SessionDetails details = new SessionDetails();
			details.setAccessType(request.getHeader("User-Agenet"));
			details.setLocation(remoteAddr + " " + geoLocation);
			session.setAttribute("SESSION_DETAILS", details);
		}
	}

	private String getRemoteAddress(HttpServletRequest request) {
		String remoteAddr = request.getHeader("X-FORWARDED-FOR");
		if (remoteAddr == null) {
			remoteAddr = request.getRemoteAddr();
		}
		else if (remoteAddr.contains(",")) {
			remoteAddr = remoteAddr.split(",")[0];
		}
		return remoteAddr;
	}
	
	String getGeoLocation(String remoteAddr) {
		try {
			CityResponse city = this.reader.city(InetAddress.getByName(remoteAddr));
			String cityName = city.getCity().getName();
			String countryName = city.getCountry().getName();
			if (cityName == null && countryName == null) {
				return null;
			}
			else if (cityName == null) {
				return countryName;
			}
			else if (countryName == null) {
				return cityName;
			}
			return cityName + ", " + countryName;
		}
		catch (Exception e) {
			return UNKNOWN;

		}
	}	
}
