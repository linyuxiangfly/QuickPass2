package com.opa.returns;

import org.json.JSONObject;
import org.json.JSONTokener;

import tools.JsonUtil;

public class BaseReturn implements JsonUtil.Json {
	private String statusCode;
	private String msg;
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}


	@Override
	public String getJsonString() {
		String retVal="";
		try{
			JSONObject json =getJsonObj();
			retVal=json.toString();
		}catch(Exception e){

		}
		return retVal;
	}

	@Override
	public void setJsonString(String data) {
		try{
			JSONTokener jsonParser = new JSONTokener(data);
			JSONObject val = (JSONObject) jsonParser.nextValue();
			setJsonObj(val);
		}catch(Exception e){

		}
	}

	@Override
	public JSONObject getJsonObj() {
		JSONObject json=null;
		try{
			json = new JSONObject();
			json.put("statusCode",statusCode);
			json.put("msg",msg);
		}catch(Exception e){

		}
		return json;
	}

	@Override
	public void setJsonObj(JSONObject val) {
		try{
			statusCode=(String)val.get("statusCode");
			msg=(String)val.get("msg");
		}catch(Exception e){

		}
	}
}
