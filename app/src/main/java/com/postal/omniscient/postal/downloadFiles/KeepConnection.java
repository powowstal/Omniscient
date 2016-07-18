package com.postal.omniscient.postal.downloadFiles;

import android.util.Log;

import com.postal.omniscient.postal.adapter.AdapterDownloadFlag;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;

/**
 * Created by Александр on 17.07.2016.
 */
public class KeepConnection extends Thread {
    private String Msg = "MyMsg";
    private DataOutputStream dos;
    private AdapterDownloadFlag is_downloadFlag;


    public KeepConnection (DataOutputStream dos, AdapterDownloadFlag is_downloadFlag){
        this.dos = dos;
        this.is_downloadFlag = is_downloadFlag;
    }
    @Override
    public void run() {
        final long sleep_time = 3*1000;
        try {
            sleep(sleep_time);
            if (!is_downloadFlag.getTreadIsWork()) {
            dos.writeUTF("isConnect");
            dos.flush();
        }
        } catch (IOException e) {
            Log.e(Msg, e.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
