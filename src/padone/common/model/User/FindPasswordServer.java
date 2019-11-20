package padone.common.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.*;
/*import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;*/

import javax.sql.DataSource;

public class FindPasswordServer {
	/*public boolean passwordFinder(DataSource datasource,String account,String identity) throws AddressException, MessagingException {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			String sql = "select * from " + identity + " where account = '" + account + "'";
			String mail = "";
			String p = "";
			System.out.println(sql);
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				mail = rs.getString("mail");
				p = rs.getString("password");
			}
			st.close();// 關閉st
			String host = "smtp.gmail.com";
			int port = 587;
			String from = "bbdp20@gmail.com";
			String to = mail;
			final String username = "bbdp20@gmail.com";
			final String password = "bbdp20bbdp";// your password

			// Get system properties
			Properties props = System.getProperties();

			// Setup mail server
			props.put("mail.smtp.host", host);
			// props.put("mail.smtp.auth", "true");
			// props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.port", port);
			Session session = Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			// Get session

			// Define message
			MimeMessage message = new MimeMessage(session);
			
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("this is your password");
			message.setText(p);

			// Send message
			Transport transport = session.getTransport("smtp");
			transport.connect(host, port, username, password);
			transport.send(message);
			return true;
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception ignore) {
					return false;
				}
		}
		
	}*/
	public String passwordFinder(DataSource datasource,String account,String mail,String identity)  {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			String sql = "select * from " + identity + " where account = '" + account + "' and mail='"+mail+"'";
			String p = "";
			System.out.println(sql);
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				p = rs.getString("password");
			}
			st.close();// 關閉st
			
			return p;
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
			
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception ignore) {
					
				}
		}
		return null;
		
	}
	
}
