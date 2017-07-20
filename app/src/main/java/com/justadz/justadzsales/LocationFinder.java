package com.justadz.justadzsales;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Admin on 19-May-16.
 */
public class LocationFinder extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent(context, NotificationReader.class);
        context.startService(intent);
    }
}
