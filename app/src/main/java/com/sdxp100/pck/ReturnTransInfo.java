package com.sdxp100.pck;

import com.utils.Convert;

/**
 * Created by Firefly on 2016/3/12.
 */
public class ReturnTransInfo implements ByteSerializable {
    private byte time;
    private int len;

    public byte[] getAbdata() {
        return abdata;
    }

    public void setAbdata(byte[] abdata) {
        this.abdata = abdata;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte getTime() {
        return time;
    }

    public void setTime(byte time) {
        this.time = time;
    }

    private byte[] abdata;

    public void setByte(byte[] data,int offset) {
        setTime(data[offset]);
        setLen(Convert.getShort(data, offset + 1));
        this.abdata=new byte[getLen()];
        for (int i=0;i<getLen();i++){
            this.abdata[i]=data[offset+3+i];
        }
    }

    @Override
    public void setByte(byte[] data) {
        setByte(data,0);
    }

    @Override
    public byte[] toByte() {
        len=abdata.length;
        byte[] data=new byte[3+len];
        data[0]=time;
        Convert.putShort(data, (short)len, 1);
        if(abdata!=null && abdata.length==len){
            for (int i=0;i<len;i++){
                data[3+i]=abdata[i];
            }
        }
        return data;
    }
}
