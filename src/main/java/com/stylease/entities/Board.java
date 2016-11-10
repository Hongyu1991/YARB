package com.stylease.entities;

import java.util.Date;

public class Board extends IdItem<Long> {

  private String name;
  private Date created;
  private boolean enabled;
  
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
  
  public boolean getEnabled() {
    return this.enabled;
  }
  
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
