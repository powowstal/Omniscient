package com.postal.omniscient.postal.catchPhone.Call;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Александр on 02.05.2016.
 */
public class DeviceAdminDemo extends DeviceAdminReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("TAG", "service onReceive");
    }

    public void onEnabled(Context context, Intent intent) {
        Log.d("TAG", "service onEnabled");
    };

    public void onDisabled(Context context, Intent intent) {
        Log.d("TAG", "service onDisabled");
    };
}