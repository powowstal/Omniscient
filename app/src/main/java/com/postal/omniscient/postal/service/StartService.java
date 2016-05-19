package com.postal.omniscient.postal.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

public class StartService extends Service {
    public StartService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        Intent hideIntent = new Intent(this, MyService.class);
//        startService(hideIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Notification n = new Notification(); //ОТЕТО ХЗ ОСТАВЛЯТЬ?
//        startForeground(1989, n);
        //return super.onStartCommand(intent, flags, startId);
        //START SERVICE
        AsyncM ad = new AsyncM(getApplicationContext());
        sendBroadcast(new Intent("YouWillNeverKillMe"));

//        ad = new AsyncM(getApplicationContext());
//        ad.forceLoad();
//
//        ad = new AsyncM(getApplicationContext());
//        ad.forceLoad();


        return START_NOT_STICKY;
    }
    public class AsyncM extends AsyncTaskLoader {


        public AsyncM(Context context) {
            super(context);

        }
        public AsyncM(Context applicationContext, Toast toast) {
            super(applicationContext);
        }

        @Override
        public Object loadInBackground() {

            sendBroadcast(new Intent("YouWillNeverKillMe"));
            Log.i("MyMsg", " StartService start");
//            Intent par = new Intent(getApplicationContext(), MyService.class);
//            startService(par);
//            par = null;
            return null;
        }
    }
}
