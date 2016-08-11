package com.tal.mymovies.Services;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by ronen_abraham on 7/12/16.
 */
public class MyResultReceiver extends ResultReceiver {

    private Receiver mReceiver;

    public MyResultReceiver(Handler handler, Receiver receiver) {
        super(handler);
        this.mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }

    public interface Receiver {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

}
