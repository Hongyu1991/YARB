package com.stylease.entities;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


public class Style {

	private String name;
	//Contains names of all Attributes
	private ArrayList<String> attrStyle;
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name= name;
	}
	
	@RequestMapping(value="/m_list/addStyle/", method=RequestMethod.POST)
	@ResponseBody
	public String controllerMethodStyle(@RequestParam String json) {
	    System.out.println(json);
		return null;
	}
	
	
	/*
	
	public void setAttributes((@RequestParam(value="attrList[]") List<String> attrList){
		attrStyle = (ArrayList<String>) attrList;
	}
	*/
	
}
