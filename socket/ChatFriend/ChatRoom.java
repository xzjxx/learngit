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
	private GridPane friendpane;//�����б���ʾ����
	private Image image;
	private GridPane textpane;//������촰�ں����봰�ڵ���壬��ֱ����
	public TextArea displayArea=new TextArea();
	private TextArea inputArea=new TextArea();
	private Button sendbutton;//���Ͱ�ť
	public OutputStream ops;  //�����Ϣ��
	private String iputMsg; //�ڶԻ������������
	public String sendMsg; //��������˵�����
	private User u;

	public ChatRoom(User u)
	{
		stage=new Stage();
		borderpane=new BorderPane();
		dispalypane=new BorderPane();
		friendpane=new GridPane();
		image=new Image("image/ͷ��.gif");
		textpane=new GridPane();
		sendbutton=new Button("����");
		this.u=u;
		
		borderpane.setLeft(FriendPane(u.getUserID()));
		borderpane.setCenter(TextPane(u));
		borderpane.setTop(new Label(""));
		borderpane.setRight(new Label(" "));
		borderpane.setStyle("-fx-background-image:url(image/����.jpg)");
		Scene scene= new Scene(borderpane,1000,600);
		/*scene.setOnKeyPressed(e->{ 
			if(e.getCode()==KeyCode.ENTER)
			{
				presssend();
			}
			
		});*/
		stage.setTitle("��ӭ�������������");
		stage.setScene(scene);
		//stage.setResizable(false);//���岻�ܵ���
		stage.show();
		//�رմ��ڴ������ߣ�Ҫ�����û�������Ϣ
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
		//�������б���������
		HBox hbox=new HBox(10);
		//hbox.setPadding(new Insets(20,20,20,20));//���ñ߾�
		hbox.setStyle("-fx-border-color:pink");
		Label label=new Label("�����б�");
		label.setAlignment(Pos.CENTER);
		label.setFont(new Font("Monospaced",25));
		hbox.getChildren().add(label);
		friendpane.add(hbox,0,2);
		
		
		friendpane.add(setOnlinelist(),0,3);
		return friendpane;
	}
	public VBox setOnlinelist() {

		//�����б���ʾ
		VBox vbox=new VBox(5);
		vbox.setPadding(new Insets(0,0,500,20));//���ñ߾�
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
						label1.setTextFill(Color.RED);//������Ϊ��ɫ
					 }
					else
					{
						label1.setTextFill(Color.GRAY);//������Ϊ��ɫ
					}
					 vbox.getChildren().add(label1);
				 }
			 }
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			System.out.println("����");
		}
		return vbox;	
	}
	private GridPane TextPane(User u){
		//������ʾ���
		//TextArea displayArea=new TextArea();//����һ���ı�����
		displayArea.setEditable(false);//���ɱ༭��
		displayArea.setWrapText(true);//�ɲ���һ��
		displayArea.setFont(Font.font("Times",20));//��������
		dispalypane.setCenter(displayArea);
		textpane.add(dispalypane,0,1);
		
		
		Label inputlabel=new Label("�����");
		//����"�����"label
		textpane.add(inputlabel,0,2);
		textpane.add(inputArea,0,3);
		
		//���������������
		
		inputArea.setEditable(true);//���ɱ༭��
		inputArea.setWrapText(true);//�ɲ���һ��
		//���������뷢�Ͱ�ť
		textpane.add(sendbutton,0,4);
		//���Ͱ�ť��������
		sendbutton.setOnAction(e->{
			try {
				iputMsg=inputArea.getText();//��ȡ������е��ı�
				if(iputMsg.isEmpty())
				{
					JOptionPane.showMessageDialog(null,"�������ݲ���Ϊ�գ�","��ʾ",
							  JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					sendMsg=u.getUserID()+":"+iputMsg+"\n";//��������˵���ϢҪ���뻻�з�,����˲���֪��ʲôʱ���������
					//������ͺ��������Ҫ���
					inputArea.clear();
					iputMsg=null;
					//Ҫÿ���һ�η��;�Ҫ������д���������
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
