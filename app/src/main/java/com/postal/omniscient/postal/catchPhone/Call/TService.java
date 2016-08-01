package com.postal.omniscient.postal.catchPhone.Call;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.content.AsyncTaskLoader;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.postal.omniscient.postal.adapter.EventBusData;
import com.postal.omniscient.postal.service.AlarmReceiver;
import com.postal.omniscient.postal.service.MyService;
import com.postal.omniscient.postal.service.StartService;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.logging.Handler;

/**
 * Created by Александр on 02.05.2016.
 */
public class TService extends Service {
    MediaRecorder recorder;
    File audiofile;
    String name, phonenumber;
    String audio_format;
    public String Audio_Type;
    int audioSource;
    Context context;
    private Handler handler;
    Timer timer;
    Boolean offHook = false, ringing = false;
    Toast toast;
    Boolean isOffHook = false;
    private boolean recordstarted = false;

    private static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    private static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";
    private CallBr br_call;




    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("MyMsg", "destroy");

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // final String terminate =(String)
        // intent.getExtras().get("terminate");//
        // intent.getStringExtra("terminate");
         Log.d("MyMsg", " TService start");
        //
        // TelephonyManager telephony = (TelephonyManager)
        // getSystemService(Context.TELEPHONY_SERVICE); // TelephonyManager
        // // object
        // CustomPhoneStateListener customPhoneListener = new
        // CustomPhoneStateListener();
        // telephony.listen(customPhoneListener,
        // PhoneStateListener.LISTEN_CALL_STATE);
        // context = getApplicationContext();

        final IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_OUT);
        filter.addAction(ACTION_IN);
        this.br_call = new CallBr();
        this.registerReceiver(this.br_call, filter);



        // if(terminate != null) {
        // stopSelf();
        // }
        return START_STICKY;//_REDELIVER_INTENT;
    }

    public class CallBr extends BroadcastReceiver {
        Bundle bundle;
        String state;
        String inCall, outCall;
        public boolean wasRinging = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_IN)) {
                if ((bundle = intent.getExtras()) != null) {
                    state = bundle.getString(TelephonyManager.EXTRA_STATE);
                    if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        inCall = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                        wasRinging = true;
//                        Toast.makeText(context, "IN : " + inCall, Toast.LENGTH_LONG).show();
                    } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                        if (wasRinging == true) {
                            EventBus.getDefault().post(new EventBusData("call_in"));//флаг что идет запись разгавора и не начинать занрузку файла (ато удалится)

//                            Toast.makeText(context, "ANSWERED", Toast.LENGTH_LONG).show();

                            String time = new SimpleDateFormat("dd_MM_yyyy_HH-mm")
                                    .format(new Date());
                            File sampleDir = new File(context.getFilesDir(), "/Omniscient/In_call");

                            //Если нет СД карты сохраняем на телефоне
//                            if (Environment.getExternalStorageState().equals(
//                                    Environment.MEDIA_MOUNTED)) {
//                                Log.d("MyMsg", "SD-карта доступна: " + Environment.getExternalStorageState());
//                                sampleDir = new File(Environment.getExternalStorageDirectory(), "/Omniscient/In_call");
//                            }
                            if (!sampleDir.exists()) {
                                sampleDir.mkdirs();
                            }
                            String file_name = inCall+"_"+time;
                            try {
                                audiofile = File.createTempFile(file_name, "_in_call.amr", sampleDir);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String path = Environment.getExternalStorageDirectory().getAbsolutePath();

                            recorder = new MediaRecorder();
//                          recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);

                            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                            recorder.setOutputFile(audiofile.getAbsolutePath());
                            try {
                                recorder.prepare();
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            recorder.start();
                            recordstarted = true;
                        }
                    } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                        wasRinging = false;
//                        Toast.makeText(context, "REJECT || DISCO", Toast.LENGTH_LONG).show();
                        Log.i("MyMsg", "1call off");
                        if (recordstarted) {
                            recorder.stop();
                            recordstarted = false;
                        }
                        EventBus.getDefault().post(new EventBusData("call_off"));
                    }
                }
            } else if (intent.getAction().equals(ACTION_OUT)) {
                if ((bundle = intent.getExtras()) != null) {
                    outCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                    //проверка на набор сервисов (*111#) а то срабатывает событие вызова
                    if (!outCall.contains("*") || !outCall.contains("#")){
                        outCall = outCall.replace("+", "");

                        EventBus.getDefault().post(new EventBusData("call_out"));//флаг что идет запись разгавора и не начинать занрузку файла (ато удалится)
                        Log.i("MyMsg", "Name on ACTION_OUT "+outCall);
//                    Toast.makeText(context, "OUT : " + outCall, Toast.LENGTH_LONG).show();
                    ////////////////////////////////////////////////////////////
                            //сохраняем на телефон
                            String time = new SimpleDateFormat("dd_MM_yyyy_HH-mm")
                                    .format(new Date());
                            File sampleDir = new File(context.getFilesDir(), "/Omniscient/Out_call");


                            //На СД карту сохраняем
//                            if (Environment.getExternalStorageState().equals(
//                                    Environment.MEDIA_MOUNTED)) {
//                                Log.d("MyMsg", "SD-карта доступна: " + Environment.getExternalStorageState());
//                                sampleDir = new File(Environment.getExternalStorageDirectory(), "/Omniscient/Out_call");
//                            }

                            if (!sampleDir.exists()) {
                                sampleDir.mkdirs();
                            }

                            String file_name = outCall+"_"+time;
                            try {
                                audiofile = File.createTempFile(file_name, "_out_call.amr", sampleDir);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //String path = Environment.getExternalStorageDirectory().getAbsolutePath();

                            recorder = new MediaRecorder();
//                          recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);

                            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                            recorder.setOutputFile(audiofile.getAbsolutePath());
                            try {
                                recorder.prepare();
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            recorder.start();
                            recordstarted = true;
                        }
                } } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

//                        Toast.makeText(context, "REJECT || DISCO", Toast.LENGTH_LONG).show();
                        Log.i("MyMsg", "2call off");
                        if (recordstarted) {
                            recorder.stop();
                            recordstarted = false;
                        }
                EventBus.getDefault().post(new EventBusData("call_off"));//разгоаор записан говорим "загрузи файл"
                    }

           // AsyncR ad = new AsyncR(context);

           // ad.forceLoad();
                    ///////////////////////////////////////////////////////////

        }
        public class AsyncR extends AsyncTaskLoader {


            public AsyncR(Context context) {
                super(context);

            }
            Toast toast;
            public AsyncR(Context applicationContext, Toast toast) {
                super(applicationContext);
                this.toast = toast;
            }

            @Override
            public Object loadInBackground() {

                Intent par = new Intent(getContext(), StartService.class);
                getContext().startService(par);
                //getContext().sendBroadcast(new Intent("YouWillNeverKillMe"));
                return null;
            }
        }
    }
}