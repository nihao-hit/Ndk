package com.example.nihao.ndk;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServerSocket {
    private ExecutorService pool = null;
    private List<Socket> sockets = new ArrayList<>();


    public MyServerSocket(){
        try{
            ServerSocket server = new ServerSocket(ImActivity.bindPort);
            pool = Executors.newCachedThreadPool();
            Log.d("debug","服务器已启动···");
            while(true){
                Socket client = server.accept();
                sockets.add(client);
                pool.execute(new ChatThread(client));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private class ChatThread implements Runnable{
        Socket socket;
        public ChatThread(Socket outSocket){
            socket = outSocket;
            try{
                socket.getOutputStream().write("成功连接服务器···\n".getBytes());
                Log.d("debug","成功连接服务器···");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        @Override
        public void run(){
            String msg = null;
            try{
                while(true){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    if((msg=reader.readLine()) != null){
                        if(msg.equals("0")) {
                            socket.getOutputStream().write("客户端断开连接···".getBytes());
                            Log.d("debug", "客户端断开连接···");
                            sockets.remove(socket);
                            reader.close();
                            socket.close();
                        }
                        else{


                            /*for(Socket socket:sockets) {
                                socket.getOutputStream().write((msg + "\n").getBytes());
                            }*/
                            socket.getOutputStream().write((msg + "\n").getBytes());
                            Message objMsg = new Message();
                            objMsg.what = 1;
                            Bundle bundle = new Bundle();
                            bundle.putString("msg",msg);
                            objMsg.setData(bundle);
                            ImActivity.mHandler.sendMessage(objMsg);
                        }
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
