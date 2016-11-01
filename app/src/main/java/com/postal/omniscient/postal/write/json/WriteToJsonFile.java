package com.postal.omniscient.postal.write.json;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
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
            writeFileToPhoneMemory(contacts, fileName, folder);
    }
    //записывает json файлы  в память телефона
    private void writeFileToPhoneMemory(JSONObject contacts, String fileName,String folder){
        try {
            // добавляем свой каталог к пути
            File stPath = new File(context.getFilesDir(), "/Omniscient/" + folder);//DIR);
            // создаем каталог
            if (!stPath.exists()) {
                stPath.mkdirs();
            }
            // формируем объект File, который содержит путь к файлу
            String bTime = new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss")
                    .format(new Date());
            // File sdFile = new File(sdPath, time + fileName);//FILENAME_SD);
            File stFile = new File(stPath, bTime + fileName);//FILENAME_SD);
            try {
                // открываем поток для записи
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(//в utf-8 кодируем
                        new FileOutputStream(stFile), "UTF-8"));// new BufferedWriter(new FileWriter(stFile));
                // пишем данные
                bw.write(contacts.toString());//"Содержимое файла в памяти");
                // закрываем поток
                bw.close();
            } catch (IOException e) {
                Log.e("MyMsg", "Файл записан на telephon: " + e.toString());
            }
        }catch (Exception e){Log.i("MyMsg", "Error WriteToJsonFile writeFileToPhoneMemory "+e);}
    }
}