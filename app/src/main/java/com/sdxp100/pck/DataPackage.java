package com.sdxp100.pck;

import com.utils.CheckUtil;
import com.utils.Convert;

/**
 * Created by Firefly on 2016/2/21.
 */
public class DataPackage implements ByteSerializable{
    private byte stx;
    private int infoAreaLen;
    private InfoArea infoArea;
    private byte check;
    private byte etx;

    public byte getStx() {
        return stx;
    }

    public void setStx(byte stx) {
        this.stx = stx;
    }

    public int getInfoAreaLen() {
        return infoAreaLen;
    }

    public void setInfoAreaLen(int infoAreaLen) {
        this.infoAreaLen = infoAreaLen;
    }

    public InfoArea getInfoArea() {
        return infoArea;
    }

    public void setInfoArea(InfoArea infoArea) {
        this.infoArea = infoArea;
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
        byte[] data=new byte[7+infoAreaLen];
        data[0]=stx;
        Convert.putInt(data, infoAreaLen, 1);
        if(infoArea!=null){
            byte[] b=infoArea.toByte();
            for(int i=0;i<infoAreaLen;i++){
                data[5+i]=b[i];
            }
        }
        data[5+infoAreaLen]=CheckUtil.xor(data,1,4+infoAreaLen);
        data[6+infoAreaLen]=etx;
        return data;
    }
}
