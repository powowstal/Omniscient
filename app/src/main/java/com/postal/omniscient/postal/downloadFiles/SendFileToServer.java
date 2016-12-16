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
    private String f_name = "";
    private String size_receive = "";
    private boolean receive = true;

    public SendFileToServer(File[] allFoldersFiles, Socket socket,
                            DataOutputStream dos, AdapterDownloadFlag is_downloadFlag) {
        this.allFoldersFiles =allFoldersFiles;
        this.socket = socket;
        this.dos = dos;
        this.is_downloadFlag = is_downloadFlag;
    }
    //отправка файлов на сервер
    private boolean sendData (){
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
                                if(!send(inFiles_in.getName().toString(), directory.getName().toString(), inFiles_in.toString())){
                                    return false;//закрывыем поток, при неудачной загрузке файла
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){Log.i(Msg, "Error SendFileToServer sendData "+e); return false;}
        return true;
    }
    //отправка файлов на сервер функция
    private boolean send(String file_Name, String folder_Name, String patch) {
        try {
            File myFile = new File(patch );
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(patch));
            long expect = myFile.length();

            byte[] buffer = new byte[1024*16];


            dos.writeUTF("start_transfer");//dos.flush();
            dos.writeUTF(file_Name);//dos.flush();
            dos.writeUTF(folder_Name);//dos.flush();
            dos.writeLong(expect);dos.flush();

            Log.i("MyMsg", "File  "+file_Name+" Bite "+expect);
            Long left = expect;
            int inlen = 0;
            Integer f_s = 0;
            int j = 0;

            //ожидаем ответа от сервера, для старта загрузки/докачки
            while (receive) {
                if (j >= 100){
                    return false;//если ответа нет - закрываем поток
                }
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i(Msg, "receive "+j);
                j++;
            }

            if (size_receive.equals("0") && !receive) {
                while (left > 0 && (inlen = bis.read(buffer, 0, (int) Math.min(left, buffer.length))) >= 0) {
                    dos.write(buffer, 0, inlen);
                    left -= inlen;
                } //_______________________________________________________________________________________
            } else //если файл существует и не догружен, то догружаем его
            if(file_Name.equals(f_name) && (f_s = Integer.parseInt(size_receive)) > 0 && !receive){
                //устанавливаем начало считывания байтов с места, после закачаных на сервер (докачка)
                left -= f_s;

                bis.skip(f_s);//количество байт которые нужно пропустить (они уже закачены)

                while (left > 0 && (inlen = bis.read(buffer, 0, (int) Math.min(left, buffer.length))) >= 0) {
                        dos.write(buffer, 0, inlen);
                        left -= inlen;
                }
            }
            try {
                dos.flush();
            }catch (Exception er){Log.i(Msg, "Error SendFileToServer sendData dos.flush() "+er);}
            if (left > 0) {
                Log.i(Msg,"We expected " + expect + " bytes but came up short by " + left);
            }
            if (bis.read() >= 0) {
                Log.i(Msg,"We expected only " + expect + " bytes, but additional data has been added to the file");
            }
            try {
                bis.close();
                wipeOff();
            }catch (Exception er){Log.i(Msg, "Error SendFileToServer sendData bis.close() "+er);}

        } catch (IOException e){Log.i(Msg, "Error SendFileToServer sendData "+e);}
        return true;
    }
    @Override
    public void run() {

        try {
            socket.setSoTimeout(0);//устанавливаем сокет без таймаута что бы загризился файл
            //когда долго грузится файл сокет может закрытся по таймауту
            //таймаут возобновляеться только при чтении информации с него

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
                sleep(5000);//первый поток бывает падает, проверяем на стабильность поток
                if(!sendData()){//закрывыем поток, при неудачной загрузке файла
                    is_downloadFlag.setTreadIsWork(false);
                    socket.setSoTimeout(25000);
                    return;
                }
                is_downloadFlag.setTreadIsWork(false);
                //начать отсылку битов для поддержки конекта
                //если не работает кип конекшн

                if (!new ThreadIsAliveOrNot("KeepConnection").liveORnot()) {
                    sleep(2000);
                    dos.writeUTF("isConnect");
                    dos.flush();
                    Log.i(Msg, " isConnect ");
                }
            }
            socket.setSoTimeout(25000);//возобновляем таймаут сокета
        } catch (Exception e) {
            Log.i(Msg, "Error SendFileToServer run " + e);
        }
        try{socket.setSoTimeout(25000);}catch (Exception e){Log.i(Msg, "Error SendFileToServer run " + e);}
    }
    //информация о файле -f_name (если существует докачать-size_resive, если не докачан)
    public void setMsg(String[] lines) {
        if(lines.length == 3) {
            f_name = lines[1];
            size_receive = lines[2];
            receive = false;
        }
    }
    //обнуляем данные о файле
    private void wipeOff(){
        f_name = "";
       size_receive = "";
       receive = true;
    }
}
