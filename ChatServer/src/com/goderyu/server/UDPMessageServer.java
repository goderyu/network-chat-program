package com.goderyu.server;

import net.sf.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 负责信息中转,使用UDP协议
 *
 * @author goderyu
 * @create 2017-11-10 上午12:27
 **/

public class UDPMessageServer implements Runnable{
    private DatagramPacket packet = null;
    public UDPMessageServer(DatagramPacket packet){
        this.packet=packet;
    }

    // 业务处理
    public void run() {
        try{
            String jsonStr=new String(packet.getData(),0,packet.getLength());
            JSONObject json = JSONObject.fromObject(jsonStr);

            // 处理UDP数据包
            if(json.getString("type").equals("reg")){
                String MyUID = json.getString("myUID");

                // 更新最新的IP和端口号
                UserOnlineList.getUserOnlineList().updateOnlinUDP(MyUID,
                        packet.getAddress().getHostAddress(),packet.getPort());
                System.out.println("有注册信息发来：" + jsonStr);

                // 处理消息转发,处理确认消息
            } else if(json.getString("type").equals("msg") || json.getString("type").equals("qr")){
                String MyUID = json.getString("myUID");
                String toUID = json.getString("toUID");
                // 更新最新的IP和端口号
                UserOnlineList.getUserOnlineList().updateOnlinUDP(MyUID,
                        packet.getAddress().getHostAddress(),packet.getPort());
                // 获得要接收你消息的人
                UserInfo toUserInfo= UserOnlineList.getUserOnlineList().getOnlineUserInfo(toUID);
                // 准备转发到客户端的数据包
                DatagramPacket datagramPacket=new DatagramPacket(packet.getData(),packet.getLength(),
                        InetAddress.getByName(toUserInfo.getUdpip()),
                        toUserInfo.getUdpport());
                // 发出数据包
                datagramSocket.send(datagramPacket);

            }
        } catch(Exception e){
            e.printStackTrace();
        }




    }
    private static DatagramSocket datagramSocket=null;
    // 启动服务器
    public static void openServer() throws Exception{
        datagramSocket=new DatagramSocket(28886);
        // 制作线程池
        ExecutorService executorService= Executors.newFixedThreadPool(1000);

        while(true){
            try{
                // 等待客户端的数据
                byte[] b=new byte[1024];
                DatagramPacket datagramPacket=new DatagramPacket(b,b.length);
                datagramSocket.receive(datagramPacket);

                // 数据一旦到手后，立马抓出一个线程处理
                executorService.execute(new UDPMessageServer(datagramPacket));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
