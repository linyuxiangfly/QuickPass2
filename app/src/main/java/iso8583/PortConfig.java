package iso8583;

/**
 * Created by Firefly on 2016/3/12.
 */
public class PortConfig {
    /**
     * 存放所有接口的配置信息
     * [][0] bit     位:在Map中的位
     * [][1] type   类型:1 ascii 2 binary
     * [][2] len    长度:(对定长有效)
     * [][3] varLen 变长:0非变长 2位变长 3位变长
     */
// 定义一个二位数组存放配置信息。
    public static final BitMap[] config= {
            new BitMap(2,BitMapiso.TYPE_BCD,0,BitMapiso.LEN_TYPE_LLVAR),//主账号  N..19(LLVAR)，2个字节的长度值＋最大19个字节的主账号，压缩时用BCD码表示的1个字节的长 度值＋用左靠BCD码表示的最大10个字节的主账号。
            new BitMap(3,BitMapiso.TYPE_BCD,3,BitMapiso.LEN_TYPE_FIXED),//交易处理码  N6，6个字节的定长数字字符域，压缩时用BCD码表示的3个字节的定长域。
            new BitMap(4,BitMapiso.TYPE_BCD,6,BitMapiso.LEN_TYPE_FIXED),
            new BitMap(11,BitMapiso.TYPE_BCD,3,BitMapiso.LEN_TYPE_FIXED),
            new BitMap(12,BitMapiso.TYPE_BCD,3,BitMapiso.LEN_TYPE_FIXED),
            new BitMap(13,BitMapiso.TYPE_BCD,2,BitMapiso.LEN_TYPE_FIXED),
            new BitMap(14,BitMapiso.TYPE_BCD,2,BitMapiso.LEN_TYPE_FIXED),
            new BitMap(15,BitMapiso.TYPE_BCD,2,BitMapiso.LEN_TYPE_FIXED),
            new BitMap(22,BitMapiso.TYPE_BCD,2,BitMapiso.LEN_TYPE_FIXED),
            new BitMap(23,BitMapiso.TYPE_BCD,2,BitMapiso.LEN_TYPE_FIXED),
            new BitMap(25,BitMapiso.TYPE_BCD,1,BitMapiso.LEN_TYPE_FIXED),
            new BitMap(26,BitMapiso.TYPE_BCD,1,BitMapiso.LEN_TYPE_FIXED),
            new BitMap(32,BitMapiso.TYPE_BCD,0,BitMapiso.LEN_TYPE_LLVAR),
            new BitMap(35,BitMapiso.TYPE_BCD,0,BitMapiso.LEN_TYPE_LLVAR),
            new BitMap(36,BitMapiso.TYPE_BCD,0,BitMapiso.LEN_TYPE_LLLVAR),
            new BitMap(37,BitMapiso.TYPE_ASC,12,BitMapiso.LEN_TYPE_FIXED),
            new BitMap(39,BitMapiso.TYPE_ASC,2,BitMapiso.LEN_TYPE_FIXED),
            new BitMap(41,BitMapiso.TYPE_ASC,8,BitMapiso.LEN_TYPE_FIXED),
            new BitMap(42,BitMapiso.TYPE_ASC,15,BitMapiso.LEN_TYPE_FIXED),
            new BitMap(44,BitMapiso.TYPE_BCD,0,BitMapiso.LEN_TYPE_LLVAR),
            new BitMap(48,BitMapiso.TYPE_BCD,0,BitMapiso.LEN_TYPE_LLLVAR),
            new BitMap(49,BitMapiso.TYPE_ASC,3,BitMapiso.LEN_TYPE_FIXED),
            new BitMap(52,BitMapiso.TYPE_BIN,8,BitMapiso.LEN_TYPE_FIXED),
            new BitMap(53,BitMapiso.TYPE_BCD,8,BitMapiso.LEN_TYPE_FIXED),
            new BitMap(54,BitMapiso.TYPE_BCD,0,BitMapiso.LEN_TYPE_LLLVAR),
            new BitMap(55,BitMapiso.TYPE_BCD,0,BitMapiso.LEN_TYPE_LLLVAR),
            new BitMap(58,BitMapiso.TYPE_ASC,0,BitMapiso.LEN_TYPE_LLLVAR),
            new BitMap(60,BitMapiso.TYPE_BCD,0,BitMapiso.LEN_TYPE_LLLVAR),
            new BitMap(61,BitMapiso.TYPE_BCD,0,BitMapiso.LEN_TYPE_LLLVAR),
            new BitMap(62,BitMapiso.TYPE_BCD,0,BitMapiso.LEN_TYPE_LLLVAR),
            new BitMap(63,BitMapiso.TYPE_BCD,0,BitMapiso.LEN_TYPE_LLLVAR),
            new BitMap(64,BitMapiso.TYPE_BCD,8,BitMapiso.LEN_TYPE_FIXED)
//
//            {11,1,6,0},
//            {12,1,6,0},
//            {13,1,4,0},
//            {32,1,11,0},
//            {37,1,12,0},
//            {39,1,2,0},
//            {40,2,50,2},
//            {41,1,8,0},
//            {48,1,52,3},
//            {120,2,128,3},
    };
}
