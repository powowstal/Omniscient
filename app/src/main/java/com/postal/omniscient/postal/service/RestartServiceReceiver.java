package com.postal.omniscient.postal.service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import com.postal.omniscient.MainActivity;

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
        Toast toast = Toast.makeText(context,
                "Пора покормить кота!", Toast.LENGTH_SHORT);
        AsyncM ad = new AsyncM(context, toast);

        ad.forceLoad();
        ad = null;
        context.startService(new Intent(context.getApplicationContext(), MyService.class));



//            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//                if (MyService.class.getName().equals(service.service.getClassName())) {
//                    Log.i("MyMsg", "рабочий");
//                }
//                Log.i("MyMsg", " не ребочий");
//
//        }
    }
    public class AsyncM extends AsyncTaskLoader {


        public AsyncM(Context context) {
            super(context);

        }
        Toast toast;
        public AsyncM(Context applicationContext, Toast toast) {
            super(applicationContext);
            this.toast = toast;
        }

        @Override
        public Object loadInBackground() {

            toast.show();
            return null;
        }
    }


}