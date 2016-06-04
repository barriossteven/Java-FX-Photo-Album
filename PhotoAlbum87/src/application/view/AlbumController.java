package application.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.ListCell;
/**
 * This is the controller class for album
 * 
 * @author Steven Barrios
 * @author Danny Choi
 *
 */
public class AlbumController implements Initializable {
	/**
	 * observable list of image paths
	 */
	ObservableList<String> paths = FXCollections.observableArrayList();
	/**
	 * arraylist of albums
	 */
	ArrayList<Album> listOfAlbums;
	/**
	 * array list of photos of the current album
	 */
	ArrayList<Photo> currAlbum;
	
	int albIndex ;
	String username;
	@FXML
	AnchorPane imageAnchor;
	@FXML
	TextField imageCaption;
	int currentdisplay;
	public static final String storeDir = "Data";
	public String storeFile;
	
	@FXML
	public ListView<String> images;
	
	/**
	 * Recieves data from userController in order to create album controller
	 * 
	 * @param Albums is the list of albums for the user
	 * @param albIndex is the current album we are working with
	 * @param username is the user's name
	 */
	public void setAlbum(ArrayList<Album> Albums, int albIndex, String username){
		this.username = username;
		storeFile = username+".dat";
		
		listOfAlbums = Albums;
		this.albIndex = albIndex;
		currAlbum = Albums.get(albIndex).Photos;
		
		paths = getObservableList();
		images.setItems(paths);
	} 
	
	
	/**
	 * implemented initialize method which checks each cell in paths and converts the path into a thumbnail
	 * @param arg0
	 * @param arg1
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		 images.setCellFactory(new Callback<ListView<String>, 
		            ListCell<String>>() {
		                @Override 
		                public ListCell<String> call(ListView<String> list) {
		                    return new ColorRectCell();
		                }
		            }
		        );
		 
	}
	
	/**
	 * Static class created in order to use callback and cell factory in thumbnail conversion
	 *
	 */
	static class ColorRectCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            ImageView imageView = new ImageView();
            if (empty) {
                setGraphic(null);
            } else {
                // true makes this load in background
                // see other constructors if you want to control the size, etc
                //Image image = new Image("file:"+item) ; 
            //	System.out.println("this is item "+ item);
            	Image image = new Image(item,true);
                imageView.setImage(image);
                imageView.setFitHeight(80);
                imageView.setFitWidth(160);
                imageView.setPreserveRatio(true);
                setGraphic(imageView);
            }
        }
    }
	
	/**
	 * standard write method for serialization
	 */
	public void write(){
		 try{
	         FileOutputStream fos= new FileOutputStream(storeDir + File.separator + storeFile);
	         ObjectOutputStream oos= new ObjectOutputStream(fos);
	         listOfAlbums.get(albIndex).Photos = currAlbum;
	         oos.writeObject(listOfAlbums);
	         oos.close();
	         fos.close();
	       }catch(IOException ioe){
	            ioe.printStackTrace();
	        }
	}
	
	/**
	 * getObservableList simply populates observable list from currAlbum
	 * @return
	 */
	public ObservableList<String> getObservableList(){
		ObservableList<String> paths = FXCollections.observableArrayList();
		for(int i = 0; i < currAlbum.size(); i++){
			paths.add(currAlbum.get(i).path);
		}
		return paths;
	}
	
	/**
	 * addPhoto check if the photo is able to be added
	 * 
	 * @param photoname name of photo
	 * @param cap caption of photo
	 * @return
	 */
	public boolean addPhoto(String photoname, String cap){
		if(currAlbum.size()== 0){
			currAlbum.add(new Photo(photoname, cap));
			write();
			return true;
		}
		for(int i = 0; i< currAlbum.size(); i++){

			
			if(currAlbum.get(i).path.toLowerCase().compareTo(photoname.toLowerCase()) ==0){
				return false;
			}else if(currAlbum.get(i).path.toLowerCase().compareTo(photoname.toLowerCase())<0){
				continue;
			}else{
				currAlbum.add(i, new Photo(photoname,cap));
				write();
				return true;
			}
		}
		currAlbum.add(new Photo(photoname,cap));
		

		write();
		return true;
	}
	
	/**
	 * when add photo is clicked then this method is called and asks for path to photo and caption
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void btnAddPhotoAction(ActionEvent event) throws IOException{
		
		TextInputDialog dialog = new TextInputDialog("Enter New Photo Path");
		dialog.setTitle("Add New Photo");
		dialog.setHeaderText("Enter New Photo Path");
		dialog.setContentText("Enter New Photo Path: ");
		
		Button confirmButton = ( Button ) dialog.getDialogPane().lookupButton( ButtonType.OK );
		confirmButton.setText("Add New Photo");
		
		GridPane gridPane = new GridPane();
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setPadding(new Insets(20, 150, 10, 10));
	    TextField photopath = new TextField();
	    TextField cap = new TextField();
	    photopath.setPromptText("Photo Path");
	    cap.setPromptText("caption");
	    gridPane.add(new Label("Photo Path:"), 0, 0);
	    gridPane.add(new Label("Caption:"), 0, 1);
	    gridPane.add(photopath, 1, 0);
	    gridPane.add(cap, 1, 1);
	    
	    dialog.getDialogPane().setContent(gridPane);
	    Platform.runLater(() -> photopath.requestFocus());
	    
		Optional<String> result = dialog.showAndWait();
		Boolean input = false;
		if(result.isPresent()){
			if(photopath.getText().trim().equals("")){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Enter Photo Path");
				alert.setHeaderText("Enter Photo Path");
				alert.showAndWait();
				return;
			}else{
				input = addPhoto(photopath.getText().trim(), cap.getText().trim());
			}
			
		}
		if(input){
			paths = getObservableList();
			images.setItems(paths);
			images.getSelectionModel().select(0);
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error has occurred");
			alert.setHeaderText("Photo maybe already be in album or photo may not exist, please try again.");
			alert.showAndWait();
		}
		
	}
	/**
	 * When button to remove is clicked this will check if the photo is removable and will remove it.
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void btnRemovePhotoAction(ActionEvent event) throws IOException{
		
		if(currAlbum.size()== 0){
			return;
		}
		int remove = images.getSelectionModel().getSelectedIndex();
		if(remove == -1){
			return;
		}
		currAlbum.remove(remove);
		write();
		if(remove == currentdisplay){
			imageAnchor.getChildren().clear();
		}
		paths = getObservableList();
		images.setItems(paths);
		images.getSelectionModel().select(0);
	}
	
	/**
	 * Method will change the caption of the selected photo and if photo is currently being displayed then it will update the view
	 * 
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void btnChangeCaptionAction(ActionEvent event) throws IOException{
		int index = images.getSelectionModel().getSelectedIndex();
		if(index == -1){
			return;
		}
		System.out.println("this is selected "+ index);
		TextInputDialog dialog = new TextInputDialog("Enter New Caption");
		dialog.setTitle("Enter New Caption");
		dialog.setHeaderText("Enter New Caption");
		dialog.setContentText("Enter New Caption: ");
		
		Button confirmButton = ( Button ) dialog.getDialogPane().lookupButton( ButtonType.OK );
		confirmButton.setText("Enter New Caption");
		
		GridPane gridPane = new GridPane();
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setPadding(new Insets(20, 150, 10, 10));
	    TextField caption = new TextField();
	    caption.setPromptText("New Caption");
	    gridPane.add(new Label("Enter New Caption"), 0, 0);
	    gridPane.add(caption, 1, 0);
	    
	    dialog.getDialogPane().setContent(gridPane);
	    Platform.runLater(() -> caption.requestFocus());
	    
		Optional<String> result = dialog.showAndWait();
		boolean worked = false;
		if(result.isPresent()){
			if(caption.getText().trim().equals("")){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Enter New Caption");
				alert.setHeaderText("Enter New Caption");
				alert.showAndWait();
				return;
			}else{
				currAlbum.get(index).caption = caption.getText().trim();
				write();
				worked = true;
			}
			
		}
		if(worked){
			if(currentdisplay ==  index){
				imageCaption.setText(currAlbum.get(index).caption);
			}
			paths = getObservableList();
			images.setItems(paths);
			images.getSelectionModel().select(0);
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Enter New Caption");
			alert.setHeaderText("Enter Caption, please try again.");
			alert.showAndWait();
		}
		
	}
		/**
		 * When add tag button is clicked this will pop up dialog box and user enters tag and the tag is added to the list of tags in the photo
		 * 
		 * @param event
		 * @throws IOException
		 */
	@FXML
	public void btnAddTagAction(ActionEvent event) throws IOException{
		int index = images.getSelectionModel().getSelectedIndex();
		if(index == -1){
			return;
		}
		TextInputDialog dialog = new TextInputDialog("Enter New Tag");
		dialog.setTitle("Enter New Tag");
		dialog.setHeaderText("Enter New Tag");
		dialog.setContentText("Enter New Tag: ");
		
		Button confirmButton = ( Button ) dialog.getDialogPane().lookupButton( ButtonType.OK );
		confirmButton.setText("Enter New Tag");
		
		GridPane gridPane = new GridPane();
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setPadding(new Insets(20, 150, 10, 10));
	    TextField Tag = new TextField();
	    Tag.setPromptText("New Tag");
	    gridPane.add(new Label("Enter New Tag"), 0, 0);
	    gridPane.add(Tag, 1, 0);
	    
	    dialog.getDialogPane().setContent(gridPane);
	    Platform.runLater(() -> Tag.requestFocus());
	    
		Optional<String> result = dialog.showAndWait();
		boolean worked = false;
		if(result.isPresent()){
			if(Tag.getText().trim().equals("")){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Enter New Tag");
				alert.setHeaderText("Enter New Tag");
				alert.showAndWait();
				return;
			}else{
				//need an add tag
				//currAlbum.get(index).tags.add(Tag.getText().trim());
				worked = addTag(index, Tag.getText().trim().toLowerCase());
				write();
			}
			
		}
		if(worked){
			for(int i = 0; i< currAlbum.get(index).tags.size(); i++){
				System.out.println(currAlbum.get(index).tags.get(i));
			}
			paths = getObservableList();
			images.setItems(paths);
			images.getSelectionModel().select(0);
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Enter New Tag");
			alert.setHeaderText("Enter Tag, please try again.");
			alert.showAndWait();
		}
		
	}
	
	public boolean addTag(int index, String tag){
		
		
		for(int i = 0; i< currAlbum.get(index).tags.size(); i++){
			if(currAlbum.get(index).tags.get(i).compareTo(tag) ==0){
				return false;
			}else if(currAlbum.get(index).tags.get(i).compareTo(tag) <0){
				continue;
			}else{
				currAlbum.get(index).tags.add(i, tag);
				write();
				return true;
			}
		}
		currAlbum.get(index).tags.add( tag);
		write();
		return true;
		
	}
	@FXML
	public void btnDeleteTagAction(ActionEvent event) throws IOException{
		int index = images.getSelectionModel().getSelectedIndex();
		if(index == -1){
			return;
		}
		TextInputDialog dialog = new TextInputDialog("Enter Tag To Remove");
		dialog.setTitle("Enter Tag To Remove");
		dialog.setHeaderText("Enter Tag To Remove");
		dialog.setContentText("Enter Tag To Remove: ");
		
		Button confirmButton = ( Button ) dialog.getDialogPane().lookupButton( ButtonType.OK );
		confirmButton.setText("Enter Tag To Remove");
		
		GridPane gridPane = new GridPane();
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setPadding(new Insets(20, 150, 10, 10));
	    TextField Tag = new TextField();
	    Tag.setPromptText("Enter Tag To Remove");
	    gridPane.add(new Label("Enter Tag To Remove"), 0, 0);
	    gridPane.add(Tag, 1, 0);
	    
	    dialog.getDialogPane().setContent(gridPane);
	    Platform.runLater(() -> Tag.requestFocus());
	    
		Optional<String> result = dialog.showAndWait();
		boolean worked = false;
		if(result.isPresent()){
			if(Tag.getText().trim().equals("")){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Enter Tag To Remove");
				alert.setHeaderText("Enter Tag To Remove");
				alert.showAndWait();
				return;
			}else{
				worked = delTag(index, Tag.getText().trim().toLowerCase());
				write();
			}
			
		}
		if(worked){
			for(int i = 0; i< currAlbum.get(index).tags.size(); i++){
				System.out.println(currAlbum.get(index).tags.get(i));
			}
			paths = getObservableList();
			images.setItems(paths);	
			images.getSelectionModel().select(0);
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Enter Tag To Remove");
			alert.setHeaderText("Enter tag to remove, please try again.");
			alert.showAndWait();
		}
	}
	
