package com.goderyu.util;


import com.goderyu.view.ChatFrame;
import com.goderyu.view.FaceJPanel;
import com.goderyu.view.FriendsListJPanel;
import com.goderyu.view.Msg;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.net.DatagramSocket;
import java.util.Hashtable;
import java.util.Vector;

//配置类
public class Config {
    // 服务器地址
    public static final String IP = "127.0.0.1";
    // 登录端口
    public static final int LOGIN_PORT = 28888;
    // 注册端口
    public static final int REG_PORT = 28887;
    // 用户名和密码寄存
    public static String username;
    public static String password;

    public static FriendsListJPanel friendsListJPanel;

    // 好友信息列表 JSON格式
    public static String friend_json_data="";
    // 好友信息列表寄存 解析分离后的一个一个的信息
    public static String friend_list_data="";

    /**
     * 取出好友列表值
     * @param friend_json_data
     */
    public static void jiexi_friend_json_data(String friend_json_data){
        Config.friend_json_data = friend_json_data;
        JSONArray json=  JSONArray.fromObject(friend_json_data);
        StringBuffer stringBuffer=new StringBuffer();
        for (int i=0;i< json.size();i++){
            JSONObject jsonobj=(JSONObject) json.get(i);
            stringBuffer.append(jsonobj.getString("uid"));
            stringBuffer.append(",");
        }
        friend_list_data=stringBuffer.toString();

    }

    // 个人资料
    public static String personal_json_data="";
    // 好友在线
    public static String friend_online="";
    // UDP的发送和接收以及心跳端
    public static DatagramSocket datagramSocket_client=null;

    // 聊天窗口登记
    public static Hashtable<String , ChatFrame> chatTable = new Hashtable();
    // 显示聊天窗口
    public static void showChatFrame(String uid, String nickname, String img,
                                     String info, Vector<Msg> msgs){
        if(chatTable.get(uid)==null){
            ChatFrame chatFrame = new ChatFrame(uid,nickname,img,info,msgs);
            chatTable.put(uid,chatFrame);
        }else{
            chatTable.get(uid).setAlwaysOnTop(true);
            chatTable.get(uid).setVisible(true);
        }
    }
    public static void closeChatFrame(String uid){
        chatTable.remove(uid);
    }
    // 好友列表对象
    public static Hashtable<String,FaceJPanel> list=new Hashtable();


}
