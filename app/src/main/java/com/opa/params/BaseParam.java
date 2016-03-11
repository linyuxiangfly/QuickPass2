package com.opa.params;

import com.utils.StringUtil;

import org.json.JSONObject;
import org.json.JSONTokener;

import tools.JsonUtil;

public class BaseParam implements JsonUtil.Json {
	public final static int SRC_PC=1;
	public final static int SRC_ANDROID=2;
	public final static int SRC_IPHONE=3;
	public final static int SRC_WEB=4;
	
	private String sessionID;
	private String version="1.0";
	private int source;
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}

	@Override
	public String getJsonString() {
		String retVal="";
		try{
			JSONObject json = new JSONObject();
			json.put("sessionID",sessionID);
			json.put("version",version);
			json.put("source",source);

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
			json.put("sessionID",sessionID);
			json.put("version",version);
			json.put("source",source);
		}catch(Exception e){

		}
		return json;
	}

	@Override
	public void setJsonObj(JSONObject val) {
		try{
			sessionID=(String)val.get("sessionID");
			version=(String)val.get("version");
			source=(int)val.get("source");
		}catch(Exception e){

		}
	}
}