public boolean delTag(int index, String tag){
		
		
		for(int i = 0; i< currAlbum.get(index).tags.size(); i++){
			if(currAlbum.get(index).tags.get(i).compareTo(tag) ==0){
				currAlbum.get(index).tags.remove(i);
				write();
				return true;
			}else if(currAlbum.get(index).tags.get(i).compareTo(tag) <0){
				continue;
			}else{
				//currAlbum.get(index).tags.add(i, tag);
				//write();
				return false;
			}
		}
		return false;
		
	}
	
@FXML	
public void btnMovePhotoAction(ActionEvent event) throws IOException{
	int index = images.getSelectionModel().getSelectedIndex();
	if(index == -1){
		return;
	}
	TextInputDialog dialog = new TextInputDialog("Enter Destination Album");
	dialog.setTitle("Enter Destination Album");
	dialog.setHeaderText("Enter Destination Album");
	dialog.setContentText("Enter Destination Album: ");
	
	Button confirmButton = ( Button ) dialog.getDialogPane().lookupButton( ButtonType.OK );
	confirmButton.setText("Enter Destination Album");
	
	GridPane gridPane = new GridPane();
    gridPane.setHgap(10);
    gridPane.setVgap(10);
    gridPane.setPadding(new Insets(20, 150, 10, 10));
    TextField destAlbum = new TextField();
    destAlbum.setPromptText("Enter Destination Album");
    gridPane.add(new Label("Enter Destination Album"), 0, 0);
    gridPane.add(destAlbum, 1, 0);
    
    dialog.getDialogPane().setContent(gridPane);
    Platform.runLater(() -> destAlbum.requestFocus());
    
	Optional<String> result = dialog.showAndWait();
	boolean worked = false;
	if(result.isPresent()){
		if(destAlbum.getText().trim().equals("")){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Enter Destination Album");
			alert.setHeaderText("Enter Destination Album");
			alert.showAndWait();
			return;
		}else{
			worked = movePhoto(index, destAlbum.getText().trim().toLowerCase());
			write();
		}
		
	}
	if(worked){
		paths = getObservableList();
		images.setItems(paths);	
		images.getSelectionModel().select(0);
	}else{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Enter Destination Album");
		alert.setHeaderText("Enter Destination Album, please try again.");
		alert.showAndWait();
	}
}

