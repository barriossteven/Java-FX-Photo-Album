package application.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * User class
 * 
 * @author Steven Barrios
 * @author Danny Choi
 *
 */
public class User implements Serializable{
	String username;
	//ArrayList<Album> albums;
	public User(String username){
		this.username = username;
		//albums = null;
	}
	 public static final String storeDir = "Data";
	 public static final String storeFile = "users.dat";
	
	
	
}
