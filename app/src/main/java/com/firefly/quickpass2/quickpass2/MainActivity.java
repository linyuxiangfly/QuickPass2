package com.firefly.quickpass2.quickpass2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.opa.params.BaseParam;
import com.opa.returns.TransparentReturn;
import com.opa.returns.UpdateParametersReturn;
import com.opa.services.Host;
import com.sdxp100.ReadListener;
import com.sdxp100.Sdxp100;
import com.sdxp100.Sdxp100Device;
import com.sdxp100.pck.CallBackListener;
import com.sdxp100.pck.DataPackage;
import com.sdxp100.pck.InfoArea;
import com.sdxp100.pck.PayResultPackage;
import com.sdxp100.pck.ReturnTransInfo;
import com.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import android_serialport_api.SerialPort;

public class MainActivity extends AppCompatActivity {
    private static SimpleDateFormat formatDate = new SimpleDateFormat("yyMMdd");
    private int index=0;

    private Button button1 = null;
    private Button button2 = null;
    private Button button3 = null;
    private Button button4 = null;
    private Button button5 = null;
    private TextView text1=null;
    private SerialPort mSerialPort = null;
    private Sdxp100 sdxp = null;
    private Host host=null;

    private static final int HOST_INIT=1;
    private static final int TRANS=2;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            switch (msg.what) {
                case HOST_INIT:
                    try {
                        host=new Host("http://61.141.235.82:8190/OPARest/Host/", BaseParam.SRC_PC,"425441954110135","51435790",
                                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvVyN0u0jl4eprODnf60s"+
                                        "OfVZZJTzn4Txcina1IpAN1ZxgJZdXHe8j3gEIp+tbL9zEmcprVkaL6MqSKmB2fOq"+
                                        "uyjDVymxkZ5qpkTnkPS+XQD6IRx/Z3IKSZhuHcKZuT9IXLHipvrDUkCy1EDsNyC+"+
                                        "Ly+1LC48jfQvuc/Kz4fU8m0uPLoRRSslbxheKFUUqhGTmoCKyVfVH+igbaz/DJFD"+
                                        "4PbufYkZXVCOI0Oi7rv/Xef1Z+xIdpdHk6WbpV+uSUIQ0EUr+xYqc9J7tGA6PQxY"+
                                        "Gpr0Rrhkt9dSyHl4lU6U1XpOr7G/FNVT5vKR1RpfJg6eaoP+jT9PGm1CLxIFoI7M"+
                                        "yQIDAQAB");
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case TRANS:
                    Bundle bundle= msg.getData();
                    TransparentReturn tr=(TransparentReturn)bundle.getSerializable("data");
                    try{
                        byte[] byt=StringUtil.hexStr2Bytes(tr.getData().getServerRespData());
                        sdxp.transparent(byt);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    private void sendData(int what,Serializable obj){
        Message msg=new Message();
        msg.what=what;
        Bundle bundle = new Bundle();
        bundle.putSerializable("data",obj);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        sendData(HOST_INIT, null);

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

                @Override
                public void trans(ReturnTransInfo returnTransInfo) {
                    try{
                        TransparentReturn tr=host.transparent(StringUtil.byte2HexStr(returnTransInfo.getAbdata()));
                        sendData(TRANS, tr);
                    }catch(Exception e){

                    }
                }
            });
//            sdxp.checkState();
//            sdxp.reg();
//            sdxp.updateAid();
//            sdxp.updatePk();
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
        button4 = (Button) this.findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    sdxp.reg();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        button5 = (Button)this.findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    sdxp.balance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                try {
////                    String data="0079600000000260310031110E0810003800010AC0001400002121464803110800155800323134363438353933343031303035313433353739303432353434313935343131303133350011000000060030004081703A74A7F8DBD31ADE741A23E7654F522C41264EEEE335695F8ABD0000000000000000A51E8ADF";
//                    String data="0079600000000260310031110E0810003800010AC000140000112253120311080015580032323533313232323131353730303531343335373930343235343431393534313130313335001100000006003000405662529A0E75ACBFAA6AE9F2DE8DF512571E85E280092BDA22180CD0000000000000000039ACBA8C";
//                    byte[] d= StringUtil.hexStr2Bytes(data);
//                    sdxp.transparent(d);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
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
        Log.i("showInfo",msg);
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
