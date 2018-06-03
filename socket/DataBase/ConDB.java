package DataBase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Common.User;

public class ConDB {
	private String driverName; //������
    private static String dbURL; //�������ݿ��URL��ַ
    private static String userName; //���ݿ���û���
    private static String root; //���ݿ������
    private Connection con; //���ݿ����Ӷ���
    private PreparedStatement pstm; //���ݿ�Ԥ���봦�����
    public ConDB(){
    	driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
		dbURL="jdbc:sqlserver://localhost:1433;DatabaseName=sqltestdb";
		userName="sa";
		root="654123";
        try{
            Class.forName(driverName);
        }catch(ClassNotFoundException e){
            System.out.println("�������ݿ���������ʧ�ܣ�");
            e.printStackTrace();
        }
    }
    public void getCon(){
        try {
        	 con=DriverManager.getConnection(dbURL,userName,root);
        } catch (SQLException e) {
            System.out.println("��ȡ���ݿ�����ʧ�ܣ�");
            e.printStackTrace();
        }
        if(con!=null)
        	System.out.println("�������ݿ�ɹ�!");
    }
    //�������顣�磺String[] obj = new String[]{"�����","201321173083"};
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
                System.out.println("����DB����doPstm����ʱ����");
                e.printStackTrace();
            }
        }
    }
    
    public ResultSet getRs(){
        try {            
            return pstm.getResultSet();
        } catch (SQLException e) {
            System.out.println("DB���е�getRs()��������");
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
    //����û��Ƿ���ں������Ƿ���ȷ
    //0���û������ڣ�1��������ȷ��2���������
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
				//���������û����Ƿ�����Լ������Ƿ���ȷ
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}finally {
			//closed();
			return b;
		}	
    }
    //��ע������û����ӵ����ݿ�
    public void InsertUser(User u)
    {
    	System.out.println("�����û�");
		Object []params={u.userid,u.passwd,u.online};
		String sql="insert into UserTable values(?,?,?)";
    	doPstm(sql, params);
    	ResultSet rs=getRs();
    }
    //���������˻�
    public ResultSet AllUser() {
    	String userid=null,password=null;
		Object []params=null;
		String sql="select * from UserTable ";
    	doPstm(sql, params);
    	ResultSet rs=getRs();
    	return rs;
    }
    //�޸��û�����״̬
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
            System.out.println("�ر�pstm����ʧ�ܣ�");
        }
        try{
            if(con!=null)
                con.close();
        }catch(Exception e){
            System.out.println("�ر�con����ʧ�ܣ�");
        }
    }
    

}

