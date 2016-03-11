package tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtil {

	public interface Json{
		public String getJsonString();
		public void setJsonString(String data);
		public JSONObject getJsonObj();
		public void setJsonObj(JSONObject jsonObject);
	}

	public static String toJson(Json obj){
        return obj.getJsonString();
	}
	
	public static <T extends Json> T toObj(Class<T> cls,String val){
		T retVal=null;
		try {
			retVal=(T)cls.newInstance();
			retVal.setJsonString(val);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return retVal;
	}
}
