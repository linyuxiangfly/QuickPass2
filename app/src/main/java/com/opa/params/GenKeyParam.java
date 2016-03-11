package com.opa.params;

import org.json.JSONObject;
import org.json.JSONTokener;

import tools.JsonUtil;

public class GenKeyParam implements JsonUtil.Json {

	public static class Data implements JsonUtil.Json{
		private String TKey;
		private String MerchNO;
		private String TermID;

		public String getTKey() {
			return TKey;
		}

		public void setTKey(String tKey) {
			TKey = tKey;
		}

		public String getMerchNO() {
			return MerchNO;
		}

		public void setMerchNO(String merchNO) {
			MerchNO = merchNO;
		}

		public String getTermID() {
			return TermID;
		}

		public void setTermID(String termID) {
			TermID = termID;
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
				json.put("TKey",TKey);
				json.put("MerchNO",MerchNO);
				json.put("TermID",TermID);
			}catch(Exception e){

			}
			return json;
		}

		@Override
		public void setJsonObj(JSONObject val) {
			try{
				TKey=(String)val.get("TKey");
				MerchNO=(String)val.get("MerchNO");
				TermID=(String)val.get("TermID");
			}catch(Exception e){

			}
		}
	}
	
	private String version="1.0";
	private int source;
	private Data data;
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
			json.put("version",version);
			json.put("source",source);
			json.put("data",data.getJsonObj());
		}catch(Exception e){

		}
		return json;
	}

	@Override
	public void setJsonObj(JSONObject val) {
		try{
			version=(String)val.get("version");
			source=(int)val.get("source");
			this.setData(new Data());
			this.getData().setJsonObj((JSONObject) val.get("data"));
		}catch(Exception e){

		}
	}
}
