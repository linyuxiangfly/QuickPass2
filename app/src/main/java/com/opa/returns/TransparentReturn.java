package com.opa.returns;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Serializable;

import tools.JsonUtil;

public class TransparentReturn extends BaseReturn implements JsonUtil.Json,Serializable {
	public static class Data implements JsonUtil.Json{
		private String ServerRespData;

		public String getServerRespData() {
			return ServerRespData;
		}

		public void setServerRespData(String serverRespData) {
			ServerRespData = serverRespData;
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
				json.put("ServerRespData", ServerRespData);
			}catch(Exception e){

			}
			return json;
		}

		@Override
		public void setJsonObj(JSONObject jsonObject) {
			try{
				ServerRespData=(String)jsonObject.get("ServerRespData");
			}catch (Exception e){
				e.printStackTrace();
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
			json.put("statusCode",this.getStatusCode());
			json.put("msg",this.getMsg());
			json.put("data",data.getJsonObj());
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
