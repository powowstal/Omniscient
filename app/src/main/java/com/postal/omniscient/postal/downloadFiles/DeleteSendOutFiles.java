package com.postal.omniscient.postal.downloadFiles;

import android.util.Log;

import java.io.File;

/**
 * Created by Александр on 10.07.2016.
 */
public class DeleteSendOutFiles implements Runnable {
    private File[] allFoldersFiles;
    private String Msg = "MyMsg";
    private String isLoaded = "isLoaded ";
    private String feleNAme;

    public DeleteSendOutFiles(File[] allFoldersFiles) {
        this.allFoldersFiles = allFoldersFiles;
    }
    public void setFeleNAme(String feleNAme) {
        this.feleNAme = feleNAme;
    }

    /** Удаляем загруженые файлы */
    private void deleteFileIsLoaded(String line) {

        try {
            if (allFoldersFiles != null) {
                File f2;
                File[] files;
                for (File directory : allFoldersFiles) {
                    if (directory.isDirectory()) {
                        f2 = new File(directory.toString());
                        files = f2.listFiles();
                        for (File inFiles_in : files) {
                            if (inFiles_in.isFile()) {
                                Log.i(Msg, "is file " + inFiles_in.toString());
                                if (line.equals(inFiles_in.getName().toString()))
                                    new File(inFiles_in.toString()).delete();
                                Log.i(Msg, "DELETE "+inFiles_in.toString());
                            }
                        }
                    }
                }

            }

        } catch (Exception e){}

    }
    @Override
    public void run() {
        Log.i(Msg, "START DELETE FILES ");
        deleteFileIsLoaded(feleNAme.replaceFirst(isLoaded, ""));
    }
}
