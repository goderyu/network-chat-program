package com.goderyu.service;

import com.goderyu.util.Config;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 登录服务器,与服务器保持连接状态
 * 1.更新好友在线状态 5秒钟更新一次
 * 2.登录验证
 * 3.退出账户
 * @author goderyu
 * @create 2017-11-09 下午11:36
 **/

public class NetService implements Runnable{

    private NetService(){}
    // 对象
    private static NetService netService = new NetService();
    // 饿汉式单例对象
    public static NetService getNetService(){
        return netService;
    }

    // 这里准备与服务器保持长时间通讯
    public void run() {
        try{
            byte[] bytes=new byte[1024*10];
            int len=0;

            // 好友在线的实时更新
            while(run){
                output.write("U0002".getBytes());
                output.flush();
                input.read();
                output.write(Config.friend_list_data.getBytes());
                output.flush();
                len= input.read(bytes);
                String online=new String(bytes,0,len);
                System.out.println("在线账户："+online);
                try{
                    if(!Config.friend_online.equals(online)){
                        Config.friend_online=online;
                        Config.friendsListJPanel.updateOnlineFriends();
                    }
                } catch (Exception e){
                }
                Config.friend_online=online;

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            run=false;
        }

    }



    private Socket socket=null;
    private InputStream input=null;
    private OutputStream output=null;
    private Thread thread=null;
    private boolean run=false;

    public JSONObject login()throws UnknownHostException,IOException{
        socket=new Socket(Config.IP,Config.LOGIN_PORT);
        input=socket.getInputStream();
        output=socket.getOutputStream();
        String json_str="{\"username\":\""+ Config.username+ "\",\"password\":\""+ Config.password +"\"}";
        // 开始与服务器传递消息
        output.write(json_str.getBytes());
        output.flush();
        // 等待服务器回执消息
        byte[] bytes=new byte[1024];
        int len = input.read(bytes);

        json_str=new String(bytes,0,len);
        JSONObject json=JSONObject.fromObject(json_str);
        // 如果是0就是登录成功
        if(json.getInt("state") == 0){
            // 开启持续的网络连接服务

            if(thread!=null ){
                // 询问线程是否还活着
                if(thread.getState()==Thread.State.RUNNABLE){
                    // 终止线程运行
                    run=false;
                    try {
                        thread.stop();
                    }catch (Exception e){
                    }
                }

            }

            // 好友信息获得
            output.write("U0001".getBytes());
            output.flush();
            bytes=new byte[1024*10];
            len=input.read(bytes);
            String jsonstr=new String(bytes,0,len);
            System.out.println("好友资料："+jsonstr);
            // 解析好友列表以便询问
            Config.jiexi_friend_json_data(jsonstr);
            // 个人资料获得
            output.write("U0003".getBytes());
            output.flush();
            len=input.read(bytes);
            Config.personal_json_data=new String(bytes,0,len);
            System.out.println("个人资料："+Config.personal_json_data);

            // 启动UDP服务器
            Config.datagramSocket_client=new DatagramSocket();
            // 启动用户数据包
            new MessageRegService(Config.datagramSocket_client);
            // 启动信息服务
            new MessageService(Config.datagramSocket_client);


            // 重新开启线程与服务器保持通信
            thread=new Thread(this);
            run=true;
            thread.start();
        }
        return json;
    }


}
