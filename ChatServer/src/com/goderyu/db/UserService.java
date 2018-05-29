package com.goderyu.db;




import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
* @author 于好贤
* @version 创建时间：2017年11月8日 下午11:15:27
* @description 
*/
public class UserService {
	/**
	 * 使用用户名进行相应的登录
	 * @param username
	 * @param password
	 * @return
	 * @throws UsernameNotFoundException 用户不存在
	 * @throws PasswordException 密码错误
	 * @throws StateException  账户被停用
	 * @throws SQLException 数据库连接失败
	 */


	// 登录验证方法
	public String login(String username, String password)
			throws UsernameNotFoundException, PasswordException, StateException, SQLException {
		Connection conn = null;
		try {
			conn = DBManager.getConnection();
			PreparedStatement pst = conn.prepareStatement("select * from users where username=?");
			pst.setString(1, username);
			ResultSet rs = pst.executeQuery();
			// 询问是否存在用户名
			if (rs.next()) {
				// 询问用户是否被停用
				if (rs.getInt("state") == 0) {
					// 询问密码是否相同
					if (rs.getString("password").equals(password)) {
						return rs.getString(1);
					} else {
						throw new PasswordException();
					}
				} else {
					throw new StateException();
				}

			} else {
				throw new UsernameNotFoundException();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			// 关闭数据库连接
			conn.close();
		}
	}

	/**
	 * 获得自己的好友列表信息
	 * @param uid  自己的编号
	 * @return  好友列表信息
	 */
	public Vector<UserInfo> getFriendsList(String uid)throws SQLException{
		Connection conn = null;
		try {
			conn = DBManager.getConnection();
			PreparedStatement pst = conn.prepareStatement("select u.uid,u.img,u.nickname,u.description " +
					"from DB_Chat.friends f inner join DB_Chat.users u on u.uid = f.fuid and f.uid =?");
			pst.setString(1, uid);
			ResultSet rs = pst.executeQuery();
			Vector<UserInfo> vector=new Vector();
			while(rs.next()){
				UserInfo userInfo=new UserInfo();
				userInfo.setUid(rs.getString(1));
				userInfo.setImg(rs.getString(2));
				userInfo.setNickname(rs.getString(3));
				userInfo.setDescription(rs.getString(4));
				vector.add(userInfo);
			}
			return vector;
		} catch (SQLException e) {
			throw e;
		} finally {
			// 关闭数据库连接
			conn.close();
		}
	}

	/**
	 * 个人资料查询
	 * 好友资料查询
	 * @param uid
	 * @return 返回信息
	 * @throws SQLException
	 */
	public UserInfo2 getUserInfo(String uid)throws SQLException{
		Connection conn = null;
		try {
			conn = DBManager.getConnection();
			PreparedStatement pst = conn.prepareStatement("select * from users where uid=?");
			pst.setString(1, uid);
			ResultSet rs=pst.executeQuery();
			UserInfo2 userInfo2=new UserInfo2();
			if(rs.next()){
				userInfo2.setUid(rs.getString("uid"));
				userInfo2.setUsername(rs.getString("username"));
				userInfo2.setEmail(rs.getString("email"));
				userInfo2.setPhone(rs.getString("phone"));
				userInfo2.setNickname(rs.getString("nickname"));
				userInfo2.setDescription(rs.getString("description"));
				userInfo2.setName(rs.getString("name"));
				userInfo2.setImg(rs.getString("img"));
				userInfo2.setRemarks(rs.getString("remarks"));
				userInfo2.setSex(rs.getString("sex"));
				userInfo2.setYy(rs.getInt("yy"));
				userInfo2.setMm(rs.getInt("mm"));
				userInfo2.setDd(rs.getInt("dd"));
			}
			return userInfo2;


		} catch (SQLException e) {
			throw e;
		} finally {
			// 关闭数据库连接
			conn.close();
		}
	}
	// 注册用户
	public void regUser(String username,String password,String sex,String email,String phone)throws UsernameException,SQLException{
		Connection conn = null;
		try {
			conn = DBManager.getConnection();
			PreparedStatement pst = conn.prepareStatement("select * from users where username=?");
			pst.setString(1, username);
			ResultSet rs= pst.executeQuery();
			if(rs.next()){
				throw new UsernameException();
			}
			pst=conn.prepareStatement("insert into users(uid,username,password,sex,email,phone,createtime) values(?,?,?,?,?,?,SYSDATE())");
			pst.setString(1, System.currentTimeMillis()+"R"+(int)(Math.random()*10000));
			pst.setString(2, username);
			pst.setString(3, password);
			pst.setString(4, sex);
			pst.setString(5, email);
			pst.setString(6, phone);
			if(pst.executeUpdate()<=0){
				throw new SQLException();
			}



		} catch (SQLException e) {
			throw e;
		} finally {
			// 关闭数据库连接
			conn.close();
		}


	}
	

}
