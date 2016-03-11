package com.sdxp100;

import com.sdxp100.exception.CheckWrongException;
import com.sdxp100.exception.EtxWrongException;
import com.sdxp100.pck.DataPackage;
import com.sdxp100.pck.InfoArea;
import com.utils.CheckUtil;
import com.utils.Convert;

import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;

/**
 * Created by Firefly on 2016/2/21.
 */
public class Sdxp100Analysisbak {
    public static final byte STX=0X02;
    public static final byte ETX=0X03;

    private SerialPort serialPort;
    private InputStreamReadThread inputStreamReadThread=null;
    private ReadListener rl=null;
    private OutputStream os=null;

    public Sdxp100Analysisbak(SerialPort serialPort, ReadListener rl){
        this.serialPort=serialPort;
        this.rl=rl;
        this.os=this.serialPort.getOutputStream();
    }

    public void  start(){
        if(serialPort!=null && serialPort.getInputStream()!=null){
            inputStreamReadThread=new InputStreamReadThread(serialPort.getInputStream(), rl);
        }
        inputStreamReadThread.start();
    }

    public void close(){
        inputStreamReadThread.close();
    }

    public void sendDataPackage(DataPackage dp){

    }

    //数据包步骤
    private class DataPackageStep{
        private boolean stx;
        private boolean infoAreaLen;
        private boolean infoArea;
        private boolean check;
        private boolean etx;

        public boolean isStx() {
            return stx;
        }

        public void setStx(boolean stx) {
            this.stx = stx;
        }

        public boolean isInfoAreaLen() {
            return infoAreaLen;
        }

        public void setInfoAreaLen(boolean infoAreaLen) {
            this.infoAreaLen = infoAreaLen;
        }

        public boolean isInfoArea() {
            return infoArea;
        }

        public void setInfoArea(boolean infoArea) {
            this.infoArea = infoArea;
        }

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public boolean isEtx() {
            return etx;
        }

        public void setEtx(boolean etx) {
            this.etx = etx;
        }

        public void reset(){
            stx=false;
            infoAreaLen=false;
            infoArea=false;
            check=false;
            etx=false;
        }
    }

    //输入流读取线程
    private class InputStreamReadThread extends Thread{
        private InputStream is;
        private byte[] buffer;
        private ReadListener rl;
        private DataPackage dp;
        private DataPackageStep dps;
        private boolean isRun;

        public InputStreamReadThread(InputStream is,ReadListener rl){
            buffer=new byte[1024];
            dp=new DataPackage();
            dps=new DataPackageStep();

            this.is=is;
            this.rl=rl;
        }

        //byte转为信息区域
        private InfoArea bytToInfoArea(byte[] data,int offset){
            InfoArea retVal=new InfoArea();
            retVal.setByte(data,offset);
            return retVal;
        }

        public void close(){
            isRun=false;
        }

        @Override
        public void run() {
            int len=0;
            byte check=0;
            isRun=true;

            while(is!=null && isRun){
                try{
                    try{
                        Thread.sleep(20);
                    }catch(Exception e){

                    }

                    //如果数据包还没有获取到起始标志
                    if(!dps.isStx()){
                        if(is.available()>=1){
                            len=is.read(buffer,0,1);
                            if(buffer[0]==STX){
                                dp.setStx(buffer[0]);
                                dps.setStx(true);
                            }
                        }
                    }
                    //如果不存在信息区域长度
                    if(dps.isStx() && !dps.isInfoAreaLen()){
                        if(is.available()>=4){
                            len=is.read(buffer,0,4);
                            dp.setInfoAreaLen(Convert.getInt(buffer));
                            check=CheckUtil.xor(buffer,0,4);//计算校验码
                            dps.setInfoAreaLen(true);
                        }
                    }
                    //如果不存在信息区域
                    if(dps.isInfoAreaLen() && !dps.isInfoArea()){
                        if(is.available()>=dp.getInfoAreaLen()){
                            len=is.read(buffer,0,dp.getInfoAreaLen());
                            check= CheckUtil.xor(check, buffer, 0, dp.getInfoAreaLen());//计算校验码
                            dps.setInfoArea(true);
                            dp.setInfoArea(bytToInfoArea(buffer,0));
                        }
                    }
                    //如果不存在校验
                    if(dps.isInfoArea() && !dps.isCheck()){
                        if(is.available()>=1){
                            len=is.read(buffer,0,1);
                            if(check!=buffer[0]){
                                throw new CheckWrongException();
                            }
                            dps.setCheck(true);
                            dp.setCheck(buffer[0]);
                        }
                    }
                    //如果不存在校验
                    if(dps.isCheck() && !dps.isEtx()){
                        if(is.available()>=1){
                            len=is.read(buffer,0,1);
                            if(ETX!=buffer[0]){
                                throw new EtxWrongException();
                            }
                            if(rl!=null){
                                rl.onRead(dp.getInfoArea());
                            }
                            dps.reset();
                        }
                    }
                }catch (Exception e){
                    if(rl!=null){
                        dps.reset();
                        rl.onException(e);
                    }
                }
            }
        }
    }
}
