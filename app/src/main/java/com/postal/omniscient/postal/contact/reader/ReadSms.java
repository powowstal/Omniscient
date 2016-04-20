package com.postal.omniscient.postal.contact.reader;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by Александр on 19.04.2016.
 */
public class ReadSms {

    private ContentResolver contentResolver;
    private  String Msg = "MyMsg";

    public ReadSms(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

   public void readSms() {
       ContentResolver cr = contentResolver;
       Uri uri = Uri.parse("content://sms/inbox");
       Cursor c = cr.query(uri, null, null ,null,null);


       // Read the sms data and store it in the list
       if(c.moveToFirst()) {
           for(int i=0; i < c.getCount(); i++) {

               Log.i(Msg," setBody(" + c.getString(c.getColumnIndexOrThrow("body")).toString());
               Log.i(Msg," Number(" + c.getString(c.getColumnIndexOrThrow("address")).toString());


               c.moveToNext();
           }
       }
       c.close();


//       Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
//               null, null, null, null);
//some cheng
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
