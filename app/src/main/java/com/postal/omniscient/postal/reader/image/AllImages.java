package com.postal.omniscient.postal.reader.image;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import android.util.Log;

import com.postal.omniscient.postal.SPreferences.PreferencesGetSet;
import com.postal.omniscient.postal.adapter.AdapterData;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Класс достающий адресса (пути) картинок
 * Created by Александр on 04.05.2016.
 */
public class AllImages {

    private final ContentResolver contentResolver;
    private final Context context;
    private static String Msg = "MyMsg";
    private static String data = "_data";
    private static String bucket_display_name = "bucket_display_name";
    private static String date_added = "date_added";
    private static String url = "content://media/external/images/media";
    //выборка по дате с колонки date_added с ? по ? число
    private static String DATE_COLUMN_NAME = date_added+" > ? AND ?";

    public AllImages(Context context, ContentResolver contentResolver){
        this.contentResolver = contentResolver;
        this.context = context;
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
        try{
            Calendar currentDate = Calendar.getInstance();
            PreferencesGetSet Sp = new PreferencesGetSet();
            // текущаяя дата (/1000 потому что получаем полную дату 14 цыфр а в БД картинок 10 цыфр
            Long end_date =  currentDate.getTimeInMillis()/1000;
            Long start_date = (Sp.readeFromPreferences(context)-1)/1000;

            uri = Uri.parse(url);//android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = { data, bucket_display_name, date_added};

            String[] date_query = {start_date.toString(), end_date.toString()};

            cursor = contentResolver.query(uri, projection, DATE_COLUMN_NAME,
                        date_query, date_added+" DESC");

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

            cursor.close();
        } catch (Exception e) {Log.i(Msg, "Error ReadContacts getContacts " + e);}
        return listOfAllImages;
    }
}
