package com.goderyu.view;

import com.goderyu.util.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

/**
 * 好友在线头像
 *
 * @author goderyu
 * @create 2017-11-11 下午1:10
 **/

public class FaceJPanel extends JPanel implements Comparable<FaceJPanel>,MouseListener,Runnable{
    private String image;
    private String nickName;
    private String description;
    private String uid;
    private JLabel label_image;
    private JLabel label_nickName;
    private JLabel label_des;
    public FaceJPanel(String image,String nickName,String description,String uid){
        this.image=image;
        this.nickName=nickName;
        this.description=description;
        this.uid=uid;
        this.setLayout(null);



        label_image = new JLabel();
        label_image.setBounds(0, 0, 50, 50);
        add(label_image);
        setImage(image);

        label_nickName = new JLabel();
        label_nickName.setBounds(62, 0, 382, 32);
        label_nickName.setText(nickName);
        add(label_nickName);
        label_nickName.setFont(new Font("Lucida Grande", Font.BOLD, 16));

        label_des = new JLabel();
        label_des.setBounds(62, 30, 382, 16);
        label_des.setText(description);
        add(label_des);

        this.addMouseListener(this);

    }
    // 所有的消息
    private Vector<Msg> msgs = new Vector<>();
    // 寄存消息
    boolean run = true;

    public void run() {
        run=true;
        int x = this.getX();
        int y = this.getY();

        while(run){

            this.setLocation(x-1,y-1);
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.setLocation(x+2,y+2);
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.setLocation(x,y);
    }

    private Thread thread = null;

    public void addMessage(Msg msg){
        // 添加消息进去
        msgs.add(msg);
        if(thread==null){
            thread=new Thread(this);
            thread.start();
        }else if(thread.getState()==Thread.State.TERMINATED){
            thread=new Thread(this);
            thread.start();
        }else if(run==false){
            thread=new Thread(this);
            thread.start();
        }
    }

    public void setImage(String image){
        if(image.equals("def")){
            image="1";
        }
        if(online){
            ImageIcon img = new ImageIcon("/Users/pingguo/Documents/ProjectDemo/coder"+image+".jpg");
            img.setImage(img.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
            label_image.setIcon(img);
        }else{
            ImageIcon img = new ImageIcon("/Users/pingguo/Documents/ProjectDemo/coder"+image+".jpg");
            img.setImage(img.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
            label_image.setIcon(img);
        }

        this.image=image;
    }

    public void setNickName(String nickName){
        label_nickName.setText(nickName);
        this.nickName=nickName;
    }

    public void setDescription(String description){
        label_des.setText(description);
        this.description=description;
    }


    int xx=0;
    int yy=0;
    private boolean online=false;



    public void setOnline(boolean online){
        this.online=online;
        if(online){
            ImageIcon img = new ImageIcon("/Users/pingguo/Documents/ProjectDemo/coder"+image+".jpg");
            img.setImage(img.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
            label_image.setIcon(img);
        }else{
            ImageIcon img = new ImageIcon("/Users/pingguo/Documents/ProjectDemo/coder"+image+".jpg");
            img.setImage(img.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
            label_image.setIcon(img);
        }
    }
    public int compareTo(FaceJPanel o){
        if(o.online){
            return 1;
        }else if(this.online){
            return -1;
        }else {
            return 0;
        }
    }

    // 双击打开聊天窗口，只有在线的用户可以打开，不在线的不能打开窗口
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() == 2){
            if(online){
                // 终止线程
                run=false;
                Config.showChatFrame(uid,nickName,image,description,msgs);
            }

        }
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {
        xx=this.getX();
        yy=this.getY();
        this.setLocation(xx-1,yy-1);
    }

    public void mouseExited(MouseEvent e) {
        this.setLocation(xx,yy);
    }
}
