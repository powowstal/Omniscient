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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class NetworkStateReceiver extends BroadcastReceiver {
//    public NetworkStateReceiver() {
//    }
    private String Msg = "MyMsg";
    @Override
    public void onReceive(Context context, Intent intent) {
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
        DownloadFileRun dwnloadFile = new DownloadFileRun();
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
