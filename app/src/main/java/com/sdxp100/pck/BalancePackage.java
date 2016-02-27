package com.sdxp100.pck;

/**
 * Created by Firefly on 2016/2/27.
 */
public class BalancePackage implements ByteSerializable {
    @Override
    public void setByte(byte[] data) {

    }

    @Override
    public byte[] toByte() {
        byte[] retVal=new byte[6];

        retVal[0]=(byte)0xBE;
        retVal[1]=(byte)0x41;

        return retVal;
    }
}
