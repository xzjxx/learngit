package Main;
import ChatLogin.Login;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import ChatFriend.ChatRoom;
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
import javafx.fxml.*;
public class MainWindow extends Application{
	public static void main(String[] args) 
	{
		Application.launch(args);
	}
	@Override
	public void start(Stage primaryStage){
		new Login();
	}		
}
