package com.sdxp100;

import android.os.Handler;
import android.os.Message;

import com.sdxp100.pck.BalancePackage;
import com.sdxp100.pck.CallBackListener;
import com.sdxp100.pck.CheckStatePackage;
import com.sdxp100.pck.DataPackage;
import com.sdxp100.pck.InfoArea;
import com.sdxp100.pck.PayPackage;
import com.sdxp100.pck.PayResultPackage;
import com.sdxp100.pck.RegPackage;
import com.utils.CheckUtil;
import com.utils.Convert;

import java.util.Date;

import android_serialport_api.SerialPort;

/**
 * Created by Firefly on 2016/2/27.
 */
public class Sdxp100 {
    private static final int Msg_InfoArea=0;
    private static final int Msg_Exception=1;

    private SerialPort mSerialPort = null;
    private Sdxp100Device device = null;
    private CallBackListener listener=null;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Msg_InfoArea:
                    analysis(listener,(InfoArea)msg.obj);
                    break;
                case Msg_Exception:
                    exception(listener,(Exception)msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void analysis(final CallBackListener listener,InfoArea infoArea){
        //如果事件存在
        if(listener!=null){
            //获取返回的消息的数据
            byte[] abdata=infoArea.getAbdata();
            if(abdata!=null){
                int len=abdata.length;

                //超时指令
                if(len>=2 && abdata[0]==(byte)0xBE && abdata[1]==(byte)0xFB){
                    listener.timeOut(infoArea);
                }
                //状态
                if(len>=3 && abdata[1]==(byte)0x90 && abdata[2]==(byte)0x00){
                    listener.state(abdata[0]);
                }
                //交易成功，完成交易，D0标签，01长度，03表示交易完成
                if(len>=3 && abdata[0]==(byte)0xD0){
                    //返回签到报文
                    if(abdata[1]==0x01 && abdata[2]==0x03){
                        if(len>=6 && abdata[3]==(byte)0xD1 && abdata[4]==(byte)0x81){
                            //后面数据长度
                            int l=(int)(abdata[5]&0xFF);
                            //如果数据等于前面长度（5）+数据长度l+状态（2）
                            if(len>=6+l+2){
                                //复制数据到数组
                                //###############################待解释数据##########################################################
                                byte[] d=new byte[l];
                                for (int i=0;i<l;i++){
                                    d[i]=abdata[6+i];
                                }

                                //交易成功的返回状态  若交易异常则返回其他的状态
                                short s=Convert.getShort(abdata,6+l);

                                //把返回数据封装成对象
                                PayResultPackage payResultPackage=new PayResultPackage();
                                //#########################################################################################
                                //交易成功
                                listener.pay(payResultPackage);
                            }
                        }
                    }
                }
                //签到返回Type-length-value（TLV）格式
                if(len>=3 && abdata[0]==(byte)0xD2){
                    //返回签到报文
                    if(abdata[1]==0x01){
                        if(len>=5){
                            if(abdata[3]==(byte)0xD1){
                                //后面数据长度
                                int l=(int)(abdata[4]&0xFF);
                                //如果数据等于前面长度（5）+数据长度l+状态（2）
                                if(len>=5+l+2){
                                    //复制数据到数组
                                    byte[] d=new byte[l];
                                    for (int i=0;i<l;i++){
                                        d[i]=abdata[5+i];
                                    }

                                    //交易成功的返回状态  若交易异常则返回其他的状态
                                    short s=Convert.getShort(abdata,5+l);
                                    //签到
                                    if(abdata[2]==0x01){
                                        listener.reg(d,(int)(s&0xFFFF));
                                    }else if(abdata[2]==0x02){
                                        listener.aid(d,(int)(s&0xFFFF));
                                    }else if(abdata[2]==0x03){
                                        listener.pk(d,(int)(s&0xFFFF));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void exception(final CallBackListener listener,Exception exception){
        //如果事件存在
        if(listener!=null){
            listener.exception(exception);
        }
    }

    public Sdxp100(SerialPort serialPort,final CallBackListener listener){
        this.mSerialPort=serialPort;
        this.listener=listener;

        device = new Sdxp100Device(mSerialPort, new ReadListener() {
            @Override
            public void onRead(InfoArea infoArea) {
                Message m = new Message();
                m.what = Msg_InfoArea;
                m.obj=infoArea;
                Sdxp100.this.handler.sendMessage(m);
            }

            @Override
            public void onException(Exception exception) {
                Message m = new Message();
                m.what = Msg_Exception;
                m.obj=exception;
                Sdxp100.this.handler.sendMessage(m);
            }
        });
    }

    //查询状态指令
    public void checkState()throws Exception{
        checkState((byte) 0, (byte) 0, (byte) 0x0A);
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

    //签到指令
    public void reg()throws Exception{
        reg((byte) 0, (byte) 1, (byte) 0x0A);
    }

    //签到指令
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

    //更新aid
    public void updateAid()throws Exception{
        reg((byte) 02, (byte) 02, (byte) 0x0A);
    }

    //更新公钥
    public void updatePk()throws Exception{
        reg((byte) 02, (byte) 03, (byte) 0x0A);
    }

    public void pay(int serialNum,int money)throws Exception{
        pay(serialNum, money, new Date(), (byte) 0x0A);
    }

    public void pay(int serialNum,int money,Date payDate)throws Exception{
        pay(serialNum,money,payDate,(byte)0x0A);
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

    //结算指令
    public void balance()throws Exception{
        balance((byte)0x0A);
    }
    //结算指令
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

//        byte[] d=dp.toByte();
//        String str = "";
//        for (int i = 0; i < d.length; i++) {
//            String hex = Integer.toHexString(d[i] & 0xFF);
//            if (hex.length() == 1) {
//                hex = '0' + hex;
//            }
//            str += hex + " ";
//        }

        device.sendDataPackage(dp);
    }
}
