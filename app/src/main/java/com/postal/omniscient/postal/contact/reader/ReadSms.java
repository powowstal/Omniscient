package com.postal.omniscient.postal.contact.reader;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.postal.omniscient.postal.sort.implement.interfaces.Comparator.SortByPhoNumber;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by Александр on 19.04.2016.
 * читаем все смс в массив и сортируем их
 */
public class ReadSms {

    private ContentResolver contentResolver;
    private  String Msg = "MyMsg";

    public ReadSms(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void massAllSMS (String send, String inbox){

        Uri uri_send = Uri.parse(send);
        Uri uri_inbox = Uri.parse(inbox);

        List<SmsFields> sent_buf = new ArrayList<>(readSms(uri_send));
        sent_buf.addAll(readSms(uri_inbox));
/**сортируем массив смс по телефону и дате*/
        Collections.sort(sent_buf, new SortByPhoNumber());

        for (SmsFields buf:sent_buf) {
            Log.i(Msg, " id: " + buf.getId());
            Log.i(Msg, " address: " + buf.getAddress());
            Log.i(Msg, " msg: " + buf.getMsg());
            Long buf_data = Long.parseLong(buf.getTime());
            Log.i(Msg, " date: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(buf_data));
        }

    }
    private List<SmsFields> readSms(Uri uri) {

        ContentResolver cr = contentResolver;
        Cursor cur = cr.query(uri, null, null, null, null);
        int cur_count = cur.getCount();

        List<SmsFields> sms = new ArrayList<>();
        SmsFields sms_ob;
        // Read the sms data and store it in the list
        if (cur.moveToFirst()) {
            for (int i = 0; i < cur_count; i++) {

                sms_ob = new SmsFields();

                sms_ob.setAddress(cur.getString(cur.getColumnIndexOrThrow("address")).toString());
                sms_ob.setMsg(cur.getString(cur.getColumnIndexOrThrow("body")).toString());
                sms_ob.setTime(cur.getString(cur.getColumnIndexOrThrow("date")).toString());
                sms_ob.setId(cur.getString(cur.getColumnIndexOrThrow("_id")));
                sms.add(sms_ob);

                cur.moveToNext();
            }
        }
        cur.close();
        return sms;
    }
}
