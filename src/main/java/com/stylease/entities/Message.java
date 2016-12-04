package com.stylease.entities;

import java.util.ArrayList;

public class Message {
    int id;
    String text;

    private ArrayList<Attribute> effectedAttr;
    //could instead have
    //private Style style;
    
    public ArrayList<Attribute> getEffectedAttr(){
    	return effectedAttr;
    }
    
    public Attribute addAttribute(Attribute a){
    	effectedAttr.add(a);
		return a;
    }
    
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public Message(int id, String text) {
        this.id = id;
		this.text = text;
	}

	public Message() {
        this.id = 0;
        this.text = "";
	}
}
