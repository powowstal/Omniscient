package com.postal.omniscient.postal.browser.history;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.postal.omniscient.postal.adapter.AdapterData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Класс получающий историю браузера за последнее 3 часа
 * Created by Александр on 05.05.2016.
 */
public class BrowserHistory {
    private final ContentResolver contentResolver;
    private static String Msg = "MyMsg";
    private static String id ="_id";
    private static String url_data = "url";
    private static String date = "date";
    private static String DATE_COLUMN_NAME = date+" > ? AND ?";//выборка по дате с колонки date с ? по ? число

    public BrowserHistory(ContentResolver contentResolver) {
       this.contentResolver = contentResolver;
    }
    /**
     * Получаем сторию браузера за последнее 3 часа
     * AdapterData c полями: Id, Address (url), Time (date)
     */
    public List<AdapterData> getBrowserHist()  {
        Uri uri;
        AdapterData breowse_ob;
        List<AdapterData> listOfAllImages = new ArrayList<>();
        Calendar currentDate = Calendar.getInstance();
        Long end_date =  currentDate.getTimeInMillis();
        Long start_date = end_date - (3*60*60*1000);
        String[] date_query = {start_date.toString(), end_date.toString()};


        uri = Uri.parse("content://browser/bookmarks");
        String[] projection = { id, url_data, date};


    Cursor cur = contentResolver.query(uri,
            projection, DATE_COLUMN_NAME, date_query, date+" DESC");// DESC - сортировка по убываню

        int id_column_index, url_data_column_index, date_column_index;
        id_column_index = cur.getColumnIndexOrThrow(id);
        url_data_column_index = cur.getColumnIndexOrThrow(url_data);
        date_column_index = cur.getColumnIndexOrThrow(date);

        long buf_date;
            while (cur.moveToNext()) {
                breowse_ob = new AdapterData();
                breowse_ob.setId(cur.getString(id_column_index));//id
                breowse_ob.setAddress(cur.getString(url_data_column_index));//url на которые заходил последних 3 часа
                buf_date = Long.parseLong(cur.getString(date_column_index));
                breowse_ob.setTime(//date когда заходили на страницу в интернент
                        new SimpleDateFormat("dd/MM/yyyy HH:mm")
                                .format(buf_date));

                        listOfAllImages.add(breowse_ob);

                Log.v("MyMsg", "id " + cur.getString(id_column_index));
                Log.v("MyMsg", "url " + cur.getString(url_data_column_index));
                buf_date = Long.parseLong(cur.getString(date_column_index));
                Log.v("MyMsg","date " + new SimpleDateFormat("dd/MM/yyyy HH:mm")
                        .format(buf_date));
            }
        cur.close();
        return listOfAllImages;
    }
}
