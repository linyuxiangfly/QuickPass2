package com.utils;

/**
 * Created by Firefly on 2016/2/21.
 */
public class CheckUtil {
    public static byte xor(byte[] data,int offset,int len){
        byte retVal=data[offset];
        for(int i=offset+1;i<offset+len;i++){
            retVal^=data[i];
        }
        return retVal;
    }
    public static byte xor(byte check,byte[] data,int offset,int len){
        byte retVal=check;
        for(int i=offset;i<offset+len;i++){
            retVal^=data[i];
        }
        return retVal;
    }
}
