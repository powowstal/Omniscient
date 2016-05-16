package com.postal.omniscient.postal.write.json;

import android.os.Environment;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Александр on 17.05.2016.
 */
public class WriteToJsonFile {

   public void writeFileSD(JSONObject contacts) {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d("MyMsg", "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + "Omniscient");//DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, "post.json");//FILENAME_SD);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            bw.write(contacts.toString());//"Содержимое файла на SD");
            // закрываем поток
            bw.close();
            Log.d("MyMsg", "Файл записан на SD: " + sdFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
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