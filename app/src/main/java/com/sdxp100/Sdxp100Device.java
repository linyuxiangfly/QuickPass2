package com.sdxp100;

import com.sdxp100.pck.DataPackage;
import com.sdxp100.pck.InfoArea;

import java.io.IOException;
import java.io.OutputStream;

import android_serialport_api.SerialPort;

/**
 * Created by Firefly on 2016/2/21.
 */
public class Sdxp100Device {
    private SerialPort serialPort=null;
    private Sdxp100Analysis analysis=null;
    private OutputStream os=null;

    public Sdxp100Device(SerialPort serialPort,ReadListener rl){
        this.serialPort=serialPort;
        os=serialPort.getOutputStream();

        analysis=new Sdxp100Analysis(serialPort,rl);
        analysis.start();
    }

    public void sendDataPackage(DataPackage dp) throws IOException {
        if(os!=null){
            os.write(dp.toByte());
            os.flush();
        }
    }
}
