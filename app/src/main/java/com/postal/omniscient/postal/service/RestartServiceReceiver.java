package com.postal.omniscient.postal.service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Alexandr on 12.05.2016.
 */
public class RestartServiceReceiver extends BroadcastReceiver
{

    private static final String TAG = "RestartServiceReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive");
        long sec = 1000 * 8;
        try {

            Thread.sleep(sec);
            Log.i("MyMsg", "RestartServiceReceiver");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        context.startService(new Intent(context.getApplicationContext(), MyService.class));



            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (MyService.class.getName().equals(service.service.getClassName())) {
                    Log.i("MyMsg", "рабочий");
                }
                Log.i("MyMsg", " не ребочий");

        }
    }


}