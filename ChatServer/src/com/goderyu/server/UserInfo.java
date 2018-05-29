package com.goderyu.server;

import java.net.Socket;

/**
* @author 于好贤
* @version 创建时间：2017年11月7日 下午9:24:42
* @description 
*/
public class UserInfo {
	private String uid;
	private String userName;
	private Socket socket;
	private String udpip;
	private int udpport;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public String getUdpip() {
		return udpip;
	}
	public void setUdpip(String udpip) {
		this.udpip = udpip;
	}
	public int getUdpport() {
		return udpport;
	}
	public void setUdpport(int udpport) {
		this.udpport = udpport;
	}
	
}
