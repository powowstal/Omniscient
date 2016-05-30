package com.postal.omniscient.postal.reader.sms;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.postal.omniscient.postal.adapter.AdapterData;
import com.postal.omniscient.postal.sort.implement.interfaces.Comparator.SortByPhoNumber;
import com.postal.omniscient.postal.write.json.WriteToJsonFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Читаем все смс в массив и сортируем их
 * Created by Александр on 19.04.2016.
 */
public class ReadSms {

    private ContentResolver contentResolver;
    private Context context;
    private  String Msg = "MyMsg";
    private static String id = "_id";
    private static String address = "address";
    private static String body = "body";
    private static String date = "date";
    private String  fileName ="sms.json";
    private String folder = "SMS";

    public ReadSms(Context context, ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
        this.context = context;
    }
    /**
     * Читаем все смс в массив и сортируем их
     * Отдаем класс данных типа AdapterData
     * С полями Id, Address, Msg (текст смс), Time (дата получения)
     */

    private List<AdapterData> massAllSMS (String send, String inbox){

        Uri uri_send = Uri.parse(send);
        //readSms(uri_send);
        Uri uri_inbox = Uri.parse(inbox);

        List<AdapterData> sent_buf = new ArrayList<AdapterData>(readSmsSend(uri_send));
        sent_buf.addAll(readSmsInbox(uri_inbox));
/**сортируем массив смс по телефону и дате*/
        Collections.sort(sent_buf, new SortByPhoNumber());

        for (AdapterData buf:sent_buf) {
            Log.i(Msg, " id: " + buf.getId());
            Log.i(Msg, " address: " + buf.getAddress());
            Log.i(Msg, " msg: " + buf.getMsg());
            Long buf_data = Long.parseLong(buf.getTime());
            Log.i(Msg, " date: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(buf_data));
        }
        return sent_buf;
    }
    private List<AdapterData> readSmsInbox(Uri uri) {

        ContentResolver cr = contentResolver;
        Cursor cur = cr.query(uri, null, null, null, null);
        int cur_count = cur.getCount();

        List<AdapterData> sms = new ArrayList<AdapterData>();
        AdapterData sms_ob;

        // Read the sms data and store it in the list
        if (cur.moveToFirst()) {
            for (int i = 0; i < cur_count; i++) {

                sms_ob = new AdapterData();

                sms_ob.setAddress(cur.getString(cur.getColumnIndexOrThrow(address)).toString());
                sms_ob.setMsg("Полученное : " + cur.getString(cur.getColumnIndexOrThrow(body)).toString()); //текст смс
                sms_ob.setTime(cur.getString(cur.getColumnIndexOrThrow(date)).toString());
                sms_ob.setId(cur.getString(cur.getColumnIndexOrThrow(id)));
                sms.add(sms_ob);

                cur.moveToNext();
            }
        }
        cur.close();
        return sms;
    }
    private List<AdapterData> readSmsSend(Uri uri) {

        ContentResolver cr = contentResolver;
        Cursor cur = cr.query(uri, null, null, null, null);
        int cur_count = cur.getCount();

        List<AdapterData> sms = new ArrayList<AdapterData>();
        AdapterData sms_ob;

        // Read the sms data and store it in the list
        if (cur.moveToFirst()) {
            for (int i = 0; i < cur_count; i++) {

                sms_ob = new AdapterData();

                sms_ob.setAddress(cur.getString(cur.getColumnIndexOrThrow(address)).toString());
                sms_ob.setMsg("Отправленное : " + cur.getString(cur.getColumnIndexOrThrow(body)).toString()); //текст смс
                sms_ob.setTime(cur.getString(cur.getColumnIndexOrThrow(date)).toString());
                sms_ob.setId(cur.getString(cur.getColumnIndexOrThrow(id)));
                sms.add(sms_ob);

                cur.moveToNext();
            }
        }
        cur.close();
        return sms;
    }
    public void smsToJson (){
        WriteToJsonFile writeToFile = new WriteToJsonFile(context);
        String uri_send_sms = "content://sms/sent";
        String uri_inbox_sms = "content://sms/inbox";
        JSONObject sms = new JSONObject();//Заголовок
        JSONObject body;
        JSONArray mass = new JSONArray();
        Long time_bufer;
        for(AdapterData date : massAllSMS(uri_send_sms, uri_inbox_sms)){
            body = new JSONObject();
            try {
                body.put("Phone", date.getAddress());
                body.put("Message", date.getMsg());
                time_bufer = Long.parseLong( date.getTime());//В читабельный формат - дату
                body.put("Time", new SimpleDateFormat("dd-MM-yyyy HH:mm")
                        .format(time_bufer));
                mass.put(body);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            sms.put("SMS", mass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        writeToFile.writeFileSD(sms, fileName, folder);
    }
}
