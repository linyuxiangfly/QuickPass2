package com.firefly.quickpass2.quickpass2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sdxp100.ReadListener;
import com.sdxp100.Sdxp100;
import com.sdxp100.Sdxp100Device;
import com.sdxp100.pck.CallBackListener;
import com.sdxp100.pck.DataPackage;
import com.sdxp100.pck.InfoArea;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android_serialport_api.SerialPort;

public class MainActivity extends AppCompatActivity {
    private static SimpleDateFormat formatDate = new SimpleDateFormat("yyMMdd");
    private int index=0;

    private Button button1 = null;
    private TextView text1=null;
    private SerialPort mSerialPort = null;
    private Sdxp100 sdxp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            mSerialPort = new SerialPort(new File("/dev/ttyUSB0"), 115200, 0);
            sdxp = new Sdxp100(mSerialPort, new CallBackListener() {
                @Override
                public void timeOut(InfoArea infoArea) {
                    showInfo("超时");
                }
            });
//            sdxp.checkState();
//            sdxp.reg();
            sdxp.balance();
        } catch (Exception e) {
            showInfo(e.getMessage());
        }

        text1=(TextView)this.findViewById(R.id.textView);

        button1 = (Button) this.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                DataPackage dp=new DataPackage();
//                dp.setStx((byte) 0x02);
//                dp.setEtx((byte) 0x3);
//                dp.setInfoAreaLen(0x0E);
//                dp.setInfoArea();
//                try {
//                    device.sendDataPackage(dp);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                String dataStr = "02 00 00 00 2E 80 00 00 00 24 00 00 0A 00 00 BE 40 00 00 00 1E 9F 02 06 00 00 00 00 00 02 9A 03 14 11 07 5F 21 03 11 55 12 9C 01 00 9F 41 04 00 00 16 40 58 03";
//                byte[] data = strByt(dataStr);
//
//                if (mSerialPort != null) {
//                    OutputStream os = mSerialPort.getOutputStream();
//                    InputStream is = mSerialPort.getInputStream();
//
//                    if (os != null) {
//                        try {
//                            os.write(data);
//                            os.flush();
//                        } catch (IOException e) {
//                            showInfo(e.getMessage());
//                        }
//                    }

//                    try {
//                        Thread.sleep(200);
//                    } catch (InterruptedException e) {
//                        showInfo(e.getMessage());
//                    }
//
//                    if (is != null) {
//                        try {
//                            byte[] buffer = new byte[4096];
//                            int readLen = 0;
//                            readLen = is.read(buffer);
//
//                            String str = "";
//                            for (int i = 0; i < readLen; i++) {
//                                String hex = Integer.toHexString(buffer[i] & 0xFF);
//                                if (hex.length() == 1) {
//                                    hex = '0' + hex;
//                                }
//                                str += hex + " ";
//                            }
//
//                            showInfo(str);
//                        } catch (IOException e) {
//                            showInfo(e.getMessage());
//                        }
//                    }
                    try {
                        index++;

                        sdxp.pay(index,12345,new Date(),(byte)0x02);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
//            }

        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void showInfo(String msg) {
        text1.setText(msg);
    }

    public byte[] strByt(String str) {
        String[] strs = str.split(" ");
        byte[] retVal = new byte[strs.length];
        for (int i = 0; i < strs.length; i++) {
            retVal[i] = Integer.valueOf(strs[i], 16).byteValue();
        }
        return retVal;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
