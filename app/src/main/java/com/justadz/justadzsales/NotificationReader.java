package com.justadz.justadzsales;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;

import com.justadz.justadzsales.Postingdata.ConnectingTask;
import com.justadz.justadzsales.Postingdata.ConnectingTask.CheckNotify;

/**
 * Created by Admin on 23-May-16.
 */
public class NotificationReader extends Service {
    String IMEI;
    TelephonyManager tm;
    Thread mythread;
    int sd = 0;
    ConnectingTask task;

    @Override
    public void onCreate() {
        super.onCreate();
        task = new ConnectingTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = tm.getDeviceId();
        CheckNotify checkNotify = task.new CheckNotify(IMEI, NotificationReader.this);
        checkNotify.execute();
        stopSelf();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
