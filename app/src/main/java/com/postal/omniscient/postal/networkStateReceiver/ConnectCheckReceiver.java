package com.postal.omniscient.postal.networkStateReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.postal.omniscient.postal.ThreadIsAliveOrNot;

public class ConnectCheckReceiver extends BroadcastReceiver {
    final int SDK_INT = Build.VERSION.SDK_INT;
    private static String Msg = "MyMsg";
    private Boolean start_or_no;
    public ConnectCheckReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // an Intent broadcast.
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            Intent myIntent = new Intent(context, ConnectCheckReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);

            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

            if (ni != null && ni.isConnected()) {
                start_or_no = new ThreadIsAliveOrNot("TreadConnect").liveORnot();
                if (!start_or_no) {
                    Log.d(Msg, "        ConnectCheckReceiver стартанулся ");
                    if (SDK_INT < Build.VERSION_CODES.KITKAT) {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 10, pendingIntent);
                    } else if (Build.VERSION_CODES.KITKAT <= SDK_INT && SDK_INT < Build.VERSION_CODES.M) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 10, pendingIntent);
                    } else if (SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 10, pendingIntent);
                    }
                }
            }

            String requiredPermission = "notGiveUpKeepConnected";
            context.sendBroadcast(new Intent(requiredPermission));//запускаем NetworkStateReceiver для попытки возобновления конекта
        }catch (Exception e){Log.i(Msg, "Error ConnectCheckReceiver "+ e );}
    }
}
