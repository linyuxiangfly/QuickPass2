package tools;

import android.util.Base64;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import javax.crypto.Cipher;

public class RSAEncrypt {
	/** 指定加密算法为DESede */
	private static String ALGORITHM = "RSA";
	/** 指定key的大小 */
	private static int KEYSIZE = 1024;
	/** 指定公钥存放文件 */
	private static String PUBLIC_KEY_FILE = "PublicKey";
	/** 指定私钥存放文件 */
	private static String PRIVATE_KEY_FILE = "PrivateKey";
	
	public static void main(String[] args){
		try {
			Key[] keys=generateKeyPair();

			String data="abc";
			String encStr=encrypt(data,keys[0]);
			System.out.println(encStr);
			System.out.println(decrypt(encStr,keys[1]));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	* 生成密钥对
	*/
	public static Key[] generateKeyPair() throws Exception{
		Key[] retVal=new Key[2];
		
	  /** RSA算法要求有一个可信任的随机数源 */
	   SecureRandom sr = new SecureRandom();
	   /** 为RSA算法创建一个KeyPairGenerator对象 */
	   KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
	  /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
	   kpg.initialize(KEYSIZE, sr);
	   /** 生成密匙对 */
	   KeyPair kp = kpg.generateKeyPair();
	   /** 得到公钥 */
	   retVal[0] = kp.getPublic();
	   /** 得到私钥 */
	   retVal[1] = kp.getPrivate();
	   
	   return retVal;
	}

	/**
	* 加密方法
	* source： 源数据
	*/
	public static String encrypt(String source,Key publicKey) throws Exception{
	   /** 得到Cipher对象来实现对源数据的RSA加密 */
	   Cipher cipher = Cipher.getInstance(ALGORITHM);
	   cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	   byte[] b = source.getBytes();
	   /** 执行加密操作 */
	   byte[] b1 = cipher.doFinal(b);
		return Base64.encodeToString(b1, Base64.DEFAULT);
	}

	/**
	* 解密算法
	* cryptograph:密文
	*/
	public static String decrypt(String cryptograph,Key privateKey) throws Exception{
	   /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
	   Cipher cipher = Cipher.getInstance(ALGORITHM);
	   cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] b1 = Base64.decode(cryptograph, Base64.DEFAULT);
	   /** 执行解密操作 */
	   byte[] b = cipher.doFinal(b1);
	   return new String(b);
	}
}
