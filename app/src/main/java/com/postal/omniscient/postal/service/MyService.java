package com.postal.omniscient.postal.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import com.postal.omniscient.R;

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

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Notification n = new Notification();
        startForeground(1989, n);
        Log.i("MyMsg", "thread onStartCommand");
        AsyncL as = new AsyncL(getApplicationContext());
        as.forceLoad();

        as = null;

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MyMsg", "onDestroy");
        sendBroadcast(new Intent("YouWillNeverKillMe"));
    }

    public class AsyncL extends AsyncTaskLoader {

        public AsyncL(Context context) {
            super(context);
        }

        @Override
        public Object loadInBackground() {

            long sec = 1000 * 5;
            try {
                while (true) {
                    Thread.sleep(sec);
                    Log.i("MyMsg", "thread start AsyncL");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            Intent par = new Intent(getApplicationContext(), MyService.class);
//            startService(par);
//            par = null;
            return null;
        }

        @Override
        public void onCanceled(Object data) {
            super.onCanceled(data);
            Log.i("MyMsg", "thread start onCancelLoad");
        }

    }
}