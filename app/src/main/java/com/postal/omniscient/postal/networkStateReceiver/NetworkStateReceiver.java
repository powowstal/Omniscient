package com.postal.omniscient.postal.networkStateReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import com.postal.omniscient.MainActivity;
import com.postal.omniscient.postal.downloadFiles.DownloadFileRun;
import com.postal.omniscient.postal.service.StartService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

public class NetworkStateReceiver extends BroadcastReceiver {
//    public NetworkStateReceiver() {
//    }
    private String Msg = "MyMsg";
    @Override
    public void onReceive(Context context, Intent intent) {
//Все файлы в папках на отправление
        getAllFoldersFiles(context);


        Log.d(Msg, "Network connectivity change");

        Integer i = 0;
        try {
            String a = readFromFile(context);
            i = Integer.parseInt(a);
        }catch (Exception e){}
        i++;
        if(i>4){i=1;}
        writeFromFile(context, i.toString());


        if (intent.getExtras() != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();







            if (ni != null && ni.isConnectedOrConnecting()) {
                if(i==4) {
                    Log.d(Msg, "FILE OUTPUT " + readFromFile(context));
                    Log.i(Msg, "Network " + ni.getTypeName() + " connected");


                    startTransferFile(context);// Начать загрузку файлов на сервер при появлении интернета
                }
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d(Msg, "There's no network connectivity");
            }
            // throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    /**  получаем пути к файлам для отправки на сервер     */
    private  File[] getAllFoldersFiles (Context context){
        //записать в массив и передать на отправку
        String [] [] folders = new String[0][]; //массив папок и содержащихся в них файлов для отправки на сервер
        File path; //путь к папке программы
        File f; //путь к папке где сохраняются все файлы для отправки на сервер
        File[] files_all; //массив папок в которых храняться файлы для отправки
        File f2; //путь к файлу для отправки на сервер
        File[] files; // массив файлов для отправки

        List< String > list_folders = null;
        List< String > list_files = null;

        path = context.getFilesDir();
        f = new File(String.valueOf(path+"/Omniscient"));
        files_all = f.listFiles();
//        int a = files_all.length;
//
//
//
//        for (File directory : files_all) {
//            if (directory.isDirectory()) {
//                Log.i(Msg, "is directory "+ directory.getName().toString());
//                f2 = new File(directory.toString());
//                files = f2.listFiles();
//                list_folders.add(directory.getName().toString());
//                for (File inFiles_in : files) {
//                    if (inFiles_in.isFile()) {
//                        Log.i(Msg, "is file " + inFiles_in.getName().toString());
//                        list_files.add(inFiles_in.getName().toString());
//                    }
//                }
//            }
//        }
//        folders = new String[list_folders.size()][list_files.size()];
//        for (int i = 0; i<list_folders.size(); i++){
//            folders [i][0] = list_folders.get(i);
//            for (int k = 0; i<list_files.size(); k++){
//                folders [i][k] = list_files.get(k);
//            }
//        }
//        for (int i = 0; i<folders.length; i++){
//            folders [i][0] = list_folders.get(i);
//            for (int k = 0; i<list_files.size(); k++){
//                folders [i][k] = list_files.get(k);
//            }
//        }
        return files_all;
    }
    private void writeFromFile(Context context, String data) {

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }
    private String readFromFile(Context context) {

        String ret = "";

        try {

            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
    private void startTransferFile(Context context){
        DownloadFileRun dwnloadFile = new DownloadFileRun(getAllFoldersFiles(context));
        Transfer ad = new Transfer(context, dwnloadFile);
        ad.forceLoad();
    }



    public class Transfer extends AsyncTaskLoader {

        private DownloadFileRun dwnloadFile;

        public Transfer (Context context, DownloadFileRun dwnloadFile) {
            super(context);
            this.dwnloadFile = dwnloadFile;

        }
        @Override
        public Object loadInBackground() {
            Log.i(Msg, "MyActyvity Start POSTAL 33954");
            Thread startDownload = new Thread(dwnloadFile);
            //поток для загрузки файлов на сервер
            startDownload.start();

            return null;
        }
    }
}
