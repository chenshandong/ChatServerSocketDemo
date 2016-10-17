package com.example;

import java.io.BufferedReader;import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class tcpclient2 {
    private static final int PORT = 8088;
    private static ExecutorService exec = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {
        new tcpclient2();
    }

    public tcpclient2() {
        try {
            Socket socket = new Socket("192.168.1.143", PORT);
            exec.submit(new Sender(socket));
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg;
            while ((msg = br.readLine()) != null) {
                System.out.println(msg);
            }
        } catch (Exception e) {
        }
    }


    // 客户端线程获取控制台输入消息
    static class Sender implements Runnable {
        private Socket socket;

        public Sender(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                String msg;

                while (true) {
                    msg = br.readLine();
                    pw.println(msg);

                    if (msg.trim().equals("exit")) {
                        pw.close();
                        br.close();
                        exec.shutdownNow();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
