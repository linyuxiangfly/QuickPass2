package iso8583;

/**
 * Created by Firefly on 2016/3/12.
 */

import java.io.ByteArrayOutputStream;
/**
 * 编码与数据类型类
 *
 * @author LLH
 */
public class LoUtils
{
	/**
	 * 
	 */
	public static void byte2Bool(byte val,boolean[] byt,int offset){
		byt[offset+0]=(val&128)==128?true:false;
		byt[offset+1]=(val&64)==64?true:false;
		byt[offset+2]=(val&32)==32?true:false;
		byt[offset+3]=(val&16)==16?true:false;
		byt[offset+4]=(val&8)==8?true:false;
		byt[offset+5]=(val&4)==4?true:false;
		byt[offset+6]=(val&2)==2?true:false;
		byt[offset+7]=(val&1)==1?true:false;
	}
	
    /**
     * 将一个16字节数组转成128二进制数组
     *
     * @param b
     * @return
     */
    public static boolean[] byte2Bool(byte [] b)
    {
        boolean[] binary = new boolean [b.length * 8];
        
        for(int i=0;i<b.length;i++){
        	byte2Bool(b[i],binary,8*i);
        }
        
        return binary;
    }

    /**
     * bytes转换成十六进制字符串
     *
     * @param b
     * @return
     */
    public static String byte2HexStr(byte [] b)
    {
        String hs = "";
        String stmp = "";
        for (int n = 0;n < b.length;n++ )
        {
            stmp = (Integer.toHexString (b [n] & 0XFF));
            if(stmp.length () == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase ();
    }

    private static byte uniteBytes(String src0, String src1)
    {
        byte b0 = Byte.decode ("0x" + src0).byteValue ();
        b0 = (byte) (b0 << 4);
        byte b1 = Byte.decode ("0x" + src1).byteValue ();
        byte ret = (byte) (b0 | b1);
        return ret;
    }

    /**
     * 十六进制字符串转换成bytes
     *
     * @param src
     * @return
     */
    public static byte [] hexStr2Bytes(String src)
    {
        int m = 0, n = 0;
        int l = src.length () / 2;
        byte [] ret = new byte [l];
        for (int i = 0;i < l;i++ )
        {
            m = i * 2 + 1;
            n = m + 1;
            ret [i] = uniteBytes (src.substring (i * 2,m),
                    src.substring (m,n));
        }
        return ret;
    }

    /**
     * 将String转成BCD码
     *
     * @param s
     * @return
     */
    public static byte [] StrToBCDBytes(String s)
    {

        if(s.length () % 2 != 0)
        {
            s = "0" + s;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream ();
        char [] cs = s.toCharArray ();
        for (int i = 0;i < cs.length;i += 2)
        {
            int high = cs [i] - 48;
            int low = cs [i + 1] - 48;
            baos.write (high << 4 | low);
        }
        return baos.toByteArray ();
    }

    /**
     * 将BCD码转成int
     *
     * @param b
     * @return
     */
    public static int bcdToint(byte [] b)
    {
        StringBuffer sb = new StringBuffer ();
        for (int i = 0;i < b.length;i++ )
        {
            int h = ((b [i] & 0xff) >> 4) + 48;
            sb.append ((char) h);
            int l = (b [i] & 0x0f) + 48;
            sb.append ((char) l);
        }
        return Integer.parseInt (sb.toString ());
    }

}