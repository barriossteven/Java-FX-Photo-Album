package application.view;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Calendar;

import javafx.scene.image.Image;
/**
 * 
 * @author Steven Barrios
 * @author Danny Choi
 *
 */
public class Photo implements Serializable{
	/**
	 * photo's caption
	 */
	String caption;
	/**
	 * list of tags
	 */
	ArrayList<String> tags;
	/**
	 * photo path
	 */
	String path;
	/**
	 * photo date
	 */
	Calendar cal;

	public Photo(String path, String cap){
		this.path = path;
		this.caption = cap;
		setDate();
		this.tags = new ArrayList<String>();
		
		
	}
	/**
	 * setDate sets the date the photo was edited
	 */
	public void setDate(){
		this.cal = cal.getInstance();
		cal.set(Calendar.MILLISECOND,0);
		
		System.out.println(cal.DAY_OF_MONTH);
		System.out.println(cal.MONTH);
		System.out.println(cal.YEAR);
	}
	
	
}
