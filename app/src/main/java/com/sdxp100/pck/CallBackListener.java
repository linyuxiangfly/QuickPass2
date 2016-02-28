package com.sdxp100.pck;

/**
 * Created by Firefly on 2016/2/28.
 * 返回事件
 */
public interface CallBackListener {
    //超时
    public void timeOut(InfoArea infoArea);

    //查询状态
    public void state(byte state);

    //异常
    public void exception(Exception exception);
}
