package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by chensd on 2016/9/22.
 */
public class ServerThread extends Thread {

    private Socket mClient;
    private PrintWriter mPrintWriter;
    private String mStrMsg;
    private BufferedReader mBufferReader;

    public ServerThread(Socket client) throws IOException {
        this.mClient = client;
        mStrMsg = client.getInetAddress() + "come total:" + TcpService.mClientList.size();
        sendMessage();
    }

    @Override
    public void run() {

        try {
            mBufferReader = new BufferedReader(new InputStreamReader(mClient.getInputStream()));

            while ((mStrMsg = mBufferReader.readLine()) != null) {

                System.out.println(mClient.getInetAddress()+"isAlive:" + mStrMsg);

                if (mStrMsg.equals("exit")) {
                    TcpService.mClientList.remove(mClient);
                    mBufferReader.close();
                    mPrintWriter.close();

                    mStrMsg = mClient.getInetAddress() + "exit total:" + TcpService.mClientList.size();
                    sendMessage();
                    mClient.close();

                    break;
                }
                else {

                    mStrMsg = mClient.getInetAddress() + ":" + mStrMsg;
                    sendMessage();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            try {
                if(mBufferReader != null){
                    mBufferReader.close();
                }

                if(mPrintWriter != null){
                    mPrintWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void sendMessage() throws IOException {

        for (Socket client : TcpService.mClientList) {

            System.out.println("send to client..." + mStrMsg);
            mPrintWriter = new PrintWriter(client.getOutputStream(), true);
            mPrintWriter.println(mStrMsg);

        }

    }

}
