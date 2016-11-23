package com.postal.omniscient.postal.service;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.postal.omniscient.postal.ThreadIsAliveOrNot;
import com.postal.omniscient.postal.downloadFiles.DownloadFileRun;

import java.io.File;


/**
 * Created by Alexandr on 12.05.2016.
 */
public class RestartServiceReceiver extends BroadcastReceiver {
    final int SDK_INT = Build.VERSION.SDK_INT;
    private static String Msg = "MyMsg";
    private Boolean start_or_no;
    @Override
    public void onReceive(Context context, Intent intent) {

        long sec = 1000 * 2;

        try {
            Thread.sleep(sec);// ВРЕМЯ СНА ЦЫКЛА
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            Intent myIntent = new Intent(context, RestartServiceReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);

            if (SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
            }
            else if (Build.VERSION_CODES.KITKAT <= SDK_INT  && SDK_INT < Build.VERSION_CODES.M) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
            }
            else if (SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
            }

            start_or_no = true;
            start_or_no = new ThreadIsAliveOrNot("TreadConnect").liveORnot();//проверка работы потока коннекта

            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

            if (!start_or_no) {
                if (ni != null && ni.isConnected()) {// включен ли интернет
                    if (!start_or_no) {//если поток не работает
//                        startTransferFile(context, intent);// Начать загрузку файлов на сервер при появлении интернета
//                        //если она уже не идет и существует конект с сервером
///////////////////////////////////////////////////////////////////////////////////////////
                        Log.i(Msg, "notGiveUp start ");
                        String requiredPermission = "notGiveUpKeepConnected";
                        context.sendBroadcast(new Intent(requiredPermission));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(Msg,"Error RestartServiceReceiver onRecive "+ e.toString());
        }

    }

    /**  получаем пути к файлам для отправки на сервер     */
    private  File[] getAllFoldersFiles (Context context){
        File[] files_all = null; //массив папок в которых храняться файлы для отправки
        File path; //путь к папке программы
        File f; //путь к папке где сохраняются все файлы для отправки на сервер
        try{
            //записать в массив и передать на отправку
            path = context.getFilesDir();
            f = new File(String.valueOf(path+"/Omniscient"));
            files_all = f.listFiles();
        }catch (Exception e){Log.i("MyMsg","Error RestartServiceReceiver getAllFoldersFiles "+e);}
        return files_all;

    }
    //создаем объект для запуска потока загрузки файлов TreadConnect
    private void startTransferFile(Context context, Intent intent){
        try{
            DownloadFileRun downloadFile = new DownloadFileRun(getAllFoldersFiles(context), context, intent);
            Transfer ad = new Transfer(context, downloadFile);
            ad.forceLoad();
        }catch (Exception e){Log.i("MyMsg","Error RestartServiceReceiver startTransferFile "+e);}
    }

    public class Transfer extends AsyncTaskLoader {

        private DownloadFileRun downloadFile;

        public Transfer (Context context, DownloadFileRun downloadFile) {
            super(context);
            this.downloadFile = downloadFile;

        }
        @Override
        public Object loadInBackground() {
            try{
                Thread startDownload = new Thread(downloadFile, "TreadConnect");
                //запускаем поток для загрузки файлов на сервер
                startDownload.start();
            }catch (Exception e){Log.i("MyMsg","Error RestartServiceReceiver, Transfer loadInBackground "+e);}
            return null;
        }
    }
}