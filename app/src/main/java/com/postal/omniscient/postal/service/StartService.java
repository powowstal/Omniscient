package com.postal.omniscient.postal.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.AsyncTaskLoader;
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        //START SERVICE
        AsyncM ad = new AsyncM(getApplicationContext());
        ad.forceLoad();

        ad = new AsyncM(getApplicationContext());
        ad.forceLoad();

        ad = new AsyncM(getApplicationContext());
        ad.forceLoad();

        ad = null;
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



            Intent par = new Intent(getApplicationContext(), MyService.class);

            startService(par);
            par = null;
            return null;
        }
    }
}
