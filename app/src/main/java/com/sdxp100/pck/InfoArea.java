package com.sdxp100.pck;

import com.utils.Convert;

import java.io.Serializable;

/**
 * Created by Firefly on 2016/2/21.
 */
public class InfoArea implements ByteSerializable {
    private byte type;
    private int len;
    private byte slot;
    private byte bseq;
    private byte bBwi;
    private short levelParam;
    private byte[] abdata;

    public short getLevelParam() {
        return levelParam;
    }

    public void setLevelParam(short levelParam) {
        this.levelParam = levelParam;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte getSlot() {
        return slot;
    }

    public void setSlot(byte slot) {
        this.slot = slot;
    }

    public byte getBseq() {
        return bseq;
    }

    public void setBseq(byte bseq) {
        this.bseq = bseq;
    }

    public byte getbBwi() {
        return bBwi;
    }

    public void setbBwi(byte bBwi) {
        this.bBwi = bBwi;
    }

    public byte[] getAbdata() {
        return abdata;
    }

    public void setAbdata(byte[] abdata) {
        this.abdata = abdata;
    }

    public void setByte(byte[] data,int offset){
        setType(data[offset]);
        setLen(Convert.getInt(data, offset + 1));
        setSlot(data[offset + 5]);
        setBseq(data[offset + 6]);
        setbBwi(data[offset + 7]);
        setLevelParam(Convert.getShort(data,offset+8));
        this.abdata=new byte[getLen()];
        for (int i=0;i<getLen();i++){
            this.abdata[i]=data[offset+10+i];
        }
    }

    public void setByte(byte[] data){
        setByte(data,0);
    }

    public byte[] toByte(){
        len=abdata.length;
        byte[] data=new byte[10+len];
        data[0]=type;
        Convert.putInt(data, len, 1);
        data[5]=slot;
        data[6]=bseq;
        data[7]=bBwi;
        Convert.putShort(data,levelParam,8);
        if(abdata!=null && abdata.length==len){
            for (int i=0;i<len;i++){
                data[10+i]=abdata[i];
            }
        }
        return data;
    }
}
