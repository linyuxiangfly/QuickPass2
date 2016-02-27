package com.sdxp100;

import com.sdxp100.pck.BalancePackage;
import com.sdxp100.pck.CheckStatePackage;
import com.sdxp100.pck.DataPackage;
import com.sdxp100.pck.InfoArea;
import com.sdxp100.pck.PayPackage;
import com.sdxp100.pck.RegPackage;
import com.utils.CheckUtil;
import com.utils.Convert;

import java.util.Date;

import android_serialport_api.SerialPort;

/**
 * Created by Firefly on 2016/2/27.
 */
public class Sdxp100 {
    private SerialPort mSerialPort = null;
    private Sdxp100Device device = null;

    public Sdxp100(SerialPort serialPort){
        this.mSerialPort=serialPort;

        device = new Sdxp100Device(mSerialPort, new ReadListener() {

            @Override
            public void onRead(InfoArea infoArea) {
                infoArea.getAbdata();
                int a=(int)infoArea.getAbdata()[0]&0xFF;

            }

            @Override
            public void onException(Exception exception) {

            }
        });
    }

    public void checkState()throws Exception{
        checkState((byte)0,(byte)0,(byte)0x0A);
    }
    //查询状态指令
    public void checkState(byte p1,byte p2,byte delay)throws Exception{
        InfoArea ia=new InfoArea();
        ia.setType((byte) 0x80);//串口通信方式
        ia.setbBwi(delay);//延时时间，毫秒

        CheckStatePackage csp=new CheckStatePackage();
        csp.setP1(p1);
        csp.setP2(p2);
        ia.setAbdata(csp.toByte());

        sendInfoArea(ia);
    }

    public void reg()throws Exception{
        reg((byte) 0, (byte) 1, (byte) 0x0A);
    }
    public void reg(byte p1,byte p2,byte delay)throws Exception{
        InfoArea ia=new InfoArea();
        ia.setType((byte) 0x80);//串口通信方式
        ia.setbBwi(delay);//延时时间，毫秒

        RegPackage rp=new RegPackage();
        rp.setP1(p1);
        rp.setP2(p2);
        ia.setAbdata(rp.toByte());

        sendInfoArea(ia);
    }

    //支付指令
    public void pay(int serialNum,int money,Date payDate,byte delay)throws Exception{
        InfoArea ia=new InfoArea();
        ia.setType((byte) 0x80);//串口通信方式
        ia.setbBwi(delay);//延时时间，毫秒

        PayPackage pp=new PayPackage();
        pp.setMoney(money);
        pp.setPayDate(payDate);
        pp.setSerialNum(serialNum);
        ia.setAbdata(pp.toByte());

        sendInfoArea(ia);
    }

    public void balance()throws Exception{
        balance((byte)0x0A);
    }
    public void balance(byte delay)throws Exception{
        InfoArea ia=new InfoArea();
        ia.setType((byte) 0x80);//串口通信方式
        ia.setbBwi(delay);//延时时间，毫秒

        BalancePackage rp=new BalancePackage();
        ia.setAbdata(rp.toByte());

        sendInfoArea(ia);
    }

    public void sendInfoArea(InfoArea ia)throws Exception{
        byte[] byt=ia.toByte();
        byte[] bytLen=new byte[4];
        Convert.putInt(bytLen, byt.length, 0);

        DataPackage dp=new DataPackage();

        dp.setStx(Sdxp100Analysis.STX);
        dp.setInfoAreaLen(byt.length);
        dp.setInfoArea(ia);

        //计算校验码
        byte check=CheckUtil.xor(bytLen,0,4);
        dp.setCheck(CheckUtil.xor(check, byt, 0, dp.getInfoAreaLen()));

        dp.setEtx(Sdxp100Analysis.ETX);

        byte[] d=dp.toByte();
        String str = "";
        for (int i = 0; i < d.length; i++) {
            String hex = Integer.toHexString(d[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            str += hex + " ";
        }

        device.sendDataPackage(dp);
    }
}
