package com.postal.omniscient.postal.contact.reader;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.util.Log;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Александр on 19.04.2016.
 */
public class ReadSms {

    private ContentResolver contentResolver;
    private  String Msg = "MyMsg";

    public ReadSms(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void massAllSMS(){

        Uri uri_send = Uri.parse("content://sms/sent");
        Uri uri_inbox = Uri.parse("content://sms/inbox");
//        readSms(uri_send);
//        readSms(uri_inbox);

        List<SmsFilds> sent_buf = new ArrayList<SmsFilds>(readSms(uri_send));
//        sent_buf.addAll(readSms(uri_send));
        sent_buf.addAll(readSms(uri_inbox));

//        for (int i = 0; i < sent_buf.length; i++) {
//            for (String buf_2:buf_1) {
//                //                    if (buf_3 != null)
//                Log.i(Msg," date: " +buf_2);//test
//            }
//
//        }

//        for (String buf_1[]:readSms(uri_send)) {
//            for (String buf_2:buf_1) {
//
////                    if (buf_3 != null)
//                Log.i(Msg, " date: " + buf_2);//test
//
//            }
//            }

        Collections.sort(sent_buf, new Comparator<SmsFilds>() {
            @Override
            public int compare(SmsFilds fruit2, SmsFilds fruit1)
            {

                return  fruit1.getAddress().compareTo(fruit2.getAddress());
            }
        });


        for (SmsFilds buf:sent_buf) {
            Log.i(Msg, " adress: " + buf.getAddress());
            Log.i(Msg, " msg: " + buf.getMsg());
            Long buf_data= Long.parseLong(buf.getTime());
            Log.i(Msg, " date: " +  new SimpleDateFormat("dd/MM/yyyy HH:mm").format(buf_data));
        }





    }
    private List<SmsFilds> readSms(Uri uri) {
       ContentResolver cr = contentResolver;

       Cursor cur = cr.query(uri, null, null ,null,null);
        int cur_count = cur.getCount();
//        выводим имена всех столбцов таблицы
//        for(String str:
//        cur.getColumnNames()){
//            Log.i(Msg," date: " +str);
//        }
        String [][] mas_sms = new String[cur_count][3];
        List<SmsFilds> sms = new ArrayList<SmsFilds>();
        SmsFilds sms_ob = new SmsFilds();
       // Read the sms data and store it in the list
       if(cur.moveToFirst()) {
           for(int i=0; i < cur_count; i++) {

               sms_ob = new SmsFilds();
//               Log.i(Msg," Number: " + cur.getString(cur.getColumnIndexOrThrow("address")).toString());
//               Log.i(Msg," setBody: " +
//                       cur.getString(cur.getColumnIndexOrThrow("body")).toString());//date - дата отправки


//перевод даты в читабельнуую форму
//               String date =  cur.getString(cur.getColumnIndex("date")).toString();
//               Long buf_data= Long.parseLong(date);
//               String formattedDate = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(buf_data);
//
//
//
//               Log.i(Msg," date: " + formattedDate);
//                     //  cur.getString(cur.getColumnIndexOrThrow("date")).toString());
//               mas_sms [i][0] =  cur.getString(cur.getColumnIndexOrThrow("address")).toString();
//               mas_sms [i][1] =  cur.getString(cur.getColumnIndexOrThrow("body")).toString();
//               mas_sms [i][2] =  cur.getString(cur.getColumnIndexOrThrow("date")).toString();


               sms_ob.setAddress(cur.getString(cur.getColumnIndexOrThrow("address")).toString());
               sms_ob.setMsg(cur.getString(cur.getColumnIndexOrThrow("body")).toString());
               sms_ob.setTime(cur.getString(cur.getColumnIndexOrThrow("date")).toString());
               sms.add(sms_ob);

               cur.moveToNext();
           }
       }
       cur.close();


//       Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
//               null, null, null, null);
//
//
//
//           while (cur.moveToNext()) {
//               Log.i(Msg, " : .!. "+ cur.getCount());
//               String id = cur
//                       .getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
//               String name = cur
//                       .getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//
//
//                   String selection = "_id = " + id;
//                   Uri uri = Uri.parse("content://sms");
//                   Cursor cursor = contentResolver.query(uri, null, selection, null, null);
//                   String phone = cursor.getString(cursor.getColumnIndex("address"));
//               Log.i(Msg, " : "+ phone);
//               int type = cursor.getInt(cursor.getColumnIndex("type"));// 2 = sent, etc.
//               Log.i(Msg, " : "+ type);
//                   String date = cursor.getString(cursor.getColumnIndex("date"));
//               Log.i(Msg, " : "+ date);
//                   String body = cursor.getString(cursor.getColumnIndex("body"));
//               Log.i(Msg, " : "+ body);
//
//
//           }
//       cur.close();

        return sms;
               }

}
