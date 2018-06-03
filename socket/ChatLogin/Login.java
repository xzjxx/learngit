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
	
	private Stage LoginStage;  //��¼���
	private BorderPane pane=new BorderPane();
	private VBox vbox;
	private Image image;//ͼƬ
	private ComboBox userComboBox;  //�û�����Ͽ�
	PasswordField pf;//�����ı���
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
		loginbt=new Button("��¼");
		registerbt=new Button("ע��");
		Scene scene= new Scene(Loginpane(),430,700);
		scene.setOnKeyPressed(e->{ 
			if(e.getCode()==KeyCode.ENTER)
			{
				presslogin();
			}
			
		});
		
		LoginStage.setTitle("����������");
		LoginStage.setScene(scene);
		LoginStage.setResizable(false);//���岻�ܵ���
		LoginStage.show();
	}

//��¼����
	private BorderPane Loginpane()
	{

		
		//�����м���棨�����û���������ȣ�
		VBox vbox=new VBox(15);
		vbox.setPadding(new Insets(100,100,100,100));//���ñ߾�
		//vbox.setStyle("-fx-background-color:pink")��	
		vbox.getChildren().addAll(new ImageView(image));
		//��ȡ�ļ����������¼�������û���ӽ��û�����Ͽ�
		readFileString();
	    userComboBox.setEditable(true);          
	    userComboBox.setOnAction((Event ev) -> {     
	         userComboBox.getSelectionModel().getSelectedItem().toString();      
	        }); 
		vbox.getChildren().addAll(new Label("�û�����"),userComboBox);//���û�����Ͽ���ӽ�pane
		
		//���á���ס���롱��ѡ��ť����
		vbox.getChildren().addAll(new Label("���룺"),pf);//�������ı���͡���ס���롱��ѡ��ť��ӽ�pane
		pane.setTop(vbox);
		
		//���ð�ť����
		HBox hbox=new HBox(15);
		hbox.setPadding(new Insets(15,15,15,15));
		hbox.setSpacing(10);
		
		Label label=new Label("                  ");//Ϊ�����ð�ť���
		Label label1=new Label("                 ");//Ϊ�����ð�ť���
		hbox.getChildren().addAll(label,loginbt,label1,registerbt);//����ť��ӵ�pane
		
		//��¼��ť��������
		loginbt.setOnAction(e->{
			presslogin();
		});
		//ע�ᰴť��������
		registerbt.setOnAction(e->{
			new Register();//����ע�����
			LoginStage.close();//�رյ�¼����
		});

		hbox.setFillHeight(true);
		pane.setCenter(hbox);
		pane.setStyle("-fx-background-image:url(image/����.jpg)");
		return pane;
	}
	//���Ͱ�ť��enter
	public void presslogin() {
		User u=new User();
		u.setUserID(userComboBox.getValue().toString());//���û�����Ͽ��л�ȡ�û���
		u.setPasswd(pf.getText());//�������ı����л�ȡ����
		//���û���Ϣ���м��
		int n=new ConDB().CheckUser(u);//�������ݿ����û���Ϣ����
			if(n==1)
			{
				//��֤�ɹ�,�������ݿ��û�״̬���������û���
				LoginFile(u.userid);//������ɹ��û�д���ļ�
				u.setonline("yes");//���û�״̬��Ϊ����
				new ConDB().updata(u);
				new ChatClient(u);
				//new Client();
				LoginStage.close();
			}
			if(n==2){ 
				//���������ʾ��ʾ����
				JOptionPane.showMessageDialog(null,"�������","��ʾ",
				  JOptionPane.ERROR_MESSAGE);
			}
			if(n==0)
			{
				//�û��������ڣ���ʾ��ʾ����
				JOptionPane.showMessageDialog(null,"�û��������ڡ�","��ʾ",
						  JOptionPane.ERROR_MESSAGE);
			}
	}
	//��¼�û�д���ļ�
	public void LoginFile(String userid)
	{
		try {
			if(!file.exists())
				file.createNewFile();
			FileWriter w = new FileWriter(file,true);
			List<String> linelist=Collections.emptyList();//����һ��List���󣬲���ʼ��Ϊ�գ���ռ�ڴ棩
			linelist=java.nio.file.Files.readAllLines(Paths.get(file.getPath()),
					   StandardCharsets.UTF_8);
			
			//����¼�ɹ����û�д���ļ����������һ����¼�ɹ����û���ȣ���д��
			if(linelist.size()>=1)
			{
				if(!userid.equals(linelist.get(linelist.size()-1)))
				{
					w.write(userid+"\n");
					w.flush();//ˢ��
					w.close();//�ر�
				}
				else
					System.out.println("��ǰ�û���ǰһ�û����");
			}
			else
			{
				w.write(userid+"\n");
				w.flush();//ˢ��
				w.close();//�ر�
			}
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	//�û��ı�����ʾ�����¼��3���û����û��������ڵ�3������ʾ2������1����
	   public void readFileString() {  
		   List<String> linelist=Collections.emptyList();//����һ��List���󣬲���ʼ��Ϊ�գ���ռ�ڴ棩
		   try {
			   if(!file.exists())
				   file.createNewFile();
			   linelist=java.nio.file.Files.readAllLines(Paths.get(file.getPath()),
					   StandardCharsets.UTF_8);			   
			  //��������¼�Ĳ�һ���������û�
			  if(linelist.size()!=0)
			  {
				  int count=1;
				  userComboBox.getItems().add(linelist.get(linelist.size()-1));//�������¼�ĵ�һ���û���ӵ���Ͽ�
				   for(int i=linelist.size()-2;i>=0;i--)
				   {
					   for(int j=linelist.size()-1;j>i;j--)
					   { 
						   if(count>2)
							   break;//��������������˳�����Ϊֻ��Ҫ��ʾ������ͬ���û�
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
				// TODO �Զ����ɵ� catch ��
				System.out.println("ʧ��");
			}
				
	   }
}



