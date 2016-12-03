package com.stylease.entities;

import java.util.Date;

public class Message extends IdItem<Long> {

    private String content;
    private int board;
    private int author;
    private Date posted;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public int getBoard() {
		return board;
	}

	public void setBoard(int board) {
		this.board = board;
	}
	
	public int getAuthor() {
		return author;
	}

	public void setAuthor(int author) {
		this.author = author;
	}
	
	public Date getPosted() {
		return this.posted;
	}
		  
	public void setPosted(Date posted) {
		this.posted = posted;
	}
		  
	public void setPosted(java.sql.Date posted) {
		this.posted = new Date(posted.getTime());
	}

}


