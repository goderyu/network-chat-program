package com.goderyu.view;


import java.awt.Image;


import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;



public class PersonalData extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel label;
	private JLabel label_1;
	private JTextField textField_2;


	/**
	 * Create the frame.
	 */
	public PersonalData(String img,String nickname,String info) {
		setResizable(false);
		setSize( 474, 404);
		// 窗口居中
		setLocationRelativeTo(null);
		setTitle("个人资料");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ImageIcon image = new ImageIcon("/Users/pingguo/Documents/ProjectDemo/coder"+ img +".jpg");
		image.setImage(image.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
		JLabel lblNewLabel = new JLabel(image);
		lblNewLabel.setBounds(8, 20, 68, 68);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(88, 21, 380, 31);
		textField.setText(nickname);
		contentPane.add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setBounds(88, 62, 380, 26);
		textField_1.setText(info);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBounds(18, 100, 437, 263);
		// 设置面板的边界格式，并加入标题
		panel.setBorder(BorderFactory.createTitledBorder("个人资料"));
		contentPane.add(panel);
		panel.setLayout(null);
		
		label = new JLabel("真实姓名：");
		label.setBounds(30, 38, 65, 16);
		panel.add(label);
		
		label_1 = new JLabel("性别：");
		label_1.setBounds(248, 38, 61, 16);
		panel.add(label_1);
		
		JRadioButton radioButton = new JRadioButton("男");
		radioButton.setBounds(290, 34, 50, 23);
		panel.add(radioButton);
		
		JRadioButton radioButton_1 = new JRadioButton("女");
		radioButton_1.setBounds(338, 34, 50, 23);
		panel.add(radioButton_1);
		
		textField_2 = new JTextField();
		textField_2.setBounds(94, 33, 130, 26);
		panel.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel label_2 = new JLabel("出生年月：");
		label_2.setBounds(30, 82, 65, 16);
		panel.add(label_2);


		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(94, 78, 73, 27);

		panel.add(comboBox);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(179, 78, 62, 27);
		panel.add(comboBox_1);
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setBounds(253, 78, 62, 27);
		panel.add(comboBox_2);
		
		JLabel label_3 = new JLabel("年");
		label_3.setBounds(163, 82, 18, 16);
		panel.add(label_3);
		
		JLabel label_4 = new JLabel("月");
		label_4.setBounds(237, 82, 18, 16);
		panel.add(label_4);
		
		JLabel label_5 = new JLabel("日");
		label_5.setBounds(312, 82, 18, 16);
		panel.add(label_5);
		
		JLabel label_6 = new JLabel("备　　注：");
		label_6.setBounds(30, 130, 65, 16);

		panel.add(label_6);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(94, 130, 294, 113);
		panel.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		setVisible(true);
	}
}
