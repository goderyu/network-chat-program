package com.goderyu.view;

import com.goderyu.util.Config;
import net.sf.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class SignUpFrame extends JFrame {
    private JTextField nameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPassField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<String> sexBox;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                SignUpFrame frame = new SignUpFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    SignUpFrame() {
        // 设置窗口大小不可变
        setResizable(false);
        setTitle("欢迎来到注册界面");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(450, 300);
        // 窗口居中
        setLocationRelativeTo(null);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(0, 2, 0, 0));

        JLabel nameLabel = new JLabel("用户名：");
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(nameLabel);

        nameField = new JTextField();
        contentPane.add(nameField);
        nameField.setColumns(10);

        JLabel passwordLabel = new JLabel("密码：");
        passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(passwordLabel);

        passwordField = new JPasswordField();
        contentPane.add(passwordField);
        passwordField.setColumns(10);

        JLabel confirmPassLabel = new JLabel("确认密码：");
        confirmPassLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(confirmPassLabel);

        confirmPassField = new JPasswordField();
        contentPane.add(confirmPassField);
        confirmPassField.setColumns(10);

        JLabel sexLabel = new JLabel("性别：");
        sexLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(sexLabel);

        sexBox = new JComboBox<>(new String[] {"男","女"});
        contentPane.add(sexBox);

        JLabel emailLabel = new JLabel("电子邮箱：");
        emailLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(emailLabel);

        emailField = new JTextField();
        contentPane.add(emailField);
        emailField.setColumns(10);

        JLabel phoneLabel = new JLabel("手机号码：");
        phoneLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(phoneLabel);

        phoneField = new JTextField();
        contentPane.add(phoneField);
        phoneField.setColumns(10);

        JButton registerButton = new JButton("注册");
        // 为注册按钮添加监听事件

        registerButton.addActionListener(this::actionPerformed);
        contentPane.add(registerButton);

        JButton resetButton = new JButton("重置");

        // 为重置按钮添加监听事件
        resetButton.addActionListener(e -> {
            //清空所有信息
            nameField.setText("");
            passwordField.setText("");
            confirmPassField.setText("");
            sexBox.setSelectedIndex(0);
            emailField.setText("");
            phoneField.setText("");
        });
        contentPane.add(resetButton);
    }

    private void actionPerformed(ActionEvent e) {
        String userName = nameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String rePassword = new String(confirmPassField.getPassword());
        String sex = (String)sexBox.getSelectedItem();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        // 正则表达式验证注册信息格式
        String nameRule = "^[A-Za-z][A-Za-z1-9_-]+$";
        String passwordRule = "^[A-Za-z0-9~!@#$%^&*()_+{}<>?]{6,16}$";
        String emailRule = "[a-zA-Z_]{0,}[0-9]{2,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}$";
        String phoneRule = "^1[0-9]{10}$";
        if (!userName.matches(nameRule)) {
            // 判断用户名是否为空
            if ("".equals(userName)) {
                JOptionPane.showMessageDialog(this,"用户名不能为空!",
                        "警告", JOptionPane.QUESTION_MESSAGE);
            }else
                JOptionPane.showMessageDialog(this,"用户名必须以字母开头且只能使用数字或字母或下划线",
                        "错误", JOptionPane.ERROR_MESSAGE);
        }
        else if (!password.matches(passwordRule)) {
            // 判断密码是否为空
            if ("".equals(password)) {
                JOptionPane.showMessageDialog(this, "密码不能为空!",
                        "错误", JOptionPane.ERROR_MESSAGE);
            }else
                JOptionPane.showMessageDialog(this, "密码最少6位最多16位且只能用数字、字母和常规符号",
                        "错误", JOptionPane.ERROR_MESSAGE);
        }
        else if (!email.matches(emailRule)) {
            // 判断邮箱是否为空
            if ("".equals(email)) {
                JOptionPane.showMessageDialog(this, "邮箱不能为空!",
                        "错误", JOptionPane.ERROR_MESSAGE);
            }else
                JOptionPane.showMessageDialog(this, "请输入正确的邮箱格式：xxxxxx@xxx.xxx",
                        "错误", JOptionPane.ERROR_MESSAGE);
        }
        else if (!phone.matches(phoneRule)) {
            // 判断手机号是否为空
            if ("".equals(phone)) {
                JOptionPane.showMessageDialog(this, "手机号不能为空",
                        "错误", JOptionPane.ERROR_MESSAGE);
            }else
                JOptionPane.showMessageDialog(this, "请输入正确的十一位有效手机号码!",
                        "错误", JOptionPane.ERROR_MESSAGE);
        }
        // 判断两次输入密码是否一致
        else if (!rePassword.equals(password)) {
            JOptionPane.showMessageDialog(this, "两次密码不一致!请注意格式!",
                    "错误", JOptionPane.ERROR_MESSAGE);
        } else {
            try{
                Socket socket=new Socket(Config.IP,Config.REG_PORT);
                InputStream input=socket.getInputStream();
                OutputStream output=socket.getOutputStream();

                output.write(("{\"username\":\"" + userName + "\",\"password\":\"" + password + "\",\"sex\":\"" + sex + "\",\"email\":\"" + email + "\",\"phone\":\"" + phone + "\"}").getBytes());
                output.flush();
                byte[] bytes=new byte[1024];
                int len=input.read(bytes);
                String str=new String(bytes,0,len);
                JSONObject json=JSONObject.fromObject(str);
                if(json.getInt("state")==0){
                    JOptionPane.showMessageDialog(this, "恭喜您注册成功啦!\n用户名："+userName+"\n密码："+password+"\n电子邮箱："+email+"\n手机号码："+phone+"\n请记牢您的个人信息哦~",
                            "亲爱的小伙伴：", JOptionPane.PLAIN_MESSAGE);
                    // 注册成功，清空文本
                    nameField.setText("");
                    passwordField.setText("");
                    confirmPassField.setText("");
                    sexBox.setSelectedIndex(0);
                    emailField.setText("");
                    phoneField.setText("");
                    // 隐藏窗口
                    setVisible(false);
                    // 释放资源
                    dispose();
                }else if(json.getInt("state")==1){
                    JOptionPane.showMessageDialog(this, "用户名已存在,换一个试试吧",
                            "温馨提示", JOptionPane.ERROR_MESSAGE);
                }else if(json.getInt("state")==2){
                    JOptionPane.showMessageDialog(this, "发生了未知错误呢,重试一下吧,于狗蛋会努力修复错误哦...",
                            "温馨提示", JOptionPane.ERROR_MESSAGE);
                }

                input.close();
                output.close();
                socket.close();
            }catch(Exception e1){
                e1.printStackTrace();
            }
//            // 创建DBUtil对象
//            DBUtil db = new DBUtil();
//            try {
//                // 连接数据库
//                db.getConnection();
//                // ִ执行添加
//                int count = db.executeUpdate(db.getInsertSql(), new String[] {userName,password,sex,email,phone});
//                if (count == 1) {
//                    JOptionPane.showMessageDialog(this, "注册成功啦!",
//                            "恭喜你!", JOptionPane.PLAIN_MESSAGE);
//                    // 如果登录成功,隐藏该窗口
//                    setVisible(false);
//                    //本窗口销毁,释放内存资源
//                    dispose();
//                }
//                else {
//                    JOptionPane.showMessageDialog(this, "注册信息写入数据库失败!",
//                            "错误", JOptionPane.ERROR_MESSAGE);
//                }
//            } catch (Exception ce) {
//                // 关闭连接
//                db.closeAll();
//                ce.printStackTrace();
//            }

        }
    }
}
