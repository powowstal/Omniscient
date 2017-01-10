package com.postal.omniscient.postal.SPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Александр on 03.01.2017.
 */
public class PreferencesGetSet {
    private String APP_PREFERENCES_KEY = "time";
    private String APP_PREFERENCES_FILE_NAME = "APP_PREFERENCES_TIME";
    private SharedPreferences mSettings = null;

    public void setAPP_PREFERENCES_KEY(String APP_PREFERENCES_KEY) {
        this.APP_PREFERENCES_KEY = APP_PREFERENCES_KEY;
    }

    public void setAPP_PREFERENCES_FILE_NAME(String APP_PREFERENCES_FILE_NAME) {
        this.APP_PREFERENCES_FILE_NAME = APP_PREFERENCES_FILE_NAME;
    }


    //запись даты последней проверки данных на наличие новых файлов, для отправки на сервер, в файл
    public void writeToPreferences(Context context) {
        try {
            // Запоминаем данные
            mSettings = context.getSharedPreferences(APP_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putLong(APP_PREFERENCES_KEY, System.currentTimeMillis());
            editor.apply();
        } catch (Exception e) {
            Log.i("MyMsg","Error PreferencesGetSet writeToFile"+e);}
    }
    //получить дату последней проверки данных на наличие новых файлов, для отправки на сервер, в файл
    public Long readeFromPreferences(Context context) {
        long k = 0;
        Long mCounter = k;
        try{
        mSettings = context.getSharedPreferences(APP_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);//context
        mCounter = mSettings.getLong(APP_PREFERENCES_KEY, 0);
        } catch (Exception e) {
            Log.i("MyMsg","Error PreferencesGetSet readeFromPreferences"+e);}
        return mCounter;
    }
    //записать в настройки Boolean
    public void boolToPreferences(Context context, Boolean data) {
        try {
            mSettings = context.getSharedPreferences(APP_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean(APP_PREFERENCES_KEY, data);
            editor.apply();
        } catch (Exception e) {
            Log.i("MyMsg","Error PreferencesGetSet boolToPreferences "+e);}
    }
    //получить настройки Boolean
    public Boolean readeBoolFromPreferences(Context context) {
        Boolean mCounter = true;
        try{
            mSettings = context.getSharedPreferences(APP_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);//context
            mCounter = mSettings.getBoolean(APP_PREFERENCES_KEY, true);
        } catch (Exception e) {
            Log.i("MyMsg","Error PreferencesGetSet readeBoolFromPreferences "+e);}
        return mCounter;
    }
    //записать в настройки String
    public void stringToPreferences(Context context, String data) {
        try {
            mSettings = context.getSharedPreferences(APP_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(APP_PREFERENCES_KEY, data);
            editor.apply();
        } catch (Exception e) {
            Log.i("MyMsg","Error PreferencesGetSet stringToPreferences "+e);}
    }
    //получить настройки String
    public String readeStringFromPreferences(Context context) {
        String mCounter = "";
        try{
            mSettings = context.getSharedPreferences(APP_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);//context
            mCounter = mSettings.getString(APP_PREFERENCES_KEY, "");
        } catch (Exception e) {
            Log.i("MyMsg","Error PreferencesGetSet readeStringFromPreferences "+e);}
        return mCounter;
    }
}
