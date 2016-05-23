package com.postal.omniscient.postal.write.json;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Александр on 17.05.2016.
 */
public class WriteToJsonFile {
    private final Context context;
    public WriteToJsonFile(Context context) {
        this.context = context;
    }
   public void writeFileSD(JSONObject contacts, String fileName,String folder) {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d("MyMsg", "SD-карта не доступна: " + Environment.getExternalStorageState());
            writeFileToPhoneMemory(contacts, fileName, folder);
            return;
        }
       Long time = System.currentTimeMillis();
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + "Omniscient" +"/" +folder);//DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
       String bTime = new SimpleDateFormat("dd_MM_yyyy_HH-mm")
               .format(time);
       // File sdFile = new File(sdPath, time + fileName);//FILENAME_SD);
        File sdFile = new File(sdPath, bTime + fileName);//FILENAME_SD);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            bw.write(contacts.toString());//"Содержимое файла на SD");
            // закрываем поток
            bw.close();
            Log.d("MyMsg", "Файл записан на SD: " + sdFile.getAbsolutePath());
            Log.v("MyMsg","date) " + new SimpleDateFormat("dd/MM/yyyy HH:mm")
                    .format(time));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void writeFileToPhoneMemory(JSONObject contacts, String fileName,String folder){


        // добавляем свой каталог к пути
        File stPath = new File(context.getFilesDir(), "Omniscient/" +folder);//DIR_SD);
        // создаем каталог
        stPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        String bTime = new SimpleDateFormat("dd_MM_yyyy_HH-mm")
                .format(new Date());
        // File sdFile = new File(sdPath, time + fileName);//FILENAME_SD);
        File stFile = new File(stPath, bTime + fileName);//FILENAME_SD);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(stFile));
            // пишем данные
            bw.write(contacts.toString());//"Содержимое файла в памяти");
            // закрываем поток
            bw.close();
            Log.d("MyMsg", "Файл записан на telephon: " + stFile.getAbsolutePath());
            Log.v("MyMsg","date) " + new SimpleDateFormat("dd/MM/yyyy HH:mm")
                    .format(new Date()));
        } catch (IOException e) {
            Log.e("MyMsg", "Файл записан на telephon: " + e.toString());
        }
    }

}