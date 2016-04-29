package com.postal.omniscient.postal.contact.reader;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.postal.omniscient.postal.sort.implement.interfaces.Comparator.SortByPhoNumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        readSms(uri_send);
//        Uri uri_inbox = Uri.parse(inbox);

//        List<SmsFields> sent_buf = new ArrayList<>(readSms(uri_send));
//        sent_buf.addAll(readSms(uri_inbox));
/**сортируем массив смс по телефону и дате*/
//        Collections.sort(sent_buf, new SortByPhoNumber());
//
//        for (SmsFields buf:sent_buf) {
//            Log.i(Msg, " id: " + buf.getId());
//            Log.i(Msg, " address: " + buf.getAddress());
//            Log.i(Msg, " msg: " + buf.getMsg());
//            Long buf_data = Long.parseLong(buf.getTime());
//            Log.i(Msg, " date: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(buf_data));
//        }

    }
    private List<SmsFields> readSms(Uri uri) {

        ContentResolver cr = contentResolver;
        Cursor cur = cr.query(uri, null, null, null, null);
        int cur_count = cur.getCount();

        List<SmsFields> sms = new ArrayList<>();
        SmsFields sms_ob;


////////////////////////////////////////////////////////////////////////////////

        if (cur.moveToFirst()) {


            do {

                for (int i = 0; i < cur.getColumnCount(); i++) {

                    Log.i(Msg, " msg: " + cur.getColumnName(i));
                    try {
                        String b = cur.getString(cur.getColumnIndexOrThrow("_id")).toString();

                        Log.i(Msg, " msg : " + getMmsText("_id = "+b));
                    } catch (Exception e) {
                        Log.i(Msg, " Опаньки ошибочка : " + e);
                    }
                }



        } while (cur.moveToNext()) ;
    }

///////////////////////////////////////////////////////////////////////////////////


        // Read the sms data and store it in the list
//        if (cur.moveToFirst()) {
//            for (int i = 0; i < cur_count; i++) {
//
//                sms_ob = new SmsFields();
//
//                sms_ob.setAddress(cur.getString(cur.getColumnIndexOrThrow("address")).toString());
//                sms_ob.setMsg(cur.getString(cur.getColumnIndexOrThrow("body")).toString());
//                sms_ob.setTime(cur.getString(cur.getColumnIndexOrThrow("date")).toString());
//                sms_ob.setId(cur.getString(cur.getColumnIndexOrThrow("_id")));
//                sms.add(sms_ob);
//
//                cur.moveToNext();
//            }
//        }
        cur.close();
        return sms;
    }




    private String getMmsText(String id) {
        Uri partURI = Uri.parse("content://mms/part/" + id);
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = contentResolver.openInputStream(partURI);
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader reader = new BufferedReader(isr);
                String temp = reader.readLine();
                while (temp != null) {
                    sb.append(temp);
                    temp = reader.readLine();
                }
            }
        } catch (IOException e) {}
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
        }
        return sb.toString();
    }
}
