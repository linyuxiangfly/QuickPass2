package iso8583;

import android.util.Log;

import com.utils.Convert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Firefly on 2016/3/12.
 */
public class BitMapiso {
    public static final int TYPE_ASC=1;
    public static final int TYPE_BCD=2;
    public static final int TYPE_BIN=3;

    public static final int LEN_TYPE_FIXED=0;//非变长
    public static final int LEN_TYPE_LLVAR=2;//2位变长
    public static final int LEN_TYPE_LLLVAR=3;//3位变长


    private static int tpduLen=5;
    private static int headLen=6;
    private static int msgTypeLen=2;
    private static int bitMapLen=8;

    public static int getTpduLen() {
        return tpduLen;
    }

    public static void setTpduLen(int tpduLen) {
        BitMapiso.tpduLen = tpduLen;
    }

    public static int getHeadLen() {
        return headLen;
    }

    public static void setHeadLen(int headLen) {
        BitMapiso.headLen = headLen;
    }

    public static int getMsgTypeLen() {
        return msgTypeLen;
    }

    public static void setMsgTypeLen(int msgTypeLen) {
        BitMapiso.msgTypeLen = msgTypeLen;
    }

    public static int getBitMapLen() {
        return bitMapLen;
    }

    public static void setBitMapLen(int bitMapLen) {
        BitMapiso.bitMapLen = bitMapLen;
    }

    /*
     * 查找对应的位信息
     */
    private static BitMap getBitMap(BitMap[] config,int bit){
        BitMap retVal=null;
        for(BitMap bm:config){
            if(bm.getBit()==bit){
                retVal=bm;
                break;
            }
        }
        return retVal;
    }

    /**
     * 解析请求包
     * @param body
     * @param config
     * @return List
     */
    @SuppressWarnings("unchecked")
    public static List<BitMap> unpackRequest(byte[] body, BitMap[] config) {
        int mapStart=tpduLen+headLen+msgTypeLen;
        int bodyStart=0;

        // 取得位图
        byte[] map = null;
        byte[] map8 = new byte[8];
        System.arraycopy(body, mapStart, map8, 0, 8);
        boolean[] bmap = LoUtils.byte2Bool(map8);
        //如果地图第一个位为true则取16个字节生成
        if (bmap[0]) {
            // 如果第一位为1，则是可扩展位图，设为16字节长度。
            map = new byte[16];
            System.arraycopy(body, mapStart, map, 0, 16);
            bmap = LoUtils.byte2Bool(map);

            bodyStart=mapStart+16;
        } else {
            map = map8;
            bodyStart=mapStart+8;
        }


        List outList = new ArrayList();
        // 取得除信息类型以外的包信息。也就是取得位图的初始位置。
        byte[] realbody = new byte[body.length - bodyStart];
        System.arraycopy(body, bodyStart, realbody, 0, realbody.length);

        int offset = 0;
        for (int i = 1; i < bmap.length+1; i++) {
            if (bmap[i-1]) {
                //查找位的位信息
                BitMap bm=getBitMap(config,i);
                //如果没有找到对应的位信息对象则退出循环
                if(bm==null){
                    break;
                }

                byte[] nextData = null;

                //如果位可变长
                if (bm.getVariable() > 0) {
                    //取出变长部分的值。
                    int varLen = bm.getVariable();
                    if (bm.getBittype() == TYPE_BCD) {
                        if(varLen%2!=0){
                            varLen++;
                        }
                        varLen=varLen/2;
                    }

                    byte[] varValue = new byte[varLen];
                    System.arraycopy(realbody, offset, varValue, 0, varValue.length);
                    //编移
                    offset += varLen;


                    int datLen = 0;
                    if (bm.getBittype() == TYPE_BCD) {
                        datLen = LoUtils.bcdToint(varValue);

                        //如果数据类型为二进制则长度不除2
                        if(bm.getDattype()!=TYPE_BIN){
                            //长度取半轩为ASC的长度是根据16进制表示字符的长度，如 A2 B3其实是2个字节，但是datLen是用4来表示
                            if(datLen%2!=0){
                                datLen++;
                            }
                            datLen=datLen/2;
                        }

                    } else {
                        datLen=Convert.getInt(varValue, 0);
                    }

                    // 取出变长部分后带的值。
                    nextData = new byte[datLen];

                    System.arraycopy(realbody, offset, nextData, 0,nextData.length);
                    offset += nextData.length;
                } else {
                    nextData = new byte[bm.getLen()];
                    System.arraycopy(realbody, offset, nextData, 0,nextData.length);
                    offset += bm.getLen();
                }

//                printByte(bm.getBit()+"",nextData);

                bm.setDat(nextData);
                outList.add(bm);
            }
        }

        return outList;
    }

//    public static void printByte(String title,byte[] d){
//        String str = "";
//        for (int i = 0; i < d.length; i++) {
//            String hex = Integer.toHexString(d[i] & 0xFF);
//            if (hex.length() == 1) {
//                hex = '0' + hex;
//            }
//            str += hex + " ";
//        }
//        Log.i(title,str);
//    }
//
//    /**
//     * 打包响应包,不包括消息类型
//     * @param list
//     * @return byte[]
//     */
//    @SuppressWarnings("unchecked")
//    public static byte[] PackResponse(List list) {
//        int len = 16;
//        for (int i = 0; i < list.size(); i++) {
//            BitMap bitMap = (BitMap) list.get(i);
//            // 计算请求包总长度
//            if (bitMap.getBittype() == 2) {
//                if (bitMap.getVariable() > 0) {
//                    len += bitMap.getVariable() - 1 + bitMap.getDat().length;
//                } else {
//                    len += bitMap.getVariable() + bitMap.getDat().length;
//                }
//            } else {
//                len += bitMap.getVariable() + bitMap.getDat().length;
//            }
//        }
//        byte[] body = new byte[len];
//        // 位图
//        boolean[] bbitMap = new boolean[129];
//        bbitMap[1] = true;
//        int temp = (bbitMap.length - 1) / 8;
//        for (int j = 0; j < list.size(); j++) {
//            BitMap bitMap = (BitMap) list.get(j);
//            bbitMap[bitMap.getBit()] = true;
//            byte[] bitmap = LoUtils.getByteFromBinary(bbitMap);
//            System.arraycopy(bitmap, 0, body, 0, bitmap.length);
//            // 数据
//            if (bitMap.getVariable() > 0) {
//                // 数据是可变长的:拼变长的值
//                byte[] varValue = null;
//                if (bitMap.getBittype() == 2) {
//                    varValue = LoUtils.StrToBCDBytes(String.format("%0"+ bitMap.getVariable() + "d",bitMap.getDat().length));
//                } else {
//                    varValue = String.format("%0" + bitMap.getVariable() + "d",bitMap.getDat().length).getBytes();
//                }
//                System.arraycopy(varValue, 0, body, temp, varValue.length);
//                temp += varValue.length;
//                // 拼变长部分后所带的数的值。
//                System.arraycopy(bitMap.getDat(), 0, body, temp, bitMap.getDat().length);
//                temp += bitMap.getDat().length;
//            } else {
//                // 数据是固定长度的。
//                byte dat[] =new byte[bitMap.getLen()];
//                if (bitMap.getDat().length!=bitMap.getLen()){
//                    System.arraycopy(bitMap.getDat(), 0, dat, 0, bitMap.getLen());
//                }else{
//                    dat=bitMap.getDat();
//                }
//                System.arraycopy(dat, 0, body, temp, dat.length);
//                temp += bitMap.getDat().length;
//            }
//        }
//        return body;
//    }
}