public boolean movePhoto(int index, String destAlbum){
	for(int i = 0; i < listOfAlbums.size(); i++) {
		if(listOfAlbums.get(i).albumName.equalsIgnoreCase(destAlbum)) {
			listOfAlbums.get(i).Photos.add( currAlbum.get(index));
			currAlbum.remove(index);
			return true;
		}
	}
	return false;
}
	@FXML
	public void btnSlideshowAction(ActionEvent event) throws IOException{
		
	}
	@FXML
	public void btnDisplayPhotoAction(ActionEvent event) throws IOException{
		imageAnchor.getChildren().clear();
		System.out.println("beginning display");
		int index = images.getSelectionModel().getSelectedIndex();
		if(index == -1){
			return;
		}
		currentdisplay = index;
		Image image = new Image(currAlbum.get(index).path);
		
		if(image == null || currAlbum.get(index).path == null){
			System.out.println("something was null");
		}
		ImageView iv1 = new ImageView();
		iv1.setFitWidth(200);
		iv1.setFitHeight(200);
		iv1.setImage(image);
		imageAnchor.getChildren().add(iv1);
		iv1.fitWidthProperty().bind(imageAnchor.widthProperty());
		iv1.fitHeightProperty().bind(imageAnchor.heightProperty());
	    iv1.setPreserveRatio(true);
	    imageCaption.setText(currAlbum.get(index).caption);
		
		System.out.println("end dispay");
		
	}
	@FXML
	public void Return(ActionEvent event) throws IOException{
		Stage primaryStage = new Stage();
		((Node) (event.getSource())).getScene().getWindow().hide();
		FXMLLoader loader = new FXMLLoader();
		SplitPane root = loader.load(getClass().getResource("/application/view/userView.fxml").openStream());
		UserController userController = (UserController)loader.getController();
		userController.setUser(username);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("User Menu");
		primaryStage.show();
	}
	
	@FXML
	public void Logout(ActionEvent event) throws IOException{
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
