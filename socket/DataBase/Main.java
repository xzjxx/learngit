package DataBase;
import java.sql.*;

import Common.User;
public class Main {
	/*public static void main(String[] args) {
		User u=new User();
		u.setUserID("123456789");
		u.setPasswd("654123");
		Condb(u);
	}*/
	public static boolean Condb(User u) {
	  
	   boolean b=false;
	   String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	   String dbURL="jdbc:sqlserver://localhost:1433;DatabaseName=sqltestdb";
	   String userName="sa";
	   String root="654123";
	   try {
		Class.forName(driverName);//������������
		Connection dbCon=DriverManager.getConnection(dbURL,userName,root);
		System.out.println("�������ݿ�ɹ�");
		
		//����statement���������ִ��sql���
		Statement statement=dbCon.createStatement();
		//Ҫִ�е�select���
		String sql="select* from UserTable";
		//ResultSet��������Ż�ȡ�Ľ����
		ResultSet rs=statement.executeQuery(sql);
		System.out.println("�����");
		dbCon.createStatement(rs.TYPE_SCROLL_INSENSITIVE,rs.CONCUR_READ_ONLY);
		
		String userid=null,password=null;
		
		while(rs.next())
		{
			userid=rs.getString("userid");
			password=rs.getString("password");
			if(userid.equals(u.userid))
			{
				if(password.equals(u.passwd))
					b=true;
				System.out.println(userid+"\t"+password);
			}
		}
		PreparedStatement psql;
		ResultSet res;
		//Ԥ����������ݣ���������������
		psql=dbCon.prepareStatement("insert into UserTable(userid,password)"
				+"values(?,?)");
		psql.setString(1, "123456789");
		psql.setString(2, "654123");
		psql.executeUpdate();
	   }catch(Exception e) {
		   e.printStackTrace();
	   }finally {
		   return b;
	   }
	}
}