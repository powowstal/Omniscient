package com.postal.omniscient.postal.downloadFiles;

import android.util.Log;

import com.postal.omniscient.postal.ThreadIsAliveOrNot;
import com.postal.omniscient.postal.adapter.AdapterDownloadFlag;

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
    //отправка файлов на сервер
    private void sendData (){
        try {
            File f2;
            File[] files;
            for (File directory : allFoldersFiles) {
                if (directory.isDirectory()) {
                    f2 = new File(directory.toString());
                    files = f2.listFiles();
                    for (File inFiles_in : files) {
                        if (inFiles_in.isFile()) {
                            //файлы отправляются если существуют ) если файл еще пишется пропускаем его!
                            if (!(inFiles_in.getName().toString()).startsWith("no_written")) {
                                send(inFiles_in.getName().toString(), directory.getName().toString(), inFiles_in.toString());
                            }
                        }
                    }
                }
            }
        }catch (Exception e){Log.i(Msg, "Error SendFileToServer sendData "+e);}
    }
    //отправка файлов на сервер функция
    public void send(String file_Name, String folder_Name, String patch) {
        try {
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
        } catch (IOException e){Log.i(Msg, "Error SendFileToServer sendData "+e);}
    }
    @Override
    public void run() {
        try {
            Boolean tOf = true;
            tOf = new ThreadIsAliveOrNot("AlarmReceiver").liveORnot();
            //эсли идет запись файлов, подождать 3 секунды и проверить заново идет ли запись (4 раза проверить)
            for (int i = 0; i < 4; i++) {
                if (tOf) {
                    sleep(3000);
                    tOf = new ThreadIsAliveOrNot("AlarmReceiver").liveORnot();
                }
            }
            if (!tOf) {//проверяем не идет ли запись файлов что бы не загружать не дописаные файлы
                is_downloadFlag.setTreadIsWork(true);// не отсылать бит инфы на сервер для поддержания конекта
                sendData();
                is_downloadFlag.setTreadIsWork(false);
                //начать отсылку битов для поддержки конекта
                //если не работает кип конекшн

                if (!new ThreadIsAliveOrNot("KeepConnection").liveORnot()) {
                    dos.writeUTF("isConnect");
                    dos.flush();
                }
            }
            //////////////////////////////////////////////////////
            sleep(3000);
            socket.close();
            /////////////////////////////////////////////////////
        } catch (Exception e) {
            Log.i(Msg, "Error SendFileToServer run " + e);
        }
    }
}
