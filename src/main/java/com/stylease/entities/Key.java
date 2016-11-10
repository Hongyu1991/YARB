package com.stylease.entities;

public class Key extends IdItem<Long> {

  public static int CAN_READ = 0;
  public static int CAN_WRITE = 1;
  public static int INVITE_USERS = 2;
  public static int ADMINISTER = 3;
  
  private boolean[] perms = {false, false, false, false};;
  
  public boolean getPermission(int perm) {
    if(perm > perms.length) {
      return false;
    }
    
    return perms[perm];
  }
  
  public void setPermission(int perm, boolean value) {
    perms[perm] = value;
  }
}
