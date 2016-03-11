package com.opa.returns;

import org.json.JSONObject;
import org.json.JSONTokener;

import tools.JsonUtil;

public class GenKeyReturn extends BaseReturn  implements JsonUtil.Json{
	public static class Data  implements JsonUtil.Json{
		private String cKey;
		private String sessionID;
		public String getcKey() {
			return cKey;
		}
		public void setcKey(String cKey) {
			this.cKey = cKey;
		}
		public String getSessionID() {
			return sessionID;
		}
		public void setSessionID(String sessionID) {
			this.sessionID = sessionID;
		}

		@Override
		public String getJsonString() {
			String retVal="";
			try{
				JSONObject json = getJsonObj();
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
				json.put("cKey",cKey);
				json.put("sessionID",sessionID);
			}catch(Exception e){

			}
			return json;
		}

		@Override
		public void setJsonObj(JSONObject val) {
			try{
				cKey=(String)val.get("cKey");
				sessionID=(String)val.get("sessionID");
			}catch(Exception e){

			}
		}
	}
	
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	@Override
	public String getJsonString() {
		String retVal="";
		try{
			JSONObject json = getJsonObj();

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
			e.printStackTrace();
		}
	}

	@Override
	public JSONObject getJsonObj() {
		JSONObject json=null;
		try{
			json = new JSONObject();
			json.put("statusCode", this.getStatusCode());
			json.put("msg", this.getMsg());
			json.put("data", data.getJsonObj());
		}catch(Exception e){

		}
		return json;
	}

	@Override
	public void setJsonObj(JSONObject val) {
		try{
			this.setStatusCode((int) val.get("statusCode")+"");
			this.setMsg((String) val.get("msg"));
			this.setData(new Data());
			this.getData().setJsonObj((JSONObject)val.get("data"));
		}catch(Exception e){

		}
	}
}
