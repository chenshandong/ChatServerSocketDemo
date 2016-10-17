package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpService{

    //服务端口号
    public static final int SERVER_PORT =8088;

    //多线程服务
    private ExecutorService mExecutorService;

    //存储连接的客户端
    public static List<Socket> mClientList = new ArrayList<>();

    //服务开启路口
    public static void main(String[] args) {
        new TcpService();
    }


    public TcpService(){

        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("server start...");
            mExecutorService = Executors.newCachedThreadPool();
            while (true){

                Socket client = serverSocket.accept();
                System.out.println("server accept...");
                mClientList.add(client);

                ServerThread serverThread = new ServerThread(client);
                mExecutorService.submit(serverThread);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}