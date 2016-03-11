package com.sdxp100.pck;

import com.utils.Convert;

/**
 * Created by Firefly on 2016/2/27.
 */
public class TransparentPackage implements ByteSerializable {
    private byte dd;
    private byte state;
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte getDd() {
        return dd;
    }

    public void setDd(byte dd) {
        this.dd = dd;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    @Override
    public void setByte(byte[] data) {

    }

    @Override
    public byte[] toByte() {
        byte[] retVal=new byte[4+data.length];

        retVal[0]=dd;
        retVal[1]=state;

        Convert.putShort(retVal,(short)data.length,2);

        for (int i=0;i<data.length;i++){
            retVal[4+i]=data[i];
        }
        return retVal;
    }
}
