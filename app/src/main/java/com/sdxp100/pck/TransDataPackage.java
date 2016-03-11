package com.sdxp100.pck;

import com.utils.CheckUtil;
import com.utils.Convert;

/**
 * Created by Firefly on 2016/2/21.
 */
public class TransDataPackage implements ByteSerializable{
    private byte stx;

    public int getTransLen() {
        return transLen;
    }

    public void setTransLen(int transLen) {
        this.transLen = transLen;
    }

    private int transLen;

    public TransparentPackage getTrans() {
        return trans;
    }

    public void setTrans(TransparentPackage trans) {
        this.trans = trans;
    }

    private TransparentPackage trans;
    private byte check;
    private byte etx;

    public byte getStx() {
        return stx;
    }

    public void setStx(byte stx) {
        this.stx = stx;
    }

    public byte getCheck() {
        return check;
    }

    public void setCheck(byte check) {
        this.check = check;
    }

    public byte getEtx() {
        return etx;
    }

    public void setEtx(byte etx) {
        this.etx = etx;
    }

    public void setByte(byte[] data){

    }

    public byte[] toByte(){
        byte[] data=new byte[7+transLen];
        data[0]=stx;
        Convert.putInt(data, transLen, 1);
        if(trans!=null){
            byte[] b=trans.toByte();
            for(int i=0;i<transLen;i++){
                data[5+i]=b[i];
            }
        }
        data[5+transLen]=CheckUtil.xor(data,1,4+transLen);
        data[6+transLen]=etx;
        return data;
    }
}
