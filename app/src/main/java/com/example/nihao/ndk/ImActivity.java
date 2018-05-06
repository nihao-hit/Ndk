package com.example.nihao.ndk;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImActivity extends Activity implements View.OnClickListener {
    private ExecutorService pool = Executors.newCachedThreadPool();
    public static String LOCALIP;
    public static List<String> mainMsgList;
    Button setServerIp;
    Button setServerPort;
    Button connect;
    Button disconnect;
    EditText edit;
    Button send;
    static RecyclerView recycler;
    static MyAdapter adapter;
    public static int bindPort = 10000;
    public static int serverPort;
    public static String serverIp;
    static Socket client;
    public static Handler mHandler = new Handler() {
        public void handleMessage(Message objMsg) {
            super.handleMessage(objMsg);
            switch (objMsg.what) {
                case 1:
                    String msg = objMsg.getData().getString("msg");
                    Log.d("debug","信息为："+msg);
                    mainMsgList.add(msg);
                    adapter.notifyItemInserted(mainMsgList.size() - 1);
                    recycler.scrollToPosition(mainMsgList.size() - 1);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_im);
        new Thread(new Runnable(){
            @Override
            public void run(){
                new MyServerSocket();
            }
        }).start();
        initMainMsgList();
        initView();
    }

    private void initMainMsgList() {
        LOCALIP = getIP(this);
        mainMsgList = new ArrayList<>();
        mainMsgList.add(LOCALIP + "-0-这是第一条消息");
    }

    private void initView() {
        setServerIp = (Button) findViewById(R.id.set_server_ip);
        setServerPort = (Button) findViewById(R.id.set_server_port);
        connect = (Button) findViewById(R.id.connect);
        disconnect = (Button) findViewById(R.id.disconnect);
        edit = (EditText) findViewById(R.id.edit);
        send = (Button) findViewById(R.id.send);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        setServerIp.setOnClickListener(this);
        setServerPort.setOnClickListener(this);
        connect.setOnClickListener(this);
        disconnect.setOnClickListener(this);
        send.setOnClickListener(this);
        initRecycler();
    }

    private void initRecycler() {
        LinearLayoutManager manager = new LinearLayoutManager(ImActivity.this);
        adapter = new MyAdapter(mainMsgList);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);
        Log.d("eee", mainMsgList.hashCode() + ":::");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_server_ip:
                showInputDialog(setServerIp.getText().toString());
                break;
            case R.id.set_server_port:
                showInputDialog(setServerPort.getText().toString());
                break;
            case R.id.connect:
                pool.execute(new ClientSocket());
                break;
            case R.id.disconnect:
                if (client.isConnected())
                    pool.execute(new Disconnect());
                break;
            case R.id.send:
                pool.execute(new SendMsg());
                edit.setText("");
                break;
        }
    }

    private void showInputDialog(final String data) {
        final EditText editText = new EditText(this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(this);
        inputDialog.setTitle("请输入信息!!!").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (data) {
                            case "设置IP":
                                serverIp = editText.getText().toString();
                                break;
                            case "设置端口":
                                serverPort = Integer.valueOf(editText.getText().toString());
                                break;
                        }
                    }
                })
                .setCancelable(false).show();
    }

    private class ClientSocket implements Runnable{
        @Override
        public void run(){
            try {
                client = new Socket(serverIp, serverPort);
                BufferedReader reader = null;
                while (true) {
                    reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String msg = null;
                    if((msg = reader.readLine()) != null) {
                        Message objMsg = new Message();
                        objMsg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", msg);
                        objMsg.setData(bundle);
                        mHandler.sendMessage(objMsg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendMsg implements Runnable{
        @Override
        public void run(){
            if (client.isConnected()) {
                try {
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    String time = format.format(new Date());
                    String relMsg = edit.getText().toString();
                    byte[] msg = (LOCALIP + "-" + time + "-" + relMsg + "\n").getBytes();
                    client.getOutputStream().write(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class Disconnect implements Runnable{
        @Override
        public void run(){
            try{
                client.getOutputStream().write("0\n".getBytes());
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public static String getIP(Context context) {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
