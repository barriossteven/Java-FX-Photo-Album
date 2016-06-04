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
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 * 
 * Controller class for User that implements Initializable
 * 
 * @author Danny Choi
 * @author Steven Barrios
 *
 */

public class UserController implements Initializable{
	/**
	 * observable list of strings
	 */
	ObservableList<String> list = FXCollections.observableArrayList();
	/**
	 * ArrayList of Album objects for a single user
	 */
	ArrayList<Album> Albums = new ArrayList<Album>();

	@FXML 
	private ListView<String> table;
	
	/**
	 * storeDir is the directory where Users are stored
	 */
	 public static final String storeDir = "Data";
	 public String storeFile;
	 
	 
	User user;
	String username;
	
	/**
	 * setUser takes username and checks the users file and loads its contents onto listview
	 * @param username is the username
	 */
	public void setUser(String username) {					
		this.username = username;
		storeFile = username;
		File f;
		f = new File(storeDir + File.separator + storeFile + ".dat");
		if(f.length() == 0){ 
			write();
		}
		load();
		list = getObservableList();
		table.setItems(list);
	}
	/**
	 * load is a standard load class using serialization and input/output streams
	 */
	public void load(){
		   try
	        {
	            FileInputStream fis = new FileInputStream(storeDir + File.separator + storeFile + ".dat");
	            ObjectInputStream ois = new ObjectInputStream(fis);
	            Albums = (ArrayList<Album>) ois.readObject();
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
	
	
	/**
	 * write is a standard write class using serialization and input/output streams
	 */
	public void write(){
		 try{
	         FileOutputStream fos= new FileOutputStream(storeDir + File.separator + storeFile + ".dat");
	         ObjectOutputStream oos= new ObjectOutputStream(fos);
	         oos.writeObject(Albums);
	         oos.close();
	         fos.close();
	       }catch(IOException ioe){
	            ioe.printStackTrace();
	        }
	}
	
	/**
	 *  getObservableList is used to load the Album arraylist into an observablelist
	 *  
	 * @return l is the returned observablelist
	 */
	public ObservableList<String> getObservableList(){
		ObservableList<String> l = FXCollections.observableArrayList();
		for(int i = 0; i < Albums.size(); i++){
			l.add(Albums.get(i).albumName + " " + "Album Size: "+ Albums.get(i).Photos.size());
		}
		return l;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
//		// TODO Auto-generated method stub
//		load();
//		list = getObservableList();
//		table.setItems(list);
//		// select the first item
//		table.getSelectionModel().select(0);
	}
	
	
/**
 * btnOpenAlbumAction checks what the selected index of listview is and will open that selected album
 * 
 * @param event when open album button is clicked
 * @throws IOException
 */
	@FXML
	public void btnOpenAlbumAction(ActionEvent event) throws IOException{
		int albIndex = table.getSelectionModel().getSelectedIndex();
		if(albIndex == -1){
			return;
		}
		Stage primaryStage = new Stage();
		((Node) (event.getSource())).getScene().getWindow().hide();
		FXMLLoader loader = new FXMLLoader();
		SplitPane root = loader.load(getClass().getResource("/application/view/albumView.fxml").openStream());
		AlbumController albumController = (AlbumController)loader.getController();
		albumController.setAlbum(Albums, albIndex,username);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("User Menu");
		primaryStage.show();
	}
	
	/**
	 * createAlbum is called by btnCreateAlbumAction and is used to check for duplicate and position this new album will go to
	 * @param albumName is the albumName that the user wants to use
	 * @return
	 */
	public boolean createAlbum(String albumName){
		if(Albums.size()== 0){
			Albums.add(new Album(albumName));
			return true;
		}
		for(int i = 0; i< Albums.size(); i++){

			
			if(Albums.get(i).albumName.toLowerCase().compareTo(albumName.toLowerCase()) ==0){
				return false;
			}else if(Albums.get(i).albumName.toLowerCase().compareTo(albumName.toLowerCase())<0){
				continue;
			}else{
				Albums.add(i, new Album(albumName));
				write();
				return true;
			}
		}
		Albums.add(new Album(albumName));
		

		write();
		return true;
	}
	
	
	/**
	 * when Create Album button is clicked this method is called, it brings up dialog box and requests the album name.
	 * It then calls createAlbum to see if the album can be added.
	 * @param event
	 */
	@FXML
	public void btnCreateAlbumAction(ActionEvent event){
		
		TextInputDialog dialog = new TextInputDialog("Enter New Album Information");
		dialog.setTitle("Create New Album");
		dialog.setHeaderText("Enter New Album Information");
		dialog.setContentText("Enter Album name: ");
		
		Button confirmButton = ( Button ) dialog.getDialogPane().lookupButton( ButtonType.OK );
		confirmButton.setText("Create New Album");
		
		GridPane gridPane = new GridPane();
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setPadding(new Insets(20, 150, 10, 10));
	    TextField albumname = new TextField();
	    albumname.setPromptText("Album name");
	    gridPane.add(new Label("Album name:"), 0, 0);
	    gridPane.add(albumname, 1, 0);
	    
	    dialog.getDialogPane().setContent(gridPane);
	    Platform.runLater(() -> albumname.requestFocus());
	    
		Optional<String> result = dialog.showAndWait();
		Boolean input = false;
		if(result.isPresent()){
			if(albumname.getText().trim().equals("")){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Enter Album Name");
				alert.setHeaderText("Enter Album Name");
				alert.showAndWait();
				return;
			}else{
				input = createAlbum(albumname.getText().trim());
			}
			
		}
		if(input){
			list = getObservableList();
			table.setItems(list);
			table.getSelectionModel().select(0);
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Album Already Exists");
			alert.setHeaderText("Album already exists, please try again.");
			alert.showAndWait();
		}
	}

	/**
	 * Checks to see if the selected album can be deleted and will delete it.
	 * @param event
	 */
	@FXML
	public void btnDeleteAlbumAction(ActionEvent event){
		int albIndex = table.getSelectionModel().getSelectedIndex();
		if(albIndex == -1){
			return;
		}
		if(Albums.size()== 0){
			return;
		}
		int remove = table.getSelectionModel().getSelectedIndex();
		Albums.remove(remove);
		write();
		list = getObservableList();
		table.setItems(list);
		table.getSelectionModel().select(0);
		
	}
	
	

	@FXML
	public void btnSearchAction(ActionEvent event) throws IOException{
		Stage primaryStage = new Stage();
		((Node) (event.getSource())).getScene().getWindow().hide();
		FXMLLoader loader = new FXMLLoader();
		SplitPane root = loader.load(getClass().getResource("/application/view/searchView.fxml").openStream());
		SearchController searchController = (SearchController)loader.getController();
		searchController.setUser(username);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("User Menu");
		primaryStage.show();
	}
	
	
	/**
	 * If logout is clicked then the session will end and return to main login screen
	 * @param event
	 */
	@FXML
	public void Logout(ActionEvent event){
		
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
