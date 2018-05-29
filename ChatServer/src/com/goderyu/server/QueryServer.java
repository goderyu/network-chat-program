package com.goderyu.server;

import com.goderyu.db.UserService;
import com.goderyu.db.UsernameException;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 注册服务器
 *
 * @author goderyu
 * @create 2017-11-10 下午9:12
 **/

public class QueryServer implements Runnable {
    private Socket socket;

    public QueryServer(Socket socket){
        this.socket=socket;
    }

    public void run(){
        InputStream input=null;
        OutputStream output=null;
        try {
            input=socket.getInputStream();
            output=socket.getOutputStream();
            // 等待客户端发送消息
            byte[] bytes=new byte[1024];
            int len=input.read(bytes);
            String str = new String(bytes,0,len);
            JSONObject json=  JSONObject.fromObject(str);
            String username=json.getString("username");
            String password=json.getString("password");
            String sex=json.getString("sex");
            String email=json.getString("email");
            String phone=json.getString("phone");
            try {
                new UserService().regUser(username,password,sex,email,phone);
            } catch (UsernameException e) {
                output.write("{\"state\":1,\"msg\":\"用户名已存在，请重试！\"}".getBytes());
                output.flush();
                return;
            } catch (SQLException e) {
                output.write("{\"state\":2,\"msg\":\"未知错误，请重试！\"}".getBytes());
                output.flush();
                return;
            }
            output.write("{\"state\":0,\"msg\":\"恭喜您注册成功啦！\"}".getBytes());
            output.flush();



        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                input.close();
                output.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void openServer() throws IOException{
        ExecutorService service= Executors.newFixedThreadPool(1000);
        // 开启查询端口
        ServerSocket server=new ServerSocket(28885);
        while(true){
            Socket socket= server.accept();
            service.execute(new QueryServer(socket));
        }
    }
}
