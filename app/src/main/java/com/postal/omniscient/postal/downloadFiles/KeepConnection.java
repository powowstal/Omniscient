package com.postal.omniscient.postal.downloadFiles;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.postal.omniscient.postal.adapter.AdapterDownloadFlag;
import java.io.DataOutputStream;
import java.io.IOException;

/**держим конкт с сервером
 * Created by Александр on 17.07.2016.
 */
public class KeepConnection extends Thread {
    private String Msg = "MyMsg";
    private DataOutputStream dos;
    private AdapterDownloadFlag is_downloadFlag;
    private boolean is_KeepConnectionFlag;
    private Context context;

    public KeepConnection (DataOutputStream dos, AdapterDownloadFlag is_downloadFlag,
                           boolean is_KeepConnectionFlag, Context context){
        this.dos = dos;
        this.is_downloadFlag = is_downloadFlag;
        this.is_KeepConnectionFlag = is_KeepConnectionFlag;
        this.context = context;
    }

    @Override
    public void run() {
        final long sleep_time = 10*1000;
        try {
            is_KeepConnectionFlag = false;
            sleep(sleep_time);
            is_KeepConnectionFlag = true;
            if (!is_downloadFlag.getTreadIsWork()) {
                dos.writeUTF("isConnect");
                dos.flush();
                Log.e(Msg,"isConnect ");
            }
        } catch (IOException e) {
            Log.e(Msg,"Error KeepConnection1 "+ e.toString());
            //.................................................................................
            //notGiveUp();
        } catch (InterruptedException e) {
            Log.e(Msg,"Error KeepConnection2 "+ e.toString());
        }
    }
    private void notGiveUp() {
        try {
            String requiredPermission = "notGiveUpConnectCheckReceiver";
            context.sendBroadcast(new Intent(requiredPermission));
        }catch (Exception e){Log.i(Msg, "Error KeepConnection notGiveUp " +e);}
    }

}
