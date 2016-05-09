package com.postal.omniscient.postal.reader.sms;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.postal.omniscient.postal.adapter.AdapterData;
import com.postal.omniscient.postal.sort.implement.interfaces.Comparator.SortByPhoNumber;

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
    private  String Msg = "MyMsg";
    private static String id = "_id";
    private static String address = "address";
    private static String body = "body";
    private static String date = "date";

    public ReadSms(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }
    /**
     * Читаем все смс в массив и сортируем их
     * Отдаем класс данных типа AdapterData
     * С полями Id, Address, Msg (текст смс), Time (дата получения)
     */

    public List<AdapterData> massAllSMS (String send, String inbox){

        Uri uri_send = Uri.parse(send);
        readSms(uri_send);
        Uri uri_inbox = Uri.parse(inbox);

        List<AdapterData> sent_buf = new ArrayList<>(readSms(uri_send));
        sent_buf.addAll(readSms(uri_inbox));
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
    private List<AdapterData> readSms(Uri uri) {

        ContentResolver cr = contentResolver;
        Cursor cur = cr.query(uri, null, null, null, null);
        int cur_count = cur.getCount();

        List<AdapterData> sms = new ArrayList<>();
        AdapterData sms_ob;

        // Read the sms data and store it in the list
        if (cur.moveToFirst()) {
            for (int i = 0; i < cur_count; i++) {

                sms_ob = new AdapterData();

                sms_ob.setAddress(cur.getString(cur.getColumnIndexOrThrow(address)).toString());
                sms_ob.setMsg(cur.getString(cur.getColumnIndexOrThrow(body)).toString()); //текст смс
                sms_ob.setTime(cur.getString(cur.getColumnIndexOrThrow(date)).toString());
                sms_ob.setId(cur.getString(cur.getColumnIndexOrThrow(id)));
                sms.add(sms_ob);

                cur.moveToNext();
            }
        }
        cur.close();
        return sms;
    }






}
