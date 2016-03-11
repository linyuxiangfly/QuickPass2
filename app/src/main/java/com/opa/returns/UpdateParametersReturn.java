package com.opa.returns;

import org.json.JSONObject;
import org.json.JSONTokener;

import tools.JsonUtil;

public class UpdateParametersReturn extends BaseReturn implements JsonUtil.Json{
	public static class Data implements JsonUtil.Json{
		private String AIDNewVersion;
		private String AIDFile;
		private String AIDFileMD5;
		private String CAPKNewVersion;
		private String CAPKFile;
		private String CAPKFileMD5;
		private String FWNewVersion;
		private String FWFile;
		private String FWFileMD5;
		public String getAIDNewVersion() {
			return AIDNewVersion;
		}
		public void setAIDNewVersion(String aIDNewVersion) {
			AIDNewVersion = aIDNewVersion;
		}
		public String getAIDFile() {
			return AIDFile;
		}
		public void setAIDFile(String aIDFile) {
			AIDFile = aIDFile;
		}
		public String getAIDFileMD5() {
			return AIDFileMD5;
		}
		public void setAIDFileMD5(String aIDFileMD5) {
			AIDFileMD5 = aIDFileMD5;
		}
		public String getCAPKNewVersion() {
			return CAPKNewVersion;
		}
		public void setCAPKNewVersion(String cAPKNewVersion) {
			CAPKNewVersion = cAPKNewVersion;
		}
		public String getCAPKFile() {
			return CAPKFile;
		}
		public void setCAPKFile(String cAPKFile) {
			CAPKFile = cAPKFile;
		}
		public String getCAPKFileMD5() {
			return CAPKFileMD5;
		}
		public void setCAPKFileMD5(String cAPKFileMD5) {
			CAPKFileMD5 = cAPKFileMD5;
		}
		public String getFWNewVersion() {
			return FWNewVersion;
		}
		public void setFWNewVersion(String fWNewVersion) {
			FWNewVersion = fWNewVersion;
		}
		public String getFWFile() {
			return FWFile;
		}
		public void setFWFile(String fWFile) {
			FWFile = fWFile;
		}
		public String getFWFileMD5() {
			return FWFileMD5;
		}
		public void setFWFileMD5(String fWFileMD5) {
			FWFileMD5 = fWFileMD5;
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
				json.put("AIDNewVersion",AIDNewVersion);
				json.put("AIDFile",AIDFile);
				json.put("AIDFileMD5",AIDFileMD5);
				json.put("CAPKNewVersion",CAPKNewVersion);
				json.put("CAPKFile",CAPKFile);
				json.put("CAPKFileMD5",CAPKFileMD5);
				json.put("FWNewVersion",FWNewVersion);
				json.put("FWFile",FWFile);
				json.put("FWFileMD5",FWFileMD5);
			}catch(Exception e){

			}
			return json;
		}

		@Override
		public void setJsonObj(JSONObject val) {
			try{
				AIDNewVersion=(String)val.get("AIDNewVersion");
				AIDFile=(String)val.get("AIDFile");
				AIDFileMD5=(String)val.get("AIDFileMD5");
				CAPKNewVersion=(String)val.get("CAPKNewVersion");
				CAPKFile=(String)val.get("CAPKFile");
				CAPKFileMD5=(String)val.get("CAPKFileMD5");
				FWNewVersion=(String)val.get("FWNewVersion");
				FWFile=(String)val.get("FWFile");
				FWFileMD5=(String)val.get("FWFileMD5");
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
			this.getData().setJsonObj((JSONObject) val.get("data"));
		}catch(Exception e){

		}
	}
}
