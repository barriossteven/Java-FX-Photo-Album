package application;
	
import application.view.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
/**
 * 
 * Main class for photo album GUI
 * 
 * @author Danny Choi
 * @author Steven Barrios
 *
 */


public class PhotoAlbMain extends Application {
	@Override
	public void start(Stage primaryStage)throws Exception {
		//loads login fxml first
		
		Parent root = FXMLLoader.load(getClass().getResource("/application/view/Login.FXML"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Login");
		primaryStage.show();
		
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
