package com.sdxp100.pck;

/**
 * Created by Firefly on 2016/2/28.
 * 返回事件
 */
public interface CallBackListener {
    //超时
    public void timeOut(InfoArea infoArea);

    //交易成功
    public void pay(PayResultPackage payResultPackage);

    //查询状态
    public void state(byte state);

    //签到返回
    public void reg(byte[] data,int state);

    //aid返回
    public void aid(byte[] data,int state);

    //pk返回
    public void pk(byte[] data,int state);

    //异常
    public void exception(Exception exception);
}
