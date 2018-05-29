package com.goderyu.view;

import com.goderyu.service.NetService;
import com.goderyu.util.Config;
import net.sf.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.net.UnknownHostException;

public class SignInFrame extends JFrame {
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                SignInFrame frame = new SignInFrame();
                frame.setResizable(false);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    private SignInFrame() {
        this.setTitle(" ©️ 2017 于好贤");
        setType(Type.UTILITY);
        setBackground(Color.WHITE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(248, 361);
        // 窗口居中
        setLocationRelativeTo(null);
        JPanel contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JTextField nameField = new JTextField();
        nameField.setToolTipText("");
        nameField.setForeground(Color.BLACK);
        nameField.setBounds(24, 174, 207, 35);
        contentPane.add(nameField);
        nameField.setColumns(10);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setToolTipText("");
        passwordField.setForeground(Color.BLACK);
        passwordField.setBounds(24, 233, 207, 35);
        contentPane.add(passwordField);

        JLabel userLabel = new JLabel("用户名");
        userLabel.setBounds(103, 157, 61, 16);
        contentPane.add(userLabel);

        JLabel passwordLabel = new JLabel("密   码");
        passwordLabel.setBounds(103, 216, 61, 16);
        contentPane.add(passwordLabel);

        JButton signInButton = new JButton("登录");
        signInButton.addActionListener(e -> {
            // 收集用户名和密码
            String username_str=nameField.getText().trim();
            String password_str = new String(passwordField.getPassword());
            if (username_str.trim().equals("") || password_str.trim().equals("")) {
                JOptionPane.showMessageDialog(null, "用户名和密码不能为空！", "通知", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Config.username=username_str;
            Config.password=password_str;
            try {
                JSONObject json=NetService.getNetService().login();
                if(json.getInt("state")==0){
                    // 登录成功后显示好友列表
                    new FriendsList().setVisible(true);
                    // 隐藏当前窗口并销毁释放资源
                    setVisible(false);
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(null, json.getString("msg"), "通知", JOptionPane.PLAIN_MESSAGE);
                }

            }catch (UnknownHostException e1){
                JOptionPane.showMessageDialog(null, "网络连接失败！", "通知", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null, "网络连接失败！", "通知", JOptionPane.ERROR_MESSAGE);
            }
        });
        signInButton.setBounds(24, 285, 69, 29);
        contentPane.add(signInButton);

        JButton signUpButton = new JButton("注册");
        signUpButton.addActionListener(e -> new SignUpFrame().setVisible(true));
        signUpButton.setBounds(87, 285, 69, 29);
        contentPane.add(signUpButton);

        JButton forgotPassButton = new JButton("忘记密码");
        forgotPassButton.setBounds(150, 285, 81, 29);
        contentPane.add(forgotPassButton);

        JLabel imageLabel = new JLabel(new ImageIcon("jj.jpg"));
        imageLabel.setBounds(65, 20, 115, 115);
        contentPane.add(imageLabel);

        // 设置窗口大小不可变
        setResizable(false);
        setVisible(true);
    }
}
