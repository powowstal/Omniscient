package com.postal.omniscient.postal.downloadFiles;

import android.content.Intent;
import android.util.Log;

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

    public DownloadFileRun(File[] allFoldersFiles) {
        this.allFoldersFiles = allFoldersFiles;
    }

    private void start() {

        String server = "192.168.1.114";
        int port = 2221;

        String isLoaded = "isLoaded ";
        try { long fff = 13;
            socket = new Socket();//(server, port);
            socket.connect(new InetSocketAddress(server, port),2000);
            socket.setSoTimeout(60000);
            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            send2();
            Log.d(Msg, "Postal work CHIDORY");



            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                String line;

                int a;

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
                    if (line.equals("isConnect")) {

                            Thread thread = new Thread(){
                                public void run(){
                                    final long sleep_time = 3000;
                                    try {
                                    sleep(sleep_time);
                                        dos.writeUTF("isConnect");
                                        dos.flush();
                                        Log.i(Msg, "Thread Running isConnect");
                                    } catch (IOException e) {
                                        Log.e(Msg, e.toString());
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                            };
                            thread.start();
                    }
                    if (line.equals("ok")) {
                        dos.writeUTF("isConnect");//начать поддержку соединения
                        dos.flush();
                        if (allFoldersFiles != null) {
                            //отправка в новом потоке
                            SendFileToServer dwnloadFile = new SendFileToServer(allFoldersFiles, socket, dos);
                            Thread startDownlow = new Thread(dwnloadFile);
                            //поток для загрузки файлов на сервер
                            startDownlow.start();


                        }
                    } //else break;dos.close();socket.close();
                }
            } catch (IOException ex) { Log.e(Msg, "Eror "+ex);
                dos.close();socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //outputStream = socket.getOutputStream();


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
