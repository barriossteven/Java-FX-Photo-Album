package application.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 * 	Login Controller checks whether user is admin or user
 * @author Steven Barrios
 * @author Danny Choi
 *
 */
public class LoginController implements Initializable{
	
	 public static final String storeDir = "Data";
	 
	@FXML
	public Label labelMessage;
	@FXML
	public TextField txtUsername;
	@FXML 
	public PasswordField txtPassword;
	/**
	 * when clicking login button, method checks whether user is admin or plain user or invalid username
	 * @param event
	 * @throws Exception
	 */
	@FXML
	public void btnLoginAction(ActionEvent event) throws Exception{
		
		/*
		 * harded usernames for admin and user for now,
		 * a sends you to admin view
		 * u sends you to user view
		 */
		
		if(txtUsername.getText().equals("admin")){
			//sends to admin view
			Stage primaryStage = new Stage();
			((Node) (event.getSource())).getScene().getWindow().hide();
			FXMLLoader loader = new FXMLLoader();
			SplitPane root = loader.load(getClass().getResource("/application/view/adminView.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Admin Menu");
			primaryStage.show();
		}else{
			String fname = txtUsername.getText().trim()+ ".dat";
			File f;
			f = new File(storeDir + File.separator + fname);
			if(f.exists()){
				//sends to user view
				Stage primaryStage = new Stage();
				((Node) (event.getSource())).getScene().getWindow().hide();
				FXMLLoader loader = new FXMLLoader();
				SplitPane root = loader.load(getClass().getResource("/application/view/userView.fxml").openStream());
				UserController userController = (UserController)loader.getController();
				userController.setUser(txtUsername.getText());
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.setTitle("User Menu");
				primaryStage.show();
			}else{
				labelMessage.setText("Invalid Username");
			}
		}
		
	}
	
	
	
	
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
	}
	


}
