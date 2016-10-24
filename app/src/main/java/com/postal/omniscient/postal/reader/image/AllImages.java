package com.postal.omniscient.postal.reader.image;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
//import android.provider.MediaStore;
import android.util.Log;

import com.postal.omniscient.postal.adapter.AdapterData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Класс достающий адресса (пути) картинок
 * Created by Александр on 04.05.2016.
 */
public class AllImages {

    private final ContentResolver contentResolver;
    private static String Msg = "MyMsg";
    private static String data = "_data";
    private static String bucket_display_name = "bucket_display_name";
    private static String date_added = "date_added";
    private static String url = "content://media/external/images/media";
    private static String DATE_COLUMN_NAME = date_added+" > ? AND ?";//выборка по дате с колонки date_added с ? по ? число

    public AllImages(ContentResolver contentResolver){
        this.contentResolver = contentResolver;
    }
    /**
     * Возвращяем AdapterData c полями:  Time, Address, index_folder_name (название папки)
     * Адресса (пути) выбраных картинок (новых)
     */
    public List<AdapterData> getLastImages(){
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name, date_create;
        List<AdapterData> listOfAllImages = new ArrayList<AdapterData>();
        AdapterData img_ob;

        Calendar currentDate = Calendar.getInstance();

        Long end_date =  currentDate.getTimeInMillis()/1000;// текущаяя дата (/1000 потому что получаем полную дату 14 цыфр а в БД картинок 10 цыфр
        Long start_date = end_date - (24*60*60);

        uri = Uri.parse(url);//android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

//        String[] projection = { MediaStore.MediaColumns.DATA,
//                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
//                MediaStore.Images.Media.DATE_ADDED};
        String[] projection = { data, bucket_display_name, date_added};

        String[] date_query = {start_date.toString(), end_date.toString()};

            cursor = contentResolver.query(uri, projection, DATE_COLUMN_NAME,
                    date_query, date_added+" DESC");


        Log.i(Msg, "Count Images1 "+cursor.getCount());

        column_index_data = cursor.getColumnIndexOrThrow(data);//MediaStore.MediaColumns.DATA
        column_index_folder_name = cursor /** откуда фото: с камеры,  с папки, скриншот и.т.д */
                .getColumnIndexOrThrow(bucket_display_name);//MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        date_create = cursor.getColumnIndexOrThrow(date_added);//MediaStore.Images.Media.DATE_ADDED
        while (cursor.moveToNext()) {
            img_ob = new AdapterData();
            img_ob.setTime(cursor.getString(date_create));//время добавления
            img_ob.setAddress(cursor.getString(column_index_data));//путь по котором лежит файл patch
            img_ob.set_index_folder_name(cursor.getString(column_index_folder_name));//название папки


            listOfAllImages.add(img_ob);
        }

        for(AdapterData buf : listOfAllImages){
            long buf_long = Long.parseLong(buf.getTime());

            Log.d(Msg, "Image date1 " + new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss")
                    .format(buf_long*1000));


        }
        cursor.close();
        return listOfAllImages;
    }

    /**
     * Возвращяем AdapterData c полями:  Time, Address, index_folder_name (название папки)
     * Все адресса (пути) картинок
     */
    public List<AdapterData> getAllImages(){
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name, date_create;
        List<AdapterData> listOfAllImages = new ArrayList<AdapterData>();
        AdapterData img_ob;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { data, bucket_display_name, date_added};


        cursor = contentResolver.query(uri, projection, null,
                    null, null);


        Log.i(Msg, "Count Images2 "+cursor.getCount());

        column_index_data = cursor.getColumnIndexOrThrow(data);
        column_index_folder_name = cursor /** откуда фото: с камеры,  с папки, скриншот и.т.д */
                .getColumnIndexOrThrow(bucket_display_name);
        date_create = cursor.getColumnIndexOrThrow(date_added);
        while (cursor.moveToNext()) {
            img_ob = new AdapterData();
            img_ob.setTime(cursor.getString(date_create));//время добавления
            img_ob.setAddress(cursor.getString(column_index_data));//путь по котором лежит файл patch
            img_ob.set_index_folder_name(cursor.getString(column_index_folder_name));//название папки


            listOfAllImages.add(img_ob);
        }

        for(AdapterData buf : listOfAllImages){
            long buf_long = Long.parseLong(buf.getTime());
            Log.d(Msg,"Image date2 " + new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss")
                    .format(buf_long*1000));
        }
        cursor.close();
        return listOfAllImages;
    }
}
