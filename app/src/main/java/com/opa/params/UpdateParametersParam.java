package com.opa.params;

import org.json.JSONObject;
import org.json.JSONTokener;

import tools.JsonUtil;

public class UpdateParametersParam extends BaseParam implements JsonUtil.Json{

	public static class Data implements JsonUtil.Json{
		private String AIDVersion;
		private String CAPKVersion;
		private String FWVersion;

		public String getAIDVersion() {
			return AIDVersion;
		}
		public void setAIDVersion(String aIDVersion) {
			AIDVersion = aIDVersion;
		}
		public String getCAPKVersion() {
			return CAPKVersion;
		}
		public void setCAPKVersion(String cAPKVersion) {
			CAPKVersion = cAPKVersion;
		}
		public String getFWVersion() {
			return FWVersion;
		}
		public void setFWVersion(String fWVersion) {
			FWVersion = fWVersion;
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
				json.put("AIDVersion",AIDVersion);
				json.put("CAPKVersion",CAPKVersion);
				json.put("FWVersion",FWVersion);
			}catch(Exception e){

			}
			return json;
		}

		@Override
		public void setJsonObj(JSONObject val) {
			try{
				AIDVersion=(String)val.get("AIDVersion");
				CAPKVersion=(String)val.get("CAPKVersion");
				FWVersion=(String)val.get("FWVersion");
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

		}
	}

	@Override
	public JSONObject getJsonObj() {
		JSONObject json=null;
		try{
			json = new JSONObject();
			json.put("sessionID", this.getSessionID());
			json.put("version", this.getVersion());
			json.put("source", this.getSource());
			json.put("data", data.getJsonObj());
		}catch(Exception e){

		}
		return json;
	}

	@Override
	public void setJsonObj(JSONObject val) {
		try{
			this.data=new Data();
			this.setSessionID((String) val.get("sessionID"));
			this.setVersion((String) val.get("version"));
			this.setSource((int) val.get("source"));
			this.setData(new Data());
			this.getData().setJsonObj((JSONObject) val.get("data"));
		}catch(Exception e){

		}
	}
}
