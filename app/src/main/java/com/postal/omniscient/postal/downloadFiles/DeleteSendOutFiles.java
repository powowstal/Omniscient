package com.postal.omniscient.postal.downloadFiles;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * Удаляем отправленые файлы
 * Created by Александр on 10.07.2016.
 */
public class DeleteSendOutFiles extends Thread {
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
            }
        } catch (Exception e){}

    }
    @Override
    public void run() {
        deleteFileIsLoaded(feleNAme.replaceFirst(isLoaded, ""));
    }
}
