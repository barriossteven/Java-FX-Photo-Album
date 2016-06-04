package application.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;

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

import application.view.AlbumController.ColorRectCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
/**
 * Search Controller class handles searching by tags
 * 
 * @author Steven Barrios
 * @author Danny Choi
 */
public class SearchController implements Initializable{

	@FXML TextField dateEnd;
	@FXML TextField dateStart;
	@FXML TextField searchTags;
	@FXML public ListView<String> images;
	public String username;
	
	public static final String storeDir = "Data";
	public String storeFile;
	public ArrayList<Album> Albums;
	public ArrayList<Photo> allPhotos = new ArrayList<Photo>();
	ObservableList<String> paths = FXCollections.observableArrayList();
	@FXML AnchorPane imageAnchor;
	@FXML TextField imageCaption;
	@FXML TextField imagedate;

/**
 * simple write method for serialization
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
            	System.out.println("this is item "+ item);
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
	 * searches for tag when button is pressed
	 * @param event
	 */
	@FXML public void btnSearchAction(ActionEvent event) {

		File f;
		f = new File(storeDir + File.separator + storeFile + ".dat");
		String tag = searchTags.getText();
		String startDate = dateStart.getText();
		String endDate = dateEnd.getText();
		
		

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
	   
	   for (int i = 0; i < Albums.size(); i++) {
		   Album currAlbum = Albums.get(i);
		   for (int j = 0; j < currAlbum.Photos.size(); j++) {
			   Photo currPhoto = currAlbum.Photos.get(j);
			   
			   System.out.println(i + " sifhiahsdf " +j);
			   System.out.println("size of i " +Albums.size());
			   System.out.println("size of j " +currAlbum.Photos.size());
			   System.out.println("s==te st: " +currAlbum.Photos.get(j));
			   System.out.println("asdfjosdjfoajsdofjsaf : " +currAlbum.Photos.get(j).cal.toString());
			   
			   // CONDITIONAL CHECK FOR SEARCH
			   Boolean checkTag = false;
			   Boolean checkStartDate = false;
			   Boolean checkEndDate = false;
			   Boolean addPhoto = true;
			   if(tag.length() > 0) {
				   checkTag = true;
			   }
			   //doesnt contain tag
			   if(checkTag && !currPhoto.tags.contains(tag)) {
				   addPhoto = false;
			   }
			   
			   if(addPhoto) {
				   allPhotos.add(currAlbum.Photos.get(j));
			   }
		   }
	   }
	   

		paths = getObservableList();
		images.setItems(paths);
		
	}
	/**
	 * gets observable list
	 * @return
	 */

	public ObservableList<String> getObservableList(){
		ObservableList<String> paths = FXCollections.observableArrayList();
		for(int i = 0; i < allPhotos.size(); i++){
			paths.add(allPhotos.get(i).path);
		}
		return paths;
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
		
		Album newAlbum = new Album(albumName);
		newAlbum.Photos = allPhotos;
		Albums.add(newAlbum);
		write();
		return true;
	}
	
	@FXML
	public void btnDisplayPhotoAction(ActionEvent event) throws IOException{
		imageAnchor.getChildren().clear();
		System.out.println("beginning display");
		int index = images.getSelectionModel().getSelectedIndex();
		if(index == -1){
			return;
		}
//		currentdisplay = index;
		Image image = new Image(allPhotos.get(index).path);
		
		if(image == null || allPhotos.get(index).path == null){
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
	    imageCaption.setText(allPhotos.get(index).caption);
		
		System.out.println("end dispay");
		
	}
	
	@FXML public void btnCreateAlbumAction(ActionEvent event) {
		if(allPhotos.isEmpty()) {
			return;
		}

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
			//clear search results
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Album Already Exists");
			alert.setHeaderText("Album already exists, please try again.");
			alert.showAndWait();
		}
	}

	@FXML public void btnReturnAction(ActionEvent event) throws IOException{
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
	
	public void setUser(String username) {					
		this.username = username;
		storeFile = username;
		File f;
		f = new File(storeDir + File.separator + storeFile + ".dat");
//		if(f.length() == 0){ 
//			write();
//		}
//		load();
//		list = getObservableList();
//		table.setItems(list);
	}


}
