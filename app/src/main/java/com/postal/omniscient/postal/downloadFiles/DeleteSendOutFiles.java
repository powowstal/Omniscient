package com.postal.omniscient.postal.downloadFiles;

import android.util.Log;

import java.io.File;

/**
 * Created by Александр on 10.07.2016.
 */
public class DeleteSendOutFiles implements Runnable {
       private String Msg = "MyMsg";
    private String isLoaded = "isLoaded ";
    private String feleNAme;
    private Context context;

    public DeleteSendOutFiles(Context context) {
        this.context = context;
    }
    public void setFeleNAme(String feleNAme) {
        this.feleNAme = feleNAme;
    }

    /** Удаляем загруженые файлы */
    private void deleteFileIsLoaded(String line) {

        try {
            File fileDir = new File(context.getFilesDir(), "/Omniscient/"+line);
            if (fileDir.exists()) {
                fileDir.delete();
                Log.i(Msg, " DELETE  "+fileDir.toString());
            }



        } catch (Exception e){}

    }
    @Override
    public void run() {
        Log.i(Msg, "START DELETE FILES ");
        deleteFileIsLoaded(feleNAme.replaceFirst(isLoaded, ""));
    }
}
