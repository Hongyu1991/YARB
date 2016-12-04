package com.stylease.entities;

import java.util.ArrayList;

public class Attribute {
	int id;
    String name;

    public Attribute(int id, String name){
    	this.id= id;
    	this.name = name;
    }
    
    
	public Attribute() {
	}


	public String getName() {
		return name;
	}

	public int getId() {
        return id;
    }
	
	public ArrayList<Attribute> allAttribs(){
		ArrayList<Attribute> arr = new ArrayList<Attribute>();
		
		Attribute a1 = new Attribute(1, "Bold");
		Attribute a2 = new Attribute(2, "Italicized");
		Attribute a3 = new Attribute(3, "Color: Red");
		
		arr.add(a1);
		arr.add(a2);
		arr.add(a3);
		
		return arr;
	}
	
	
	
}
