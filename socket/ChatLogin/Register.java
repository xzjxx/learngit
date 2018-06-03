package ChatLogin;
import javax.swing.JOptionPane;
import Common.User;
import DataBase.ConDB;
import DataBase.Main;
import javafx.event.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
public class Register
{
	Stage primaryStage;
	BorderPane pane;
	TextField rtf;//用户名输入框
	PasswordField rpf,rpf2;//密码输入框，第二次密码确认框
	Button rbt;//注册按钮
	Button rbt1;//取消按钮
	public Register(){
		//初始化所有成员
		primaryStage=new Stage();
		pane=new BorderPane();
		rtf=new TextField();
		rpf=new PasswordField();
		rpf2=new PasswordField();
		rbt=new Button("注册");
		rbt1=new Button("取消");

		//pane.setAlignment(Pos.CENTER);//设置窗体内容的位置为居中
		pane.setStyle("-fx-background-image:url(image/背景.jpg)");
		Scene scene= new Scene(Registerpane(),430,700);
		primaryStage.setTitle("欢迎注册");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);//窗体不能调整
		primaryStage.show();
	}


//注册界面
	private BorderPane Registerpane()
	{

		
		rtf.setPromptText("昵称");
		rpf.setPromptText("密码(不少于6位）");
		rpf2.setPromptText("重新输入密码");
		final Label label1=new Label();//提示用户名信息问题
		final Label label2=new Label();//提示密码信息问题
		final Label label3=new Label();//提示第二次密码是否与第一次一致
		
		VBox vbox=new VBox(15);
		vbox.setPadding(new Insets(100,100,100,100));//设置边距
		
		vbox.getChildren().addAll(new Label("用户名："),rtf,label1);
		vbox.getChildren().addAll(new Label("密码："),rpf,label2);
		vbox.getChildren().addAll(new Label("确认密码"),rpf2,label3);
		pane.setTop(vbox);
		
		HBox hbox=new HBox(15);
		hbox.setPadding(new Insets(15,15,15,15));
		hbox.setSpacing(10);
		
		Label rlabel=new Label("                  ");//为了设置按钮间距
		Label rlabel1=new Label("         ");//为了设置按钮间距
		//设置按钮大小
		rbt.setPrefSize(80, 20);
		rbt1.setPrefSize(80, 20);
		hbox.getChildren().addAll(rlabel,rbt,rlabel1,rbt1);
		pane.setCenter(hbox);
		System.out.println(rtf.getText());
		/*rpf.setOnAction(e->{ 
			System.out.println(rtf.getText());
			if(rtf.getText().equals(""))
			{
				label1.setText("用户名不可以为空");
				label1.setTextFill(Color.RED);
			}
		});*/
		
		//注册按钮驱动程序
		rbt.setOnAction(e->{
			User u=new User();
			u.setUserID(rtf.getText());//从输入框中获取用户名
			u.setPasswd(rpf.getText());//从输入框中获取密码
			u.setonline("no");//设置用户在线状态为“no”
			System.out.print("user"+u.userid);
			System.out.print("mima"+u.passwd);
			ConDB con=new ConDB();
			int flag=con.CheckUser(u);
			
			//用户名或者密码为空
			if(u.userid.equals("")||u.passwd.equals(""))
			{
				//用户名为空
				if(u.userid.equals(""))
				{
					label1.setText("用户名不可以为空");
					label1.setTextFill(Color.RED);
				}
				//密码为空
				if(u.passwd.equals(""))
				{
					label2.setText("密码不能为空");
					label1.setTextFill(Color.RED);
				}
			}
			if(flag!=0) 
			{
					label1.setText("用户名已存在");
			}	
			//密码少于6位或者两次密码输入不一致
			if(u.passwd.length()<6||!u.passwd.equals(rpf2.getText())){ 
				if(u.passwd.length()<6)
					label2.setText("密码不能少于6位");
				if(!u.passwd.equals(rpf2.getText()))
					label3.setText("密码不一致");
			}
			//所有验证通过
			if(flag==0&&u.passwd.length()>=6&&u.passwd.equals(rpf2.getText()))
			{
				con.InsertUser(u);//将用户信息插入数据库
				JOptionPane.showMessageDialog(null,"注册成功","消息",
						JOptionPane.ERROR_MESSAGE);//弹出注册成功窗口
				new Login();//启动登录界面
				primaryStage.close();//关闭注册界面
			}
		});
		
		//取消按钮驱动程序
		rbt1.setOnAction(e->{
			primaryStage.close();//关闭注册窗口
			new Login();//打开登录窗口
		});
		
		pane.setStyle("-fx-background-image:url(image/背景.jpg)");
		return pane;
	}
}

