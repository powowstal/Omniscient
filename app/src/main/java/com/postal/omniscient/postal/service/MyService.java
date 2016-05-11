package com.postal.omniscient.postal.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
    private String Msg = "MyMsg";

    public MyService() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(Msg, "destroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(Msg, "start");
        Toast toast = Toast.makeText(getApplicationContext(),
                "Пора покормить кота!", Toast.LENGTH_SHORT);
        Long sec = Long.valueOf(1000*3);
        try {
            Thread.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        toast.show();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(Msg, "create");

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }
}
