package com.sdxp100.pck;

/**
 * Created by Firefly on 2016/2/27.
 */
public class RegPackage implements ByteSerializable {
    private byte p1;
    private byte p2;

    public byte getP2() {
        return p2;
    }

    public void setP2(byte p2) {
        this.p2 = p2;
    }

    public byte getP1() {
        return p1;
    }

    public void setP1(byte p1) {
        this.p1 = p1;
    }

    @Override
    public void setByte(byte[] data) {

    }

    @Override
    public byte[] toByte() {
        byte[] retVal=new byte[6];

        retVal[0]=(byte)0xBE;
        retVal[1]=(byte)0x42;

        retVal[2]=p1;
        retVal[3]=p2;

        return retVal;
    }
}
