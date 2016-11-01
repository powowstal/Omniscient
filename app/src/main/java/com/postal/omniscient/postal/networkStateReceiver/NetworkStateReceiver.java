package com.postal.omniscient.postal.networkStateReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.postal.omniscient.postal.ThreadIsAliveOrNot;
import com.postal.omniscient.postal.downloadFiles.DownloadFileRun;

import java.io.File;


public class NetworkStateReceiver extends BroadcastReceiver {

    private Boolean start_or_no;
    private String Msg = "MyMsg";
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            //Все файлы в папках на отправление
            start_or_no = true;
            start_or_no = new ThreadIsAliveOrNot("TreadConnect").liveORnot();//проверка работы потока коннекта

            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

            if (ni != null && ni.isConnected()) {// включен ли интернет
                if (!start_or_no) {//если поток не работает
                    startTransferFile(context, intent);// Начать загрузку файлов на сервер при появлении интернета
                    //если она уже не идет и существует конект с сервером
                }
            }
        }catch (Exception e){Log.i(Msg,"Error NetworkStateReceiver onReceive "+e);}
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
        }catch (Exception e){Log.i(Msg,"Error NetworkStateReceiver getAllFoldersFiles "+e);}
        return files_all;

    }
    //создаем объект для запуска потока загрузки файлов TreadConnect
    private void startTransferFile(Context context, Intent intent){
        try{
        DownloadFileRun downloadFile = new DownloadFileRun(getAllFoldersFiles(context), context, intent);
        Transfer ad = new Transfer(context, downloadFile);
        ad.forceLoad();
        }catch (Exception e){Log.i(Msg,"Error NetworkStateReceiver startTransferFile "+e);}
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
            }catch (Exception e){Log.i(Msg,"Error NetworkStateReceiver, Transfer loadInBackground "+e);}
            return null;
        }
    }
}
