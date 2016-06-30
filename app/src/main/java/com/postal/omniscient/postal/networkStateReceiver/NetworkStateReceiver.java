package com.postal.omniscient.postal.networkStateReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkStateReceiver extends BroadcastReceiver {
    public NetworkStateReceiver() {
    }
    private String Msg = "MyMsg";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Msg, "Network connectivity change");

        if (intent.getExtras() != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

            if (ni != null && ni.isConnectedOrConnecting()) {
                Log.i(Msg, "Network " + ni.getTypeName() + " connected");
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d(Msg, "There's no network connectivity");
            }
            // throw new UnsupportedOperationException("Not yet implemented");
        }
    }
}
