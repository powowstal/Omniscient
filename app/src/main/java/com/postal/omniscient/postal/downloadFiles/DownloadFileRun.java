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
    private void start() {

        String server = "192.168.168.100";
        int port = 2221;
        try {
            socket = new Socket(server, port);
            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            send2();
            Log.d(Msg, "Postal work CHIDORY");


            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                String line;

                int a;
                while ((line = reader.readLine()) != null)
                    Log.d(Msg, "ANSWER " + line);


            } catch (IOException ex) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //outputStream = socket.getOutputStream();


    }
    /** Send a line of text */
    public void send(String text) {
        try {
            //outputStream.write((text + CRLF).getBytes());

            String fileName = text;
            File myFile = new File(fileName );
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
            long expect = myFile.length();

            byte[] buffer = new byte[socket.getSendBufferSize()];

            dos.writeUTF(text);
            dos.writeLong(expect);

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
