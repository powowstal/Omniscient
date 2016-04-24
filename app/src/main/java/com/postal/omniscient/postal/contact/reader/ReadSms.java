package com.postal.omniscient.postal.contact.reader;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        readSms(uri_send);
        readSms(uri_inbox);
    }
    private void readSms(Uri uri) {
       ContentResolver cr = contentResolver;

       Cursor cur = cr.query(uri, null, null ,null,null);


       // Read the sms data and store it in the list
       if(cur.moveToFirst()) {
           for(int i=0; i < cur.getCount(); i++) {

               Log.i(Msg," Number: " + cur.getString(cur.getColumnIndexOrThrow("address")).toString());
               Log.i(Msg," setBody: " +
                       cur.getString(cur.getColumnIndexOrThrow("body")).toString());//date - дата отправки



               String date =  cur.getString(cur.getColumnIndex("date")).toString();
               String formattedDate = new SimpleDateFormat("MM/dd/yyyy").format(date);



               Log.i(Msg," date: " + date);
                     //  cur.getString(cur.getColumnIndexOrThrow("date")).toString());


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

               }

}
