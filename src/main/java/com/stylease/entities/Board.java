package com.stylease.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Board extends IdItem<Long> {

  private String name;
  private Date created;
  private boolean enabled;
  private HashMap<Long, Message> messages;
  private List<Key> keys;
  
  public Board() {
	  messages = new HashMap<Long, Message>();
	  keys = new ArrayList<Key>();
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public Date getCreated() {
    return this.created;
  }
  
  public void setCreated(Date created) {
    this.created = created;
  }
  
  public void setCreated(java.sql.Date created) {
    this.created = new Date(created.getTime());
  }
  
  public boolean getEnabled() {
    return this.enabled;
  }
  
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
  
  public int getSize() {
	  return this.messages.size();
  }
  
  public List<Message> getMessageList() {
    return new ArrayList<Message>(messages.values());
  }
  
  public Message getMessage(int i) {
    return this.messages.get(Long.valueOf(i));
  }
  
  public List<Key> getKeys() {
    return this.keys;
  }
  
  public void setKeys(List<Key> keys) {
    this.keys = keys;
  }
  
  public void addMessage(Message m) {
    messages.put(m.getId(),m);
  }
  
  public void addKey(Key k) {
    keys.add(k);
  }
}
