package com.goderyu.view;

import com.goderyu.util.Config;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.swing.JPanel;

import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * 好友列表的列表面板
 *
 * @author goderyu
 * @create 2017-11-11 上午11:11
 **/


public class FriendsListJPanel extends JPanel {

    private static final long serialVersionUID = 1L;


    public FriendsListJPanel() {
        setLayout(null);

        updateFriends();


        Config.friendsListJPanel=this;
    }

    // 好友在线更新
    public void updateOnlineFriends(){
        // 在线列表
        String friendsOnlineList = Config.friend_online;
        String[] uids= friendsOnlineList.split(",");
        Set<String> keys= Config.list.keySet();
        for (String string : keys) {
            Config.list.get(string).setOnline(false);
        }
        if(!friendsOnlineList.equals("notFound") && !"".equals(friendsOnlineList)){
            for (String uid : uids) {
                FaceJPanel faceJPanel= Config.list.get(uid);
                faceJPanel.setOnline(true);
            }
        }
        Collection<FaceJPanel> faceJPanels= Config.list.values();
        List<FaceJPanel> list=new ArrayList(faceJPanels);
        Collections.sort(list);

        this.removeAll();
        int i=0;
        for (FaceJPanel faceJPanel: list) {
            faceJPanel.setBounds(0,i++ * 55,450, 52);
            this.add(faceJPanel);

        }
        this.setPreferredSize(new Dimension(0,52 * list.size()));
        this.updateUI();
    }


    // 实时更新在线好友
    public void updateFriends(){
        // 好友列表
        String friendsList =  Config.friend_json_data;

        JSONArray jsonArray=JSONArray.fromObject(friendsList);

        if(Config.list.size() == 0){
            // 第一次加载列表
            for (int i=0; i<jsonArray.size();i++) {
                JSONObject jsonObject=(JSONObject) jsonArray.get(i);
                Config.list.put(jsonObject.getString("uid"),new FaceJPanel(jsonObject.getString("img"),
                        jsonObject.getString("nickname"),
                        jsonObject.getString("description"),
                        jsonObject.getString("uid")));

            }
        }else{
            // 已经有列表数据了
            for (int i=0; i<jsonArray.size();i++) {
                JSONObject jsonObject=(JSONObject) jsonArray.get(i);
                String uid = jsonObject.getString("uid");

                FaceJPanel faceJPanel=Config.list.get(uid);
                if(faceJPanel!=null){
                    // 已经存在,需要更新
                    faceJPanel.setNickName(jsonObject.getString("nickname"));
                    faceJPanel.setDescription(jsonObject.getString("description"));
                    faceJPanel.setImage(jsonObject.getString("img"));
                }else{
                    // 不存在列表数据时，新建列表数据
                    Config.list.put(jsonObject.getString("uid"),new FaceJPanel(jsonObject.getString("img"),
                            jsonObject.getString("nickname"),
                            jsonObject.getString("description"),
                            jsonObject.getString("uid")));
                }
            }

        }
        updateOnlineFriends();


    }


}
