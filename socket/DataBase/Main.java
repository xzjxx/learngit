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
		Class.forName(driverName);//加载驱动程序
		Connection dbCon=DriverManager.getConnection(dbURL,userName,root);
		System.out.println("连接数据库成功");
		
		//创建statement类对象，用来执行sql语句
		Statement statement=dbCon.createStatement();
		//要执行的select语句
		String sql="select* from UserTable";
		//ResultSet类用来存放获取的结果集
		ResultSet rs=statement.executeQuery(sql);
		System.out.println("结果：");
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
		//预处理添加数据，其中有两个参数
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