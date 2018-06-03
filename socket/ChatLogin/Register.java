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
	TextField rtf;//�û��������
	PasswordField rpf,rpf2;//��������򣬵ڶ�������ȷ�Ͽ�
	Button rbt;//ע�ᰴť
	Button rbt1;//ȡ����ť
	public Register(){
		//��ʼ�����г�Ա
		primaryStage=new Stage();
		pane=new BorderPane();
		rtf=new TextField();
		rpf=new PasswordField();
		rpf2=new PasswordField();
		rbt=new Button("ע��");
		rbt1=new Button("ȡ��");

		//pane.setAlignment(Pos.CENTER);//���ô������ݵ�λ��Ϊ����
		pane.setStyle("-fx-background-image:url(image/����.jpg)");
		Scene scene= new Scene(Registerpane(),430,700);
		primaryStage.setTitle("��ӭע��");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);//���岻�ܵ���
		primaryStage.show();
	}


//ע�����
	private BorderPane Registerpane()
	{

		
		rtf.setPromptText("�ǳ�");
		rpf.setPromptText("����(������6λ��");
		rpf2.setPromptText("������������");
		final Label label1=new Label();//��ʾ�û�����Ϣ����
		final Label label2=new Label();//��ʾ������Ϣ����
		final Label label3=new Label();//��ʾ�ڶ��������Ƿ����һ��һ��
		
		VBox vbox=new VBox(15);
		vbox.setPadding(new Insets(100,100,100,100));//���ñ߾�
		
		vbox.getChildren().addAll(new Label("�û�����"),rtf,label1);
		vbox.getChildren().addAll(new Label("���룺"),rpf,label2);
		vbox.getChildren().addAll(new Label("ȷ������"),rpf2,label3);
		pane.setTop(vbox);
		
		HBox hbox=new HBox(15);
		hbox.setPadding(new Insets(15,15,15,15));
		hbox.setSpacing(10);
		
		Label rlabel=new Label("                  ");//Ϊ�����ð�ť���
		Label rlabel1=new Label("         ");//Ϊ�����ð�ť���
		//���ð�ť��С
		rbt.setPrefSize(80, 20);
		rbt1.setPrefSize(80, 20);
		hbox.getChildren().addAll(rlabel,rbt,rlabel1,rbt1);
		pane.setCenter(hbox);
		System.out.println(rtf.getText());
		/*rpf.setOnAction(e->{ 
			System.out.println(rtf.getText());
			if(rtf.getText().equals(""))
			{
				label1.setText("�û���������Ϊ��");
				label1.setTextFill(Color.RED);
			}
		});*/
		
		//ע�ᰴť��������
		rbt.setOnAction(e->{
			User u=new User();
			u.setUserID(rtf.getText());//��������л�ȡ�û���
			u.setPasswd(rpf.getText());//��������л�ȡ����
			u.setonline("no");//�����û�����״̬Ϊ��no��
			System.out.print("user"+u.userid);
			System.out.print("mima"+u.passwd);
			ConDB con=new ConDB();
			int flag=con.CheckUser(u);
			
			//�û�����������Ϊ��
			if(u.userid.equals("")||u.passwd.equals(""))
			{
				//�û���Ϊ��
				if(u.userid.equals(""))
				{
					label1.setText("�û���������Ϊ��");
					label1.setTextFill(Color.RED);
				}
				//����Ϊ��
				if(u.passwd.equals(""))
				{
					label2.setText("���벻��Ϊ��");
					label1.setTextFill(Color.RED);
				}
			}
			if(flag!=0) 
			{
					label1.setText("�û����Ѵ���");
			}	
			//��������6λ���������������벻һ��
			if(u.passwd.length()<6||!u.passwd.equals(rpf2.getText())){ 
				if(u.passwd.length()<6)
					label2.setText("���벻������6λ");
				if(!u.passwd.equals(rpf2.getText()))
					label3.setText("���벻һ��");
			}
			//������֤ͨ��
			if(flag==0&&u.passwd.length()>=6&&u.passwd.equals(rpf2.getText()))
			{
				con.InsertUser(u);//���û���Ϣ�������ݿ�
				JOptionPane.showMessageDialog(null,"ע��ɹ�","��Ϣ",
						JOptionPane.ERROR_MESSAGE);//����ע��ɹ�����
				new Login();//������¼����
				primaryStage.close();//�ر�ע�����
			}
		});
		
		//ȡ����ť��������
		rbt1.setOnAction(e->{
			primaryStage.close();//�ر�ע�ᴰ��
			new Login();//�򿪵�¼����
		});
		
		pane.setStyle("-fx-background-image:url(image/����.jpg)");
		return pane;
	}
}

