package application.view;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class adminController implements Initializable {	
	//array list of users
	ArrayList<User> Users = new ArrayList<User>();
	
	//listview of strings
	@FXML 
	private ListView<String> table;
	
	//observable list of usernames
	ObservableList<String> list = FXCollections.observableArrayList();
	 public static final String storeDir = "Data";
	 public static final String storeFile = "users.dat";
	
	
	
	public ObservableList<String> getObservableList(){
		ObservableList<String> l = FXCollections.observableArrayList();
		for(int i = 0; i < Users.size(); i++){
			l.add(Users.get(i).username);
		}
		return l;
	}
	

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		load();
		list = getObservableList();
		table.setItems(list);
		// select the first item
		table.getSelectionModel().select(0);
	}
	
	
	
	public void load(){
		   try
	        {
	            FileInputStream fis = new FileInputStream(storeDir + File.separator + storeFile);
	            ObjectInputStream ois = new ObjectInputStream(fis);
	            Users = (ArrayList<User>) ois.readObject();
	            ois.close();
	            fis.close();
	         }catch(IOException ioe){
	             ioe.printStackTrace();
	             return;
	          }catch(ClassNotFoundException c){
	             System.out.println("Class not found");
	             c.printStackTrace();
	             return;
	          }
	}
	
	public void write(){
		 try{
	         FileOutputStream fos= new FileOutputStream(storeDir + File.separator + storeFile);
	         ObjectOutputStream oos= new ObjectOutputStream(fos);
	         oos.writeObject(Users);
	         oos.close();
	         fos.close();
	       }catch(IOException ioe){
	            ioe.printStackTrace();
	        }
	}
	
	
	//when create button is press this dialog comes up so you can add user
	@FXML
	public void btnCreateUserAction(ActionEvent event) throws IOException{
		TextInputDialog dialog = new TextInputDialog("Enter New User Information");
		dialog.setTitle("Create New User");
		dialog.setHeaderText("Enter New User Information");
		dialog.setContentText("Enter Username: ");
		
		Button confirmButton = ( Button ) dialog.getDialogPane().lookupButton( ButtonType.OK );
		confirmButton.setText("Create New User");
		
		GridPane gridPane = new GridPane();
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setPadding(new Insets(20, 150, 10, 10));
	    TextField username = new TextField();
	    username.setPromptText("Username");
	    gridPane.add(new Label("Username:"), 0, 0);
	    gridPane.add(username, 1, 0);
	    
	    dialog.getDialogPane().setContent(gridPane);
	    Platform.runLater(() -> username.requestFocus());
	    
		Optional<String> result = dialog.showAndWait();
		Boolean input = false;
		if(result.isPresent()){
			if(username.getText().trim().equals("")){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Enter UserName");
				alert.setHeaderText("Enter UserName");
				alert.showAndWait();
				return;
			}else{
				input = addUser(username.getText().trim());
			}
			
		}
		if(input){
			list = getObservableList();
			table.setItems(list);
			table.getSelectionModel().select(0);
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("User Already Exists");
			alert.setHeaderText("User already exists, please try again.");
			alert.showAndWait();
		}
		
	}
	
	
	
	
	
	public boolean addUser(String username) throws IOException{
		if(Users.size()== 0){
			Users.add(new User(username));
			createFile(username);
			write();
			return true;
		}
		for(int i = 0; i< Users.size(); i++){

			
			if(Users.get(i).username.toLowerCase().compareTo(username.toLowerCase()) ==0){
				return false;
			}else if(Users.get(i).username.toLowerCase().compareTo(username.toLowerCase())<0){
				continue;
			}else{
				Users.add(i, new User(username));
				createFile(username);
				write();
				return true;
			}
		}
		Users.add(new User(username));
		createFile(username);
		write();
		return true;
	}
	
	public void createFile(String username) throws IOException{
		File f;
		String filename = username+".dat";
		f = new File(storeDir + File.separator + filename);
		if(!f.exists()){
			f.createNewFile();
		}
	}
	
	
	
	@FXML
	public void btnDeleteUserAction(ActionEvent event){
		if(Users.size()== 0){
			return;
		}
		int remove = table.getSelectionModel().getSelectedIndex();
		String fname = Users.get(remove).username + ".dat";
		File f;
		f = new File(storeDir + File.separator + fname);
		f.delete();
		Users.remove(remove);
		write();
		list = getObservableList();
		table.setItems(list);
		table.getSelectionModel().select(0);
	}
	
	
	
	
	@FXML
	public void btnLogoutAction(ActionEvent event){
		try {
			Stage primaryStage = new Stage();
			((Node) (event.getSource())).getScene().getWindow().hide();
			FXMLLoader loader = new FXMLLoader();
			Pane root;
			root = loader.load(getClass().getResource("/application/view/Login.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("User Menu");
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	


}
