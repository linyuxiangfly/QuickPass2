package com.sdxp100;

import com.sdxp100.pck.InfoArea;

/**
 * Created by Firefly on 2016/2/21.
 */
public interface ReadListener {
    public void onRead(InfoArea infoArea);
    public void onException(Exception exception);
}
