package com.goderyu.view;

import com.goderyu.service.DBManager;

import java.sql.*;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class FindUserFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTable table;
	// 声明默认表格模式
	private DefaultTableModel model;

	/**
	 * Create the frame.
	 */
	public FindUserFrame() {
		setTitle("查找好友");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(447, 357);
        // 窗口居中
        setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("昵称：");
		label.setBounds(16, 23, 61, 16);
		contentPane.add(label);
		
		textField = new JTextField();
		textField.setBounds(70, 13, 283, 36);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton button = new JButton("查找");
		button.setBounds(354, 15, 75, 34);
		button.addActionListener(e -> {
            Connection conn = null;
            try {
                conn = DBManager.getConnection();
                PreparedStatement pst = conn.prepareStatement("select nickname as 昵称,sex as 性别 from users where nickname like ?");
                pst.setString(1, textField.getText().trim());
                ResultSet rs=pst.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                // 获取列数
                int colCount = rsmd.getColumnCount();
                // 存放列名
                Vector<String> title = new Vector<>();
                // 列名
                for (int i = 1; i <= colCount; i++) {
                    title.add(rsmd.getColumnLabel(i));
                }
                // 表格数据
                Vector<Vector<String>> data = new Vector<>();
                int rowCount = 0;
                while (rs.next()) {
                    rowCount++;
                    // 行数据
                    Vector<String> rowdata = new Vector<>();
                    for (int i = 1; i <= colCount; i++) {
                        rowdata.add(rs.getString(i));
                    }
                    data.add(rowdata);
                }
                if (rowCount == 0) {
                    model.setDataVector(null, title);
                } else {
                    model.setDataVector(data, title);
                }



            } catch (SQLException ee) {
                try {
                    throw ee;
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } finally {
                // 关闭数据库连接
                try {
                    conn.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
		contentPane.add(button);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(16, 62, 413, 247);
		contentPane.add(scrollPane);

		model = new DefaultTableModel();
		table = new JTable(model);
		scrollPane.setViewportView(table);

		setVisible(true);
	}
}
