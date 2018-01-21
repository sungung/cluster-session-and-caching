package com.sungung.appcaching.session;

import java.io.Serializable;

public class SessionDetails implements Serializable{
	private static final long serialVersionUID = -7868333862678623174L;
	private String location;
	private String accessType;
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
}
