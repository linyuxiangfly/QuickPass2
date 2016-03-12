package iso8583;

/**
 * Created by Firefly on 2016/3/12.
 */
public class BitMap {
    private int bit; //位
    private int bittype; //数据类型 1 ascii 2 binary
    private int variable; //是否变长0 不是 2 两位变长 3 三位变长
    private int len; //数据长度
    private byte[] dat; //数据
    
    public BitMap(){}
    
    public BitMap(int bit,int bittype,int len,int variable){
    	this.bit=bit;
    	this.bittype=bittype;
    	this.variable=variable;
    	this.len=len;
    }

    public int getBit() {
        return bit;
    }
    public void setBit(int bit) {
        this.bit = bit;
    }
    public int getBittype() {
        return bittype;
    }
    public void setBittype(int bittype) {
        this.bittype = bittype;
    }
    public int getVariable() {
        return variable;
    }
    public void setVariable(int variable) {
        this.variable = variable;
    }
    public byte[] getDat() {
        return dat;
    }
    public void setDat(byte[] dat) {
        this.dat = dat;
    }
    public int getLen() {
        return len;
    }
    public void setLen(int len) {
        this.len = len;
    }
}
