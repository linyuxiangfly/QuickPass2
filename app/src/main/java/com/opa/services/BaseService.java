package com.opa.services;

public class BaseService {
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public BaseService(String url){
		this.url=url;
	}
}
