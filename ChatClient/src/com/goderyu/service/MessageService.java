package com.goderyu.service;



import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * 接收服务器的中转消息
 *
 * @author goderyu
 * @create 2017-11-10 上午1:17
 **/

public class MessageService extends Thread{

    public void run() {
        while(true){
            try {
                byte[] bytes=new byte[1024*32];
                DatagramPacket datagramPacket=new DatagramPacket(bytes,bytes.length);
                client.receive(datagramPacket);
                MessagePool
                        .getMessagePool()
                        .addMessage(
                                new String(datagramPacket.getData(),
                                        0,datagramPacket.getData().length));
//                System.out.println("datagramPacket.getData().length == "+datagramPacket.getData().length);
//                System.out.println("bytes.length == "+bytes.length);
//                System.out.println("bytes == "+ Arrays.toString(bytes));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private DatagramSocket client = null;
    public MessageService(DatagramSocket client){
        this.client=client;
        this.start();
    }
}
