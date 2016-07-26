package com.postal.omniscient.postal.networkStateReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class ConnectCheckReceiver extends BroadcastReceiver {
    final int SDK_INT = Build.VERSION.SDK_INT;
    private static String Msg = "MyMsg";
    public ConnectCheckReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, ConnectCheckReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);

        boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                new Intent("notGiveUpConnectCheckReceiver"),
                PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmUp){Log.d(Msg, "ConnectCheckReceiver уже работает");}
        if(!alarmUp){Log.d(Msg, "ConnectCheckReceiver не работает еще");}

        Log.d(Msg, " ConnectCheckReceiver стартанулся ");
        if (SDK_INT < Build.VERSION_CODES.KITKAT) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+1000*1, pendingIntent);
        }
        else if (Build.VERSION_CODES.KITKAT <= SDK_INT  && SDK_INT < Build.VERSION_CODES.M) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+1000*1, pendingIntent);
        }
        else if (SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+1000*1, pendingIntent);
        }

        Log.i(Msg, "ConnectCheckReceiver start ");
        String requiredPermission = "notGiveUpKeepConnected";
        context.sendBroadcast(intent, requiredPermission);

    }
}
