package com.goderyu.view;

import com.goderyu.util.Config;
import net.sf.json.JSONObject;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Date;
import java.util.Vector;

public class ChatFrame extends JFrame implements WindowListener{


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea textArea = new JTextArea();
	private JTextArea textArea_1 = new JTextArea();
	private JLabel img = new JLabel();
	private JLabel nicknameLabel = new JLabel();
	private JLabel infoLabel = new JLabel();
	private String uidStr;
	private String nicknameStr;
	private String imgStr;
	private String infoStr;
	private ImageIcon image = null;

	// 添加自己的消息
	public void addMyMessage(Msg msg){
		String str = "\n" +JSONObject.fromObject(Config.personal_json_data).getString("nickname")+"\t"
				+new Date().toLocaleString() + "\n" + msg.getMsg() + "\n";
		textArea_1.setText(textArea_1.getText() + str);
		textArea_1.setSelectionStart(textArea_1.getText().toString().length());
		textArea_1.setSelectionEnd(textArea_1.getText().toString().length());
		textArea.requestFocus();
	}
	// 添加别人的消息
	public void addMessage(Msg msg){
		String str = "\n" + nicknameStr +"\t" + new Date().toLocaleString() + "\n" + msg.getMsg() + "\n";
		textArea_1.setText(textArea_1.getText() + str);
		textArea_1.setSelectionStart(textArea_1.getText().toString().length());
		textArea_1.setSelectionEnd(textArea_1.getText().toString().length());
		textArea.requestFocus();

	}

	public ChatFrame(String uidStr, String nicknameStr, String imgStr, String infoStr, Vector<Msg> msgs) {
		this.uidStr=uidStr;
		this.nicknameStr=nicknameStr;
		this.imgStr=imgStr;
		this.infoStr=infoStr;

		infoLabel.setText(infoStr);
		nicknameLabel.setText(nicknameStr);
		image = new ImageIcon("/Users/pingguo/Documents/ProjectDemo/coder"+ imgStr +".jpg");
		image.setImage(image.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
		img.setIcon(image);

		setTitle("和"+nicknameStr+"的聊天窗口");
		setBackground(Color.WHITE);
		setBounds(100, 100, 591, 491);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));


		panel.add(img, BorderLayout.WEST);
		img.setBounds(0, 0, 50, 50);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		nicknameLabel.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		panel_1.add(nicknameLabel, BorderLayout.CENTER);

		panel_1.add(infoLabel, BorderLayout.SOUTH);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setBackground(Color.WHITE);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				splitPane.setDividerLocation(0.55);
			}
		});
		contentPane.add(splitPane, BorderLayout.CENTER);

		JPanel panel_2 = new JPanel();
		splitPane.setRightComponent(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.WHITE);
		FlowLayout flowLayout_1 = (FlowLayout) panel_3.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_2.add(panel_3, BorderLayout.NORTH);

		JButton btnNewButton_2 = new JButton("字体");
		panel_3.add(btnNewButton_2);

		JButton button = new JButton("窗口抖动");
		panel_3.add(button);

		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.WHITE);
		FlowLayout flowLayout = (FlowLayout) panel_4.getLayout();
		flowLayout.setVgap(3);
		flowLayout.setHgap(3);
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panel_2.add(panel_4, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("关闭");
		panel_4.add(btnNewButton);


		JScrollPane scrollPane = new JScrollPane();
		panel_2.add(scrollPane, BorderLayout.CENTER);


		scrollPane.setViewportView(textArea);

		JButton btnNewButton_1 = new JButton("发送");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				{"type":"msg","toUID":"","myUID":"","msg":"","code":""}
				try{

					Msg msg = new Msg();
					msg.setCode(System.currentTimeMillis()+"");
					msg.setMyUID(JSONObject.fromObject(Config.personal_json_data).getString("uid"));
					msg.setToUID(uidStr);
					msg.setType("msg");
					msg.setMsg(textArea.getText());


					String json=JSONObject.fromObject(msg).toString();
					byte[] bytes = json.getBytes();
					DatagramPacket datagramPacket=new DatagramPacket(bytes,bytes.length,
							InetAddress.getByName(Config.IP),28886);
					Config.datagramSocket_client.send(datagramPacket);
					textArea.setText("");

					addMyMessage(msg);
				} catch(Exception e1){
					e1.printStackTrace();
				}

			}
		});
		panel_4.add(btnNewButton_1);

		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setLeftComponent(scrollPane_1);

		scrollPane_1.setViewportView(textArea_1);

		addWindowListener(this);
		setVisible(true);

		for (Msg msg: msgs){
			addMessage(msg);
		}
		msgs.clear();
	}

	public void windowOpened(WindowEvent e) {

	}

	public void windowClosing(WindowEvent e) {
		Config.closeChatFrame(uidStr);
		this.dispose();
	}

	public void windowClosed(WindowEvent e) {

	}

	public void windowIconified(WindowEvent e) {

	}

	public void windowDeiconified(WindowEvent e) {

	}

	public void windowActivated(WindowEvent e) {

	}

	public void windowDeactivated(WindowEvent e) {

	}
}
