package com.postal.omniscient.postal.reader.mms;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Adapter;

import com.postal.omniscient.postal.adapter.AdapterData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Александр on 29.04.2016.
 * читаем ммс
 */
public class ReadMms {
    private ContentResolver contentResolver;
    private  String Msg = "MyMsg";
    private static String id = "_id";
    private static String address = "address";
    private static String body = "body";
    private static String date = "date";
    private static String content_part = "content://mms/part";
    private static String content_mms = "content://mms";

    public ReadMms (ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }
//
//    public void massAllMms() {
//
//
////       try {
////           final String[] projection = new String[]{"ct_t"};
////           Uri uri = Uri.parse("content://mms/conversations?simple=false");
////           Cursor cur = contentResolver.query(uri, null, null, null, null);
////           if (cur.moveToFirst()) {
////               do {
////                   for (int i = 0; i < cur.getColumnCount(); i++) {
////                                                   Log.i(Msg, " msg: " + cur.getColumnName(i));
////                            try {
////                                Log.i(Msg, " Содержание0: " +cur.getString(cur.getColumnIndexOrThrow(cur.getColumnName(i))));
////
////                            } catch (Exception e) {
////                                Log.i(Msg, e.toString());
////                            }
////                        }
//
////                   String string = cur.getString(cur.getColumnIndex("ct_t"));
////                   if ("application/vnd.wap.multipart.related".equals(string)) {
////                       Log.i(Msg,  " it's MMS");
////                   } else {
////                       Log.i(Msg,  " it's SMS");
////                   }
//
//
//
////                   long buf_data = Long.parseLong( cur.getString(cur.getColumnIndex("date")).toString())*1000;
////                        Log.i(Msg, " Содержание0: " +new SimpleDateFormat("dd/MM/yyyy HH:mm").format(buf_data));
////                        buf_data = Long.parseLong( cur.getString(cur.getColumnIndex("date_sent")).toString())*1000;
////                        Log.i(Msg, " Содержание_send: " +new SimpleDateFormat("dd/MM/yyyy HH:mm").format(buf_data));
////                        Log.i(Msg, " Addres: " +cur.getString(cur.getColumnIndexOrThrow("address")).toString());
//
//
//
//
//
//                   //
////                        String partId = cur.getString(cur.getColumnIndex("_id"));
////
////                        Log.i(Msg, "Джеки Чан: "+ getANumber(Integer.parseInt(partId)));
////                    String type = cur.getString(cur.getColumnIndex("ct"));
////                    Log.i(Msg, "_id: " + partId + " ct : " + type);
////
////
////                    if ("text/plain".equals(type)) {
////                        String body;
////                        String data = cur.getString(cur.getColumnIndexOrThrow("_data"));
////                        Log.i(Msg, "data: " + data);
////
////
////
////
////
////                        if (data != null) {
////                            // implementation of this method below
////
////                            body = getMmsText(partId);
////                            Log.i(Msg, "body1: " + body);
////
////
////                        } else {
////                            body = cur.getString(cur.getColumnIndex("text"));
////                            Log.i(Msg, "body21: " + body);
////                        }
////                    }
////               }
////    while (cur.moveToNext());
////           }
////           cur.close();
////
////       }catch (Exception e){
////           Log.i(Msg, " msg: " + e);}
//
////        ContentResolver cr = contentResolver;
////        Uri partURI = Uri.parse(content_mms);
////        String [] projection = {id,date};
////        AdapterData mms;
////        List<AdapterData> listOfAllMms = new ArrayList<>();
////        int id_column_index, date_column_index;
////        id_column_index = cur.getColumnIndexOrThrow(id);
////        date_column_index = cur.getColumnIndexOrThrow(id);
////
////        breowse_ob.setId(cur.getString(id_column_index));
////        breowse_ob.setId(cur.getString(id_column_index));
////        breowse_ob.setId(cur.getString(id_column_index));
////
////        Cursor cur = cr.query(partURI, projection, null, null, date+" DESC");
////        while (cur.moveToNext()){
////            mms = new AdapterData();
////
////            mms.setId(cur.getString(id));
////            mms.setTime();
////            listOfAllMms.add(mms);
////
////        }
////        cur.close();
////    }
//
//
////    private String getMmsText(String id) {
////        Uri partURI = Uri.parse("content://mms/part/" + id);
////        InputStream is = null;
////        StringBuilder sb = new StringBuilder();
////        try {
////            is = contentResolver.openInputStream(partURI);
////            if (is != null) {
////                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
////                BufferedReader reader = new BufferedReader(isr);
////                String temp = reader.readLine();
////                while (temp != null) {
////                    sb.append(temp);
////                    temp = reader.readLine();
////                }
////            }
////        } catch (IOException e) {}
////        finally {
////            if (is != null) {
////                try {
////                    is.close();
////                } catch (IOException e) {}
////            }
////        }
////        return sb.toString();
////    }
//    /**
//     * Get Sender number
//     *
//     * @param id
//     * @return
//     */
//    private String getANumber(int id) {
//        String add = "";
//        final String[] projection = new String[] {"address","contact_id","charset","type"};
//        Uri.Builder builder = Uri.parse("content://mms").buildUpon();
//        builder.appendPath(String.valueOf(id)).appendPath("addr");
//        Cursor cursor = contentResolver.query(
//                builder.build(),
//                projection,
//                null,
//                null, null);
//        if (cursor.moveToFirst()) {
//            add = cursor.getString(cursor.getColumnIndex("address"));
//        }
//        return add;
//    }
//
//
}
