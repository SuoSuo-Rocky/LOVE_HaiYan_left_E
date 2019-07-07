package aloneExampleDemo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.swing.JOptionPane;

public class text {
	public static void main(String[] args) {
		DBImple shi = new DBImple();
		Properties properties = new Properties();
		URL url = text.class.getResource("properties");
		try {
			properties.load(new FileInputStream(url.getFile()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String userid = properties.getProperty("userid");
		new text().findByid(shi, userid);

	}

	public void findByid(Dao dao, String userid) {
		Login login = dao.findByid(userid);
		if (login == null) {
			JOptionPane.showMessageDialog(null, "根据姓名 所获取的密码为 ：" + login.getPassword());
		} else {
			JOptionPane.showMessageDialog(null, "Sorry! 查无此人！");
		}
	}

}
