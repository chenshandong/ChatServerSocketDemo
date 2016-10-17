package com.chensd.chatserversocketdemo;

import android.os.Bundle;
import android.os.Message;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by chensd on 2016/9/22.
 */
public class ClientThread extends Thread {

    private Socket mClient;
    private MainActivity.MyHandler mHandler;
    private BufferedReader mBufferReader;
    private String msg = "";

    public ClientThread(Socket client, MainActivity.MyHandler handler){
        this.mClient = client;
        this.mHandler = handler;
    }

    @Override
    public void run() {

        while(true){
            try {

//                DataInputStream dis =  new DataInputStream(mClient.getInputStream());
//                byte[] bytes = new byte[1024];
//                int readBytes = 0;
//                int len = bytes.length;
//
//                while(readBytes < len){
//                    int read = dis.read(bytes, readBytes, len - readBytes);
//
//                    msg = new String(bytes, "UTF-8");
//                    Message message = Message.obtain();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("msg",msg);
//                    message.what = 2;
//                    message.setData(bundle);
//                    mHandler.sendMessage(message);
//
//                    if(read == -1){
//                        break;
//                    }
//
//                    readBytes += read;
//
//                }

                mBufferReader = new BufferedReader(new InputStreamReader(mClient.getInputStream()));
                if ((msg = mBufferReader.readLine()) != null){
                    msg += "\n";
                    Message message = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString("msg",msg);
                    message.what = 2;
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
