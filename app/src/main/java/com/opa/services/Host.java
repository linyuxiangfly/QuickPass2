package com.opa.services;


import com.opa.params.BaseParam;
import com.opa.params.GenKeyParam;
import com.opa.params.TransparentParam;
import com.opa.params.UpdateParametersParam;
import com.opa.returns.GenKeyReturn;
import com.opa.returns.TransparentReturn;
import com.opa.returns.UpdateParametersReturn;
import com.utils.ALGHelper;
import com.utils.StringUtil;

import Decoder.BASE64Decoder;
import tools.ByteUtil;
import tools.HttpRequest;
import tools.JsonUtil;

public class Host extends BaseService {

	public String pk = "";

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	private byte[] tk;
	private int source;
	// private String merchNo;
	// private String termId;
	private byte[] ck;
	String strCk;
	private String sessionId;

	public Host(String url, int source, String merchNo, String termId, String pk)
			throws Exception {
		super(url);

		this.pk = pk;
		this.source = source;
		// this.merchNo=merchNo;
		// this.termId=termId;

		// 生成随机key
		tk = ALGHelper.buildTK();

		GenKeyParam gkp = new GenKeyParam();
		gkp.setSource(source);
		gkp.setData(new GenKeyParam.Data());
		gkp.getData().setMerchNO(merchNo);
		gkp.getData().setTermID(termId);

		try {
			GenKeyReturn gkr = genKey(gkp, pk, tk);
			if (gkr != null) {
				if ("-1".equals(gkr.getStatusCode())) {
					throw new Exception(gkr.getMsg());
				} else {
					// 解密ckey
					System.out.println("encCK:" + gkr.getData().getcKey());
					byte[] ckTemp = ALGHelper.DES3Decrypt(ByteUtil.asc2bcd(gkr
							.getData().getcKey().getBytes()), tk);
					System.out.println("ck:" + new String(ckTemp));
					ck = ByteUtil.asc2bcd(ckTemp);
					System.out.println("ck2:" + new String(ck));
					sessionId = gkr.getData().getSessionID();
				}
			}

		} catch (Exception e) {
			throw e;
		}
	}

	public <T extends JsonUtil.Json> T post(Class<T> cls, String method, JsonUtil.Json param)
			throws Exception {
		T retVal = null;

		String dataStr = JsonUtil.toJson(param);
		System.out.println("dataStr:" + dataStr);
		String retStr = HttpRequest.sendPost(this.getUrl() + method, dataStr);
		System.out.println("retStr:" + retStr);
		retVal = JsonUtil.toObj(cls, retStr);

		return retVal;
	}

	public <T extends JsonUtil.Json> T postDes(Class<T> cls, String method, BaseParam param)
			throws Exception {
		T retVal = null;

		param.setSessionID(sessionId);
		param.setSource(source);
		param.setVersion("1.0");

		String dataStr = JsonUtil.toJson(param);
		System.out.println("dataStr:" + dataStr);
		// 用cKey加密
		String encStr = StringUtil.byte2HexStr(ALGHelper.DES3Encrypt(
				dataStr.getBytes(), ck));

		String retStr = HttpRequest.sendPost(this.getUrl() + method, encStr);

		String decStr = new String(ALGHelper.DES3Decrypt(
				StringUtil.hexStr2Bytes(retStr), ck));

		retVal = JsonUtil.toObj(cls, decStr);

		return retVal;
	}

	private GenKeyReturn genKey(GenKeyParam param, String pk, byte[] tk)
			throws Exception {
		// PK解base64
		byte[] publicKey = new BASE64Decoder().decodeBuffer(pk);
		String data = StringUtil.byte2HexStr((ALGHelper.RSAEncryptByPK(tk,
				publicKey)));
		param.getData().setTKey(data);
		return post(GenKeyReturn.class, "genKey", param);
	}

	public TransparentReturn transparent(String data) throws Exception {
		TransparentParam tp = new TransparentParam();

		tp.setData(new TransparentParam.Data());
		tp.getData().setTransparentData(data);

		return postDes(TransparentReturn.class, "transparent", tp);
	}

	public UpdateParametersReturn updateParameters(String aidVer,
			String capkVer, String fwVer) throws Exception {
		UpdateParametersParam upp = new UpdateParametersParam();
		upp.setData(new UpdateParametersParam.Data());
		upp.getData().setAIDVersion(aidVer);
		upp.getData().setCAPKVersion(capkVer);
		upp.getData().setFWVersion(fwVer);

		return postDes(UpdateParametersReturn.class, "updateParameters", upp);
	}
}
