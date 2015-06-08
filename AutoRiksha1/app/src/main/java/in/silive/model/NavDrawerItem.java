package in.silive.model;


import java.util.ArrayList;

import android.content.Context;


/*
 * this class returns the values of items in the list at specific position
 */
public class NavDrawerItem {
	private String name;
	private String number;
	private int icon;
	private String venue;
	
	public NavDrawerItem() {
		// default constructor
	}
	// constructor if the list has all the three required fields
	public NavDrawerItem(String name, String num, int icon) {
		this.name = name;
		this.number = num;
		this.icon = icon;
	}
	// constructor if the list has all the three required fields
		public NavDrawerItem(String name, String num, int icon, String venue) {
			this.name = name;
			this.number = num;
			this.icon = icon;
			this.venue = venue;
		}

	
	// constructor if the list only has name and number
	public NavDrawerItem(String name, String num) {
		this.name = name;
		this.number = num;
	}

	// constructor if the list has only icon and name
	public NavDrawerItem(String name, int icon) {
		this.name = name;
		this.icon = icon;
	}

	// method to get the name
	public String getName() {
		return this.name;
	}

	// method to get the number
	public String getNum() {
		return this.number;
	}

	// method to get the icon
	public int getIcon() {
		return this.icon;
	}
	
	public String getVenue() {
		return this.venue;
	}
}