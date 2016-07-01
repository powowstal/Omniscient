package com.postal.omniscient.postal.networkStateReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import com.postal.omniscient.postal.downloadFiles.DownloadFileRun;
import com.postal.omniscient.postal.service.StartService;

public class NetworkStateReceiver extends BroadcastReceiver {
//    public NetworkStateReceiver() {
//    }
    private String Msg = "MyMsg";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Msg, "Network connectivity change");

        if (intent.getExtras() != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

            if (ni != null && ni.isConnectedOrConnecting()) {
                Log.i(Msg, "Network " + ni.getTypeName() + " connected");
                startTransferFile(context);// Начать загрузку файлов на сервер при появлении интернета
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d(Msg, "There's no network connectivity");
            }
            // throw new UnsupportedOperationException("Not yet implemented");
        }
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
            Log.i("MyMsg", "MyActyvity Start");
            Thread startDownload = new Thread(dwnloadFile);
            //поток для загрузки файлов на сервер
            startDownload.start();

            return null;
        }
    }
}
