package com.goderyu.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.goderyu.db.*;

import com.goderyu.db.UserInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
* @author 于好贤
* @version 创建时间：2017年11月7日 下午9:10:08
* @description 服务器的登录类
*/
public class LoginServer implements Runnable {

	private Socket socket = null;

	public LoginServer(Socket socket) {
		this.socket = socket;
	}

	// 线程方法
	public void run() {

		// 登录操作
		InputStream in = null;
		OutputStream out = null;
		String uid = null;
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
			// 等待客户端发来用户名和密码
			byte[] bytes = new byte[1024];
			int len = in.read(bytes);
			String json_str = new String(bytes, 0, len);
			// 解析
			JSONObject json = JSONObject.fromObject(json_str);
			String username = json.getString("username");
			String password = json.getString("password");

			// 判断
			try {
				uid = new UserService().login(username, password);
				// 登记登录信息
				UserOnlineList.getUserOnlineList().regOnline(uid, socket, username);
				out.write("{\"state\":0,\"msg\":\"登录成功!\"}".getBytes());
				out.flush();
				// 死循环保持用户不断开,一直保持在线状态,并且一直接收客户端发送的请求
				while (true) {
					bytes = new byte[2048];
					len = in.read(bytes);
					String command = new String(bytes, 0, len);
					if (command.equals("U0001")) {
						// 更新好友列表

						Vector<UserInfo> userInfos=new UserService().getFriendsList(uid);
						out.write(JSONArray.fromObject(userInfos).toString().getBytes());
						out.flush();

					} else if (command.equals("U0002")) {
						out.write(1);
						out.flush();
						// 更新好友在线,获得好友的列表编号
						len=in.read(bytes);// 123456,45678913,456465456645,789978789978,123456789
						String str=new String(bytes,0,len);
						String[] ids=str.split(",");

						StringBuffer stringBuffer=new StringBuffer();
						for (String string:ids) {
							if(UserOnlineList.getUserOnlineList().isUserOnline(string)) {
								stringBuffer.append(string);
								stringBuffer.append(",");
							}
						}
						if(stringBuffer.length()==0){
							// 没有好友在线
							out.write("notFound".getBytes());
							out.flush();
						}else{
							// 回执好友在线列表
							out.write(stringBuffer.toString().getBytes());
							out.flush();
						}

					} else if (command.equals("U0003")) {
						// 更新个人资料
						UserInfo2 userInfo2= new UserService().getUserInfo(uid);
						out.write(JSONObject.fromObject(userInfo2).toString().getBytes());
						out.flush();


					} else if (command.equals("E0001")) {
						// 保存个人资料
					} else if (command.equals("EXIT")) {
						// 退出用户登录
						UserOnlineList.getUserOnlineList().logout(uid);
						return;
					}

				}

			} catch (UsernameNotFoundException e) {
				out.write("{\"state\":2,\"msg\":\"用户名错误!\"}".getBytes());
				out.flush();
				return;
			} catch (PasswordException e) {
				out.write("{\"state\":1,\"msg\":\"密码错误!\"}".getBytes());
				out.flush();
				return;
			} catch (StateException e) {
				out.write("{\"state\":3,\"msg\":\"账户已停用!\"}".getBytes());
				out.flush();
				return;
			} catch (SQLException e) {
				out.write("{\"state\":4,\"msg\":\"未知错误!\"}".getBytes());
				out.flush();
				return;
			}

		} catch (Exception e) {

		} finally {
			// 结束后把连接关闭
			try {
				// 如果突然关闭或者关闭,在列表中去除此用户
				UserOnlineList.getUserOnlineList().logout(uid);
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static void openServer() {
		// 线程池
		ExecutorService execute = Executors.newFixedThreadPool(1000);
		try {
			// 开启了TCP 28888端口用于登录业务
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(28888);
			// 无限循环用于一直服务
			while (true) {
				Socket socket = server.accept();
				socket.setSoTimeout(10000);
				execute.execute(new LoginServer(socket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		openServer();
	}

}
