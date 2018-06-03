package DataBase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Common.User;

public class ConDB {
	private String driverName; //驱动名
    private static String dbURL; //连接数据库的URL地址
    private static String userName; //数据库的用户名
    private static String root; //数据库的密码
    private Connection con; //数据库连接对象
    private PreparedStatement pstm; //数据库预编译处理对象
    public ConDB(){
    	driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
		dbURL="jdbc:sqlserver://localhost:1433;DatabaseName=sqltestdb";
		userName="sa";
		root="654123";
        try{
            Class.forName(driverName);
        }catch(ClassNotFoundException e){
            System.out.println("加载数据库驱动程序失败！");
            e.printStackTrace();
        }
    }
    public void getCon(){
        try {
        	 con=DriverManager.getConnection(dbURL,userName,root);
        } catch (SQLException e) {
            System.out.println("获取数据库连接失败！");
            e.printStackTrace();
        }
        if(con!=null)
        	System.out.println("连接数据库成功!");
    }
    //对象数组。如：String[] obj = new String[]{"宾桀锋","201321173083"};
    public void doPstm(String sql,Object[] params){
        if(sql!=null && !sql.equals(""))
        {
            if(con==null)
                getCon();
            try {
                pstm=con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                if(params==null){
                    params=new Object[0];
                }
                for(int i=0;i<params.length;i++){
                    pstm.setObject(i+1,params[i]);
                }
                pstm.execute();
            } catch (SQLException e) {
                System.out.println("调用DB类中doPstm方法时出错！");
                e.printStackTrace();
            }
        }
    }
    
    public ResultSet getRs(){
        try {            
            return pstm.getResultSet();
        } catch (SQLException e) {
            System.out.println("DB类中的getRs()方法出错！");
            e.printStackTrace();
            return null;
        }
    }    
    
    public int getUpdate(){
        try {
            return pstm.getUpdateCount();
        } catch (SQLException e) {            
            e.printStackTrace();
            return -1;
        }
    }
    //检查用户是否存在和密码是否正确
    //0：用户不存在；1：密码正确；2：密码错误
    public int CheckUser(User u) {
    	int b=0;
    	String userid=null,password=null;
		Object []params=null;
		String sql="select * from UserTable ";
    	doPstm(sql, params);
    	ResultSet rs=getRs();
    	try {
			while(rs.next())
			{
				//检测输入的用户名是否存在以及密码是否正确
				userid=rs.getString("userid");
				password=rs.getString("password");
				if(userid.equals(u.userid))
				{
					b=2;
					if(password.equals(u.passwd))
						b=1;
				}
			}
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			//closed();
			return b;
		}	
    }
    //将注册的新用户增加到数据库
    public void InsertUser(User u)
    {
    	System.out.println("新增用户");
		Object []params={u.userid,u.passwd,u.online};
		String sql="insert into UserTable values(?,?,?)";
    	doPstm(sql, params);
    	ResultSet rs=getRs();
    }
    //返回所有账户
    public ResultSet AllUser() {
    	String userid=null,password=null;
		Object []params=null;
		String sql="select * from UserTable ";
    	doPstm(sql, params);
    	ResultSet rs=getRs();
    	return rs;
    }
    //修改用户在线状态
    public void updata(User u) {
    	String userid=null,password=null,online=null;
		Object []params= {u.userid,u.passwd,u.online,u.userid};
		String sql="update UserTable set userid=?,password=?,online=? where userid=?";
    	doPstm(sql, params);
		
    }
    public void closed(){
        try{
            if(pstm!=null)
                pstm.close();
        }catch(Exception e){
            System.out.println("关闭pstm对象失败！");
        }
        try{
            if(con!=null)
                con.close();
        }catch(Exception e){
            System.out.println("关闭con对象失败！");
        }
    }
    

}

