package com.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class ALGHelper {

	public static byte[] RSAEncryptByPK(byte[] data, byte[] publicKey) {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
				publicKey);
		byte[] encRet = null;

		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey pk = keyFactory.generatePublic(x509EncodedKeySpec);
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, pk);
			ByteArrayOutputStream baos = new ByteArrayOutputStream(245);
			for (int i = 0; i < data.length; i += 245) {
				byte[] temp;
				if (data.length - i < 245) {
					temp = new byte[data.length];
					System.arraycopy(data, i, temp, 0, data.length);
				} else {
					temp = new byte[245];
					System.arraycopy(data, i, temp, 0, 245);
				}

				baos.write(cipher.doFinal(temp));
			}
			encRet = baos.toByteArray();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return encRet;
	}

	public static byte[] DES3Encrypt(byte[] data, byte[] key) {
		try {
			Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "DESede"));
			byte[] desData = data;
			if (data.length % 8 != 0) {
				desData = new byte[data.length + 8 - (data.length % 8)];
				System.arraycopy(data, 0, desData, 0, data.length);
				Arrays.fill(desData, data.length, desData.length, (byte) 0x00);
			}
			byte[] res = cipher.doFinal(desData);
			return res;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] DES3Decrypt(byte[] data, byte[] key) {
		try {
			Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "DESede"));
			byte[] res = cipher.doFinal(data);
			return res;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String buildTKStr() {
		return buildTKStr(24);
	}
	
	public static String buildTKStr(int keyLen) {
		String tempKey = StringUtil.bcd2Str(buildTK(keyLen));
		return tempKey;
	}
	
	public static byte[] buildTK() {
		return buildTK(24);
	}
	
	public static byte[] buildTK(int keyLen) {
		// 生成随机数
		byte[] ranBytes = new byte[keyLen];// TK
		for (int i = 0; i < keyLen; i++) {
			int ran1 = (int) (Math.random() * 16);
			int ran2 = (int) (Math.random() * 16);
			int ran = ran1 * 16 + ran2;
			byte[] tmp = new byte[] { (byte) ran };
			System.arraycopy(tmp, 0, ranBytes, i, 1);
		}
		return ranBytes;
	}

}
