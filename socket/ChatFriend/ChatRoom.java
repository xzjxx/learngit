package ChatFriend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.swing.JOptionPane;

import java.io.*;
import Common.User;
import DataBase.ConDB;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ChatRoom {
	private Stage stage;
	private BorderPane borderpane;
	private BorderPane dispalypane;
	private GridPane friendpane;//好友列表显示界面
	private Image image;
	private GridPane textpane;//存放聊天窗口和输入窗口的面板，竖直放置
	public TextArea displayArea=new TextArea();
	private TextArea inputArea=new TextArea();
	private Button sendbutton;//发送按钮
	public OutputStream ops;  //输出信息流
	private String iputMsg; //在对话框输入的内容
	public String sendMsg; //传给服务端的内容
	private User u;

	public ChatRoom(User u)
	{
		stage=new Stage();
		borderpane=new BorderPane();
		dispalypane=new BorderPane();
		friendpane=new GridPane();
		image=new Image("image/头像.gif");
		textpane=new GridPane();
		sendbutton=new Button("发送");
		this.u=u;
		
		borderpane.setLeft(FriendPane(u.getUserID()));
		borderpane.setCenter(TextPane(u));
		borderpane.setTop(new Label(""));
		borderpane.setRight(new Label(" "));
		borderpane.setStyle("-fx-background-image:url(image/背景.jpg)");
		Scene scene= new Scene(borderpane,1000,600);
		/*scene.setOnKeyPressed(e->{ 
			if(e.getCode()==KeyCode.ENTER)
			{
				presssend();
			}
			
		});*/
		stage.setTitle("欢迎进入多人聊天室");
		stage.setScene(scene);
		//stage.setResizable(false);//窗体不能调整
		stage.show();
		//关闭窗口触发下线，要更改用户在线信息
		stage.setOnCloseRequest(e->{
			u.setonline("no");
			new ConDB().updata(u);
		});
	}
	private GridPane FriendPane(String userid)
	{
		//friendpane.setAlignment(Pos.TOP_LEFT);
		HBox userbox=new HBox(5);
		Label userlabel=new Label(userid);
		userbox.getChildren().addAll(new ImageView(image),userlabel);
		friendpane.add(userbox,0,1);
		//”在线列表“标题设置
		HBox hbox=new HBox(10);
		//hbox.setPadding(new Insets(20,20,20,20));//设置边距
		hbox.setStyle("-fx-border-color:pink");
		Label label=new Label("在线列表");
		label.setAlignment(Pos.CENTER);
		label.setFont(new Font("Monospaced",25));
		hbox.getChildren().add(label);
		friendpane.add(hbox,0,2);
		
		
		friendpane.add(setOnlinelist(),0,3);
		return friendpane;
	}
	public VBox setOnlinelist() {

		//在线列表显示
		VBox vbox=new VBox(5);
		vbox.setPadding(new Insets(0,0,500,20));//设置边距
		vbox.setStyle("-fx-border-color:pink");
		 List<Label> lists=Collections.emptyList();
		 int i=0;
		 String useridrs=null,online=null;
		 try {
			ResultSet rs=new ConDB().AllUser();
			while(rs.next())
			 {
				useridrs=rs.getString("userid");
				 online=rs.getString("online");
				 Label label1 = null;
				 Font font=new Font("Monospaced",18);
				 if(!useridrs.equals(u.userid)) {
					 label1=new Label(useridrs);
					 label1.setFont(font);
					 label1.setContentDisplay(ContentDisplay.LEFT);
					 //label1.setStyle("-fx-border-color:black");
					 if(online.equals("yes")) 
					 {
						label1.setTextFill(Color.RED);//在线设为红色
					 }
					else
					{
						label1.setTextFill(Color.GRAY);//离线设为灰色
					}
					 vbox.getChildren().add(label1);
				 }
			 }
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			System.out.println("错误");
		}
		return vbox;	
	}
	private GridPane TextPane(User u){
		//创建显示面板
		//TextArea displayArea=new TextArea();//创建一个文本区域
		displayArea.setEditable(false);//不可编辑的
		displayArea.setWrapText(true);//可拆到下一行
		displayArea.setFont(Font.font("Times",20));//设置字体
		dispalypane.setCenter(displayArea);
		textpane.add(dispalypane,0,1);
		
		
		Label inputlabel=new Label("输入框");
		//加入"输入框"label
		textpane.add(inputlabel,0,2);
		textpane.add(inputArea,0,3);
		
		//设置输入面板属性
		
		inputArea.setEditable(true);//不可编辑的
		inputArea.setWrapText(true);//可拆到下一行
		//在面板里加入发送按钮
		textpane.add(sendbutton,0,4);
		//发送按钮触发程序
		sendbutton.setOnAction(e->{
			try {
				iputMsg=inputArea.getText();//获取输入框中的文本
				if(iputMsg.isEmpty())
				{
					JOptionPane.showMessageDialog(null,"发送内容不能为空！","提示",
							  JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					sendMsg=u.getUserID()+":"+iputMsg+"\n";//传给服务端的消息要加入换行符,服务端才能知道什么时候结束接收
					//点击发送后，输入面板要清空
					inputArea.clear();
					iputMsg=null;
					//要每点击一次发送就要将内容写入输出流中
			            ops.write(sendMsg.getBytes()); 
			            ops.flush();
				}
	        } catch (IOException e1) {
	            e1.printStackTrace();
	        }   				
		});
		return textpane;
	}
	
	public TextArea getArea() {
		return displayArea;
	}
	public String getUserid() {
		return u.userid;
	}
}
