package com.postal.omniscient.postal.reader.gps;

import android.content.Context;
import android.util.Log;

import com.postal.omniscient.postal.SPreferences.PreferencesGetSet;
import com.postal.omniscient.postal.write.json.WriteToJsonFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Александр on 10.01.2017.
 */
public class GPStoJSON {
    private static final String GPS = "GPS";
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String TIME = "TIME";
    private static String fileName = "GPS.json";
    private static String folder = "GPS";
    private static String Msg = "MyMsg";

    public void writeGPStoJSON(Context context) {
        try {
            PreferencesGetSet Sp = new PreferencesGetSet();
            Sp.setAPP_PREFERENCES_FILE_NAME(GPS);
            Sp.setAPP_PREFERENCES_KEY(LATITUDE);
            String lat = Sp.readeStringFromPreferences(context);
            Sp.setAPP_PREFERENCES_KEY(LONGITUDE);
            String lon = Sp.readeStringFromPreferences(context);
            Sp.setAPP_PREFERENCES_KEY(TIME);
            String time = Sp.readeStringFromPreferences(context);

            if (!lat.equals("") && !lat.equals("0")
                    && !lon.equals("") && !lon.equals("0")) {
                WriteToJsonFile writeToFile = new WriteToJsonFile(context);
                JSONObject gps = new JSONObject();//Заголовок
                JSONObject coordinates = new JSONObject();//gps координаты + время

                coordinates.put("Latitude", lat);
                coordinates.put("Longitude", lon);
                coordinates.put("Time", time);

                try {
                    gps.put("GPS", coordinates);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                writeToFile.writeFileSD(gps, fileName, folder);
            }
        }catch (Exception e){Log.i(Msg, "Error GPStoJSON writeGPStoJSON " + e);}
    }
}
