package com.android.sdk.shcore;

public interface MyHttpListener {
    public final static int ERROR = 0;
    public final static int FINISH = 1;

    void onCompleted(String recvStr, int eventCode);
}
