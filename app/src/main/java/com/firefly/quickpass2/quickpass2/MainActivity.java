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
import com.sdxp100.pck.PayResultPackage;

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
    private Button button2 = null;
    private Button button3 = null;
    private TextView text1=null;
    private SerialPort mSerialPort = null;
    private Sdxp100 sdxp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        text1=(TextView)this.findViewById(R.id.textView);
        showInfo("");

        try {
            mSerialPort = new SerialPort(new File("/dev/ttyUSB0"), 115200, 0);
            sdxp = new Sdxp100(mSerialPort, new CallBackListener() {
                @Override
                public void timeOut(InfoArea infoArea) {
                    showInfo("超时");
                }

                @Override
                public void pay(PayResultPackage payResultPackage) {
                    showInfo("卡号:"+payResultPackage.getCard()+",金额:"+payResultPackage.getMoney());
                }

                @Override
                public void state(byte state) {
                    int s=(int)(state&0xFF);
                    showInfo("状态:"+Integer.toBinaryString(s));
                }

                @Override
                public void reg(byte[] data,int state) {
                    String str = "";
                    for (int i = 0; i < data.length; i++) {
                        String hex = Integer.toHexString(data[i] & 0xFF);
                        if (hex.length() == 1) {
                            hex = '0' + hex;
                        }
                        str += hex + " ";
                    }
                    showInfo("状态:"+state + ",签到:"+str);
                }

                @Override
                public void aid(byte[] data, int state) {
                    String str = "";
                    for (int i = 0; i < data.length; i++) {
                        String hex = Integer.toHexString(data[i] & 0xFF);
                        if (hex.length() == 1) {
                            hex = '0' + hex;
                        }
                        str += hex + " ";
                    }
                    showInfo("状态:"+state + ",aid:"+str);
                }

                @Override
                public void pk(byte[] data, int state) {
                    String str = "";
                    for (int i = 0; i < data.length; i++) {
                        String hex = Integer.toHexString(data[i] & 0xFF);
                        if (hex.length() == 1) {
                            hex = '0' + hex;
                        }
                        str += hex + " ";
                    }
                    showInfo("状态:"+state + ",pk:"+str);
                }

                @Override
                public void exception(Exception exception) {
                    showInfo(exception.getMessage());
                }
            });
//            sdxp.checkState();
//            sdxp.reg();
//            sdxp.updateAid();
            sdxp.updatePk();
//            sdxp.balance();
        } catch (Exception e) {
            showInfo(e.getMessage());
        }

        button1 = (Button) this.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    index++;
                    sdxp.pay(index, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        button2 = (Button) this.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    index++;
                    sdxp.pay(index,2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        button3 = (Button) this.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    index++;
                    sdxp.pay(index,3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
