package com.goderyu.server;

/**
 * 用于开启多个服务器的类
 *
 * @author goderyu
 * @create 2017-11-10 下午9:18
 **/

public class Start {
    public static void main(String[] args){
        new Thread(() -> {
            try{
                System.out.println("登录服务器启动成功!");
                LoginServer.openServer();
            }catch (Exception e){
                e.printStackTrace();
            }

        }).start();
        new Thread(() -> {
            try{
                System.out.println("注册服务器启动成功!");
                RegServer.openServer();
            }catch (Exception e){
                e.printStackTrace();
            }

        }).start();
        new Thread(() -> {
            try{
                System.out.println("信息中转服务器启动成功!");
                UDPMessageServer.openServer();
            }catch (Exception e){
                e.printStackTrace();
            }

        }).start();
        new Thread(() -> {
            try{
                System.out.println("查询好友服务器启动成功!");
                QueryServer.openServer();
            }catch (Exception e){
                e.printStackTrace();
            }

        }).start();
    }
}
