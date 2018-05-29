package com.goderyu.service;

import com.goderyu.util.Config;
import net.sf.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 信息转发服务
 *
 * @author goderyu
 * @create 2017-11-10 上午1:17
 **/

public class MessageRegService extends Thread{

    // 每十秒钟向服务器注册一下
    public void run() {

        String uid= JSONObject.fromObject(Config.personal_json_data).getString("uid");
        String jsonStr="{\"type\":\"reg\",\"myUID\":\""+uid+"\"}";
        byte[] bytes=jsonStr.getBytes();

        while(true){
            try {
                DatagramPacket datagramPacket=new DatagramPacket(
                        bytes,bytes.length,InetAddress.getByName(Config.IP),28886
                );
                // 将更新消息发送给服务器
                client.send(datagramPacket);
                Thread.sleep(9999);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    private DatagramSocket client = null;
    public MessageRegService(DatagramSocket client){
        this.client=client;
        this.start();
    }
}
