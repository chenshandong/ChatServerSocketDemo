package com.chensd.chatserversocketdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends Activity {

    private Button mInButton, mSendButton;
    private EditText mEditText;
    private EditText mEditChatRecord;
    private static final String SERVERIP = "192.168.1.143";
    private static final int SERVERPORT = 8088;

    private static final String TAG = "tcp_client";
    private PrintWriter mPrintWriter;
    private MyHandler mHandler;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInButton = (Button) findViewById(R.id.myinternet_tcpclient_BtnIn);
        mSendButton = (Button) findViewById(R.id.myinternet_tcpclient_BtnSend);
        mEditChatRecord = (EditText) findViewById(R.id.myinternet_tcpclient_EditText01);
        mEditText = (EditText) findViewById(R.id.myinternet_tcpclient_EditText02);

        mHandler = new MyHandler(mEditChatRecord);

        //登录操作
        mInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        login();
                    }
                }).start();
            }
        });

        mSendButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = mEditText.getText().toString().trim();
                mPrintWriter.println(msg);
                mPrintWriter.flush();

                mEditText.setText("");
            }
        });

    }

    //登录
    private void login() {

        if (socket != null) {
            return;
        }

        try {
            socket = new Socket(SERVERIP, SERVERPORT);
            mPrintWriter = new PrintWriter(socket.getOutputStream(), true);

            mHandler.sendEmptyMessage(1);


        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }


    }


    class MyHandler extends Handler {

        private TextView mTv;

        public MyHandler(TextView content) {
            this.mTv = content;

        }

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 1) {

                ClientThread clientThread = new ClientThread(socket, mHandler);
                clientThread.start();

            }
            else {
                Bundle data = msg.getData();
                String content = data.getString("msg");

                mTv.append(content);

            }

        }
    }

}