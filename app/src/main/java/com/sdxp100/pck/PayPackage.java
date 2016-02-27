package com.sdxp100.pck;

import com.utils.BCDDecode;
import com.utils.Convert;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Firefly on 2016/2/27.
 */
public class PayPackage implements ByteSerializable {
    private static SimpleDateFormat formatDate = new SimpleDateFormat("yyMMdd");
    private static SimpleDateFormat formatDay = new SimpleDateFormat("HHmmss");

    private long money;
    private Date payDate;
    private int serialNum;

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public int getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }

    @Override
    public void setByte(byte[] data) {

    }

    @Override
    public byte[] toByte() {
        byte[] retVal=new byte[36];

        retVal[0]=(byte)0xBE;
        retVal[1]=(byte)0x40;

        retVal[5]=(byte)0x1E;

        //交易金额
        retVal[6]=(byte)0x9F;
        retVal[7]=(byte)0x02;
        retVal[8]=(byte)0x06;
        byte[] moneyBCD=BCDDecode.lng2Bcd(money, 12);
        for (int i=0;i<moneyBCD.length;i++){
            retVal[9+i]=moneyBCD[i];
        }

        //交易日期
        retVal[15]=(byte)0x9A;
        retVal[16]=(byte)0x03;
        //日期BCD码
        byte[] dateBCD= BCDDecode.str2Bcd(formatDate.format(payDate));
        for(int i=0;i<dateBCD.length;i++){
            retVal[17+i]=dateBCD[i];
        }

        //交易日期
        retVal[20]=(byte)0x5F;
        retVal[21]=(byte)0x21;
        retVal[22]=(byte)0x03;
        //日期BCD码
        byte[] dayBCD= BCDDecode.str2Bcd(formatDay.format(payDate));
        for(int i=0;i<dayBCD.length;i++){
            retVal[23+i]=dayBCD[i];
        }

        //交易类型
        retVal[26]=(byte)0x9C;
        retVal[27]=(byte)0x01;
        retVal[28]=(byte)0x00;

        //交易序列号
        retVal[29]=(byte)0x9F;
        retVal[30]=(byte)0x41;
        retVal[31]=(byte)0x04;
        byte[] serialNumByt=new byte[4];
        Convert.putInt(serialNumByt,serialNum,0);
        for (int i=0;i<serialNumByt.length;i++){
            retVal[32+i]=serialNumByt[i];
        }

        return retVal;
    }
}
