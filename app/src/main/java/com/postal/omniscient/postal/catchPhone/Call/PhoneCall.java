package com.postal.omniscient.postal.catchPhone.Call;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.postal.omniscient.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by Александр on 02.05.2016.
 */
public class PhoneCall implements Runnable {
    private Context context;
    public PhoneCall(Context context){
        this.context = context;
    }

    @Override
    public void run() {
//        Intent intent = new Intent(getApplicationContext(), TService.class);
//        startService(intent);
        Intent intent = new Intent(context, TService.class);
        context.startService(intent);
    }
}
