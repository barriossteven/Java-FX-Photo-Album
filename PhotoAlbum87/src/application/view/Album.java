package application.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable{
	String albumName;
	ArrayList<Photo> Photos;
	
	public Album(String albumName) {
		this.albumName = albumName;
		Photos = new ArrayList<Photo>();
	}
}
