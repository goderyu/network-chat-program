package com.goderyu.view;

import com.goderyu.util.Config;
import net.sf.json.JSONObject;

import java.awt.BorderLayout;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;

/**
 * 好友信息列表
 * @author 于好贤
 */

public class FriendsList extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel myFace = new JLabel();
	private JLabel myNickname = new JLabel();
	private JLabel myInfo = new JLabel();
	private ImageIcon image = null;
	// 更新自己的个人信息
	public void update(){
		JSONObject jsonObject=JSONObject.fromObject(Config.personal_json_data);
		this.setTitle(" ©️ 2017 于好贤");
		myNickname.setText(jsonObject.getString("nickname"));
		myInfo.setText(jsonObject.getString("description"));
		image = new ImageIcon("/Users/pingguo/Documents/ProjectDemo/coder"+ jsonObject.getString("img") +".jpg");
		image.setImage(image.getImage().getScaledInstance(70,70, Image.SCALE_DEFAULT));
		myFace.setIcon(image);
	}

	public FriendsList() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(1100, 100, 235, 695);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel myInfoPanel = new JPanel();
		contentPane.add(myInfoPanel, BorderLayout.NORTH);
		myInfoPanel.setLayout(new BorderLayout(10, 10));
		myInfoPanel.add(myFace, BorderLayout.WEST);
		
		JPanel panel = new JPanel();
		myInfoPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		myNickname.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		panel.add(myNickname, BorderLayout.CENTER);
		panel.add(myInfo, BorderLayout.SOUTH);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JButton button = new JButton("退出");
		panel_1.add(button, BorderLayout.EAST);
		button.addActionListener(e -> System.exit(0));
		
		JButton btnNewButton = new JButton("设置");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JSONObject jsonObject=JSONObject.fromObject(Config.personal_json_data);
				new PersonalData(jsonObject.getString("img"),
						jsonObject.getString("nickname"),
						jsonObject.getString("description"));

			}
		});
		panel_1.add(btnNewButton, BorderLayout.WEST);
		
		JButton button_1 = new JButton("查找");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FindUserFrame();
			}
		});
		panel_1.add(button_1, BorderLayout.CENTER);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("好友列表", null, panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_2.add(scrollPane,BorderLayout.CENTER);
		scrollPane.getViewport().add(new FriendsListJPanel());
		update();

		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("群　　组", null, panel_3, null);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
	}

}
