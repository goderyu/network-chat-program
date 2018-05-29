package com.goderyu.server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
* @author 于好贤
* @version 创建时间：2017年11月7日 下午9:20:23
* @description 用户在线列表
*/
public class UserOnlineList {
	// 单例模式
	private UserOnlineList() {

	}

	private static UserOnlineList userOnlineList = new UserOnlineList();

	public static UserOnlineList getUserOnlineList() {
		return userOnlineList;
	}

	// 把所有的在线账户  全部登记在集合中
	// String 传的是用户的编号
	private HashMap<String, UserInfo> hashMap = new HashMap<String, UserInfo>();

	// 注册在线用户
	public void regOnline(String uid, Socket socket, String username) {
		// 判断其他的客户端是否登录一样的用户名,如果一样,强行退出
		UserInfo userInfo = hashMap.get(uid);
		if (userInfo != null) {
			try {
				try {
					userInfo.getSocket().getOutputStream().write(4);
				} catch (Exception e) {
				}
				userInfo.getSocket().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		userInfo = new UserInfo();
		userInfo.setUid(uid);
		userInfo.setUserName(username);
		userInfo.setSocket(socket);
		// 登记在线
		hashMap.put(uid, userInfo);
	}

	/**
	 * 
	 * @param uid 用户编号
	 * @param ip UDP IP地址
	 * @param port UDP端口
	 * @throws NullPointerException 空指针异常
	 */
	public void updateOnlinUDP(String uid, String ip, int port) throws NullPointerException {
		UserInfo userInfo = hashMap.get(uid);
		userInfo.setUdpip(ip);
		userInfo.setUdpport(port);

	}

	// 判断用户是否在线 TRUE为在线 FALSE为不在线
	public boolean isUserOnline(String uid) {
		return hashMap.containsKey(uid);
	}

	// 获得在线用户信息
	public UserInfo getOnlineUserInfo(String uid) {
		return hashMap.get(uid);
	}

	// 下线
	public void logout(String uid) {
		hashMap.remove(uid);
	}

	// 获得所有的在线信息
	public Set<String> getUserInfo() {
		return hashMap.keySet();
	}
}
