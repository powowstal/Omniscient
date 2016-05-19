package com.postal.omniscient.postal.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import com.postal.omniscient.MainActivity;
import com.postal.omniscient.postal.catchPhone.Call.PhoneCall;

/**
 * Created by Alexandr on 12.05.2016.
 */
public class RestartServiceReceiver extends BroadcastReceiver
{

    private static final String TAG = "RestartServiceReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {


        Log.e("MyMsg", " Receive Start");
        long sec = 1000 * 2;

        AsyncR ad = new AsyncR(context);

        try {

                    Thread.sleep(sec);// ВРЕМЯ СНА ЦЫКЛА ВКЛЮЧИТЬ 1 ЧАС

                     ad.forceLoad();

                   // ad = null;

        } catch (Exception e) {
            Log.e("MyMsg","1"+ e.toString());
        }

        Log.i("MyMsg", " Resiver END");

        //context.startService(new Intent(context.getApplicationContext(), MyService.class));



//            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//                if (MyService.class.getName().equals(service.service.getClassName())) {
//                    Log.i("MyMsg", "рабочий");
//                }
//                Log.i("MyMsg", " не ребочий");
//
//        }

    }
    public class AsyncR extends AsyncTaskLoader {


        public AsyncR(Context context) {
            super(context);

        }
        Toast toast;
        public AsyncR(Context applicationContext, Toast toast) {
            super(applicationContext);
            this.toast = toast;
        }

        @Override
        public Object loadInBackground() {

            long sec = 1000 * 20;
            try {
               // while(true) {
                    //Thread.sleep(sec);

                    Log.i("MyMsg", " Resiver TASC START");
                //}
            } catch (Exception e) {
                Log.e("MyMsg","2"+ e.toString());
            }

            Intent par = new Intent(getContext(), MyService.class);
            getContext().startService(par);
            //getContext().sendBroadcast(new Intent("YouWillNeverKillMe"));
            return null;
        }
    }


}