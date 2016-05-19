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
       writeFileToPhoneMemory(contacts, fileName, folder);
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

//        Long time = System.currentTimeMillis();
        // получаем путь к SD
//        File sdPath = getDire;
//        // добавляем свой каталог к пути
//        sdPath = new File(sdPath.getAbsolutePath() + "/" + "Omniscient" +"/" +folder);//DIR_SD);
//        // создаем каталог
//        sdPath.mkdirs();
//        // формируем объект File, который содержит путь к файлу
//        String bTime = new SimpleDateFormat("dd_MM_yyyy_HH-mm")
//                .format(time);
//        // File sdFile = new File(sdPath, time + fileName);//FILENAME_SD);
//        File sdFile = new File(sdPath, bTime + fileName);//FILENAME_SD);
//        File sdPath = context.get;
//        try{
//
//        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
//                context.openFileOutput("Omniscient" +"/" +folder, context.MODE_WORLD_READABLE)));
//        // пишем данные
//        bw.write("Содержимое файла");
//        // закрываем поток
//        bw.close();
//        Log.d("MyMsg", "Файл записан");
//    } catch (FileNotFoundException e) {
//        e.printStackTrace();
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
        Long time = System.currentTimeMillis();
        // получаем путь к SD
        File sdPath = Environment.getDataDirectory();
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
            Log.d("MyMsg", "Файл записан на telephon: " + sdFile.getAbsolutePath());
            Log.v("MyMsg","date) " + new SimpleDateFormat("dd/MM/yyyy HH:mm")
                    .format(time));
        } catch (IOException e) {
            Log.e("MyMsg", "Файл записан на telephon: " + e.toString());
        }
    }

    private void writeTofile(JSONObject contacts) {

        File path = new File(Environment.getExternalStorageDirectory(), "/Postal/jsonfile");
        String content = contacts.toString();
        if (!path.exists()) {
            path.mkdirs();
        }
        try {
//            FileOutputStream fOut = context.openFileOutput(path + "/samplefile.txt",
//                    context.MODE_WORLD_READABLE);
//            OutputStreamWriter osw = new OutputStreamWriter(fOut);
//
//            // Write the string to the file
//            osw.write(content);
                        /* ensure that everything is
                         * really written out and close */
//            osw.flush();
//            osw.close();

        } catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }


//        try {
//            FileWriter fw = new FileWriter("path");
//            BufferedWriter bw = new BufferedWriter(fw);
//
//            bw.write(content);
//            bw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
//            outputStreamWriter.write(data);
//            outputStreamWriter.close();
//        }
//        catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }


//        String out = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(new Date());
//        File sampleDir = new File(Environment.getExternalStorageDirectory(), "/Trd1");
//        if (!sampleDir.exists()) {
//            sampleDir.mkdirs();
//        }
//        String file_name = "Record";
//        try {
//            File audiofile = File.createTempFile(file_name, "postal.amr", sampleDir);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
//        recorder.setOutputFile(audiofile.getAbsolutePath());
    }
}