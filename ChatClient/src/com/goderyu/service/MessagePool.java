package com.goderyu.service;

import com.goderyu.util.Config;
import com.goderyu.view.ChatFrame;
import com.goderyu.view.FaceJPanel;
import com.goderyu.view.Msg;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * 消息池 会把所有的消息接收到池里面进行存储
 *
 * @author goderyu
 * @create 2017-11-10 上午4:31
 **/

public class MessagePool {

    private MessagePool(){}

    private static MessagePool messagePool = new MessagePool();

    public static MessagePool getMessagePool(){
        return messagePool;
    }

    public static HashMap<String, LinkedList<Msg>> hashMap = new HashMap();

    // 不管是给谁的消息，都应该在池里存储
    // {"type":"msg","toUID":"","myUID":"","msg":"","code":""}
    public void addMessage(String json){
        JSONObject jsonObject= JSONObject.fromObject(json);
        String toUID= jsonObject.getString("toUID");
        String myUID= jsonObject.getString("myUID");
        String msg= jsonObject.getString("msg");
        String type= jsonObject.getString("type");
        String code= jsonObject.getString("code");

        // 把接收好的消息 包装在Msg对象内
        Msg msgObj=new Msg();
        msgObj.setCode(code);
        msgObj.setMyUID(myUID);
        msgObj.setToUID(toUID);
        msgObj.setType(type);
        msgObj.setMsg(msg);
        try{
            ChatFrame chatFrame = Config.chatTable.get(myUID);
            if(chatFrame.isVisible()){
                chatFrame.addMessage(msgObj);
            }else{
                throw new Exception();
            }

        } catch (Exception e){
            FaceJPanel faceJPanel= Config.list.get(myUID);
            faceJPanel.addMessage(msgObj);

//            // 链表集合存储Msg对象，以便今后读取里面的消息
//            LinkedList<Msg> list = hashMap.get(myUID);
//            if(list==null){
//                list=new LinkedList();
//            }
//            list.add(msgObj);
//            hashMap.put(myUID, list);
        }
    }

}
