package com.postal.omniscient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.postal.omniscient.postal.contact.reader.ReadContacts;

public class MainActivity extends AppCompatActivity {
    private static String Msg = "MyMsg1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getContacts();//in test

    }
    private void getContacts(){
        ReadContacts getPhones = new ReadContacts();
        getPhones.setContentResolver(getContentResolver());
        String [][] phone_contacts = getPhones.readContacts();



            for (int i=0; getPhones.getX()>=i; i++) {
                if(phone_contacts[i][0]!=null) {
                    Log.i(Msg, " ОТВЕТ размер1 - " + phone_contacts[i][0]);

                }

                for (int k=1; getPhones.getY()>k; k++) {
                    if (phone_contacts[i][k] != null) {
                        Log.i(Msg, " ОТВЕТ размер2  - " + phone_contacts[i][k]);
                    }
                }

                }
            }

}
