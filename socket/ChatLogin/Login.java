package ChatLogin;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;

import ChatClient.ChatClient;
import Common.User;
import DataBase.*;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.fxml.*;

public class Login
{
	static File file=new File("Y:\\LasterUser.txt");
	
	private Stage LoginStage;  //登录面板
	private BorderPane pane=new BorderPane();
	private VBox vbox;
	private Image image;//图片
	private ComboBox userComboBox;  //用户名组合框
	PasswordField pf;//密码文本框
	private HBox hbox;
	Button loginbt;
	Button registerbt;
	public Login(){
		LoginStage=new Stage();
		pane=new BorderPane();
		vbox=new VBox(15);
		image=new Image("image/image1.gif");
		userComboBox = new ComboBox();
		pf=new PasswordField();
		hbox=new HBox(15);
		loginbt=new Button("登录");
		registerbt=new Button("注册");
		Scene scene= new Scene(Loginpane(),430,700);
		scene.setOnKeyPressed(e->{ 
			if(e.getCode()==KeyCode.ENTER)
			{
				presslogin();
			}
			
		});
		
		LoginStage.setTitle("多人聊天室");
		LoginStage.setScene(scene);
		LoginStage.setResizable(false);//窗体不能调整
		LoginStage.show();
	}

//登录界面
	private BorderPane Loginpane()
	{

		
		//设置中间界面（包括用户名，密码等）
		VBox vbox=new VBox(15);
		vbox.setPadding(new Insets(100,100,100,100));//设置边距
		//vbox.setStyle("-fx-background-color:pink")；	
		vbox.getChildren().addAll(new ImageView(image));
		//读取文件，将最近登录的三个用户添加进用户名组合框
		readFileString();
	    userComboBox.setEditable(true);          
	    userComboBox.setOnAction((Event ev) -> {     
	         userComboBox.getSelectionModel().getSelectedItem().toString();      
	        }); 
		vbox.getChildren().addAll(new Label("用户名："),userComboBox);//将用户名组合框添加进pane
		
		//设置“记住密码”可选按钮属性
		vbox.getChildren().addAll(new Label("密码："),pf);//将密码文本框和“记住密码”可选按钮添加进pane
		pane.setTop(vbox);
		
		//设置按钮界面
		HBox hbox=new HBox(15);
		hbox.setPadding(new Insets(15,15,15,15));
		hbox.setSpacing(10);
		
		Label label=new Label("                  ");//为了设置按钮间距
		Label label1=new Label("                 ");//为了设置按钮间距
		hbox.getChildren().addAll(label,loginbt,label1,registerbt);//将按钮添加到pane
		
		//登录按钮驱动程序
		loginbt.setOnAction(e->{
			presslogin();
		});
		//注册按钮驱动程序
		registerbt.setOnAction(e->{
			new Register();//进入注册界面
			LoginStage.close();//关闭登录界面
		});

		hbox.setFillHeight(true);
		pane.setCenter(hbox);
		pane.setStyle("-fx-background-image:url(image/背景.jpg)");
		return pane;
	}
	//发送按钮和enter
	public void presslogin() {
		User u=new User();
		u.setUserID(userComboBox.getValue().toString());//从用户名组合框中获取用户名
		u.setPasswd(pf.getText());//从密码文本框中获取密码
		//将用户信息进行检查
		int n=new ConDB().CheckUser(u);//调用数据库检查用户信息函数
			if(n==1)
			{
				//验证成功,更改数据库用户状态，并进入用户端
				LoginFile(u.userid);//将登入成功用户写入文件
				u.setonline("yes");//将用户状态改为上线
				new ConDB().updata(u);
				new ChatClient(u);
				//new Client();
				LoginStage.close();
			}
			if(n==2){ 
				//密码错误，显示提示窗口
				JOptionPane.showMessageDialog(null,"密码错误","提示",
				  JOptionPane.ERROR_MESSAGE);
			}
			if(n==0)
			{
				//用户名不存在，显示提示窗口
				JOptionPane.showMessageDialog(null,"用户名不存在。","提示",
						  JOptionPane.ERROR_MESSAGE);
			}
	}
	//登录用户写入文件
	public void LoginFile(String userid)
	{
		try {
			if(!file.exists())
				file.createNewFile();
			FileWriter w = new FileWriter(file,true);
			List<String> linelist=Collections.emptyList();//创建一个List对象，并初始化为空（不占内存）
			linelist=java.nio.file.Files.readAllLines(Paths.get(file.getPath()),
					   StandardCharsets.UTF_8);
			
			//将登录成功的用户写入文件，如果与上一个登录成功的用户相等，则不写入
			if(linelist.size()>=1)
			{
				if(!userid.equals(linelist.get(linelist.size()-1)))
				{
					w.write(userid+"\n");
					w.flush();//刷新
					w.close();//关闭
				}
				else
					System.out.println("当前用户与前一用户相等");
			}
			else
			{
				w.write(userid+"\n");
				w.flush();//刷新
				w.close();//关闭
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	//用户文本框处显示最近登录的3个用户的用户名（少于的3个的显示2个或者1个）
	   public void readFileString() {  
		   List<String> linelist=Collections.emptyList();//创建一个List对象，并初始化为空（不占内存）
		   try {
			   if(!file.exists())
				   file.createNewFile();
			   linelist=java.nio.file.Files.readAllLines(Paths.get(file.getPath()),
					   StandardCharsets.UTF_8);			   
			  //输出最近登录的不一样的三个用户
			  if(linelist.size()!=0)
			  {
				  int count=1;
				  userComboBox.getItems().add(linelist.get(linelist.size()-1));//将最近登录的第一个用户添加到组合框
				   for(int i=linelist.size()-2;i>=0;i--)
				   {
					   for(int j=linelist.size()-1;j>i;j--)
					   { 
						   if(count>2)
							   break;//如果超过三个就退出，因为只需要显示三个不同的用户
						   if(!linelist.get(i).equals(linelist.get(j)))
						   {
							   if(j==i+1)
							   {
								   userComboBox.getItems().add(linelist.get(i));
								   count++;
								   break;
							   }
						   }
						   else
							   break;
					   }
					}	
			  }
		   } catch (IOException e) {
				// TODO 自动生成的 catch 块
				System.out.println("失败");
			}
				
	   }
}



