package com.stylease.entities;

import java.util.ArrayList;

import com.stormpath.sdk.account.Account;

public class User extends IdItem<Long> {

  private String name;
  private Account account;
  
  private ArrayList<Key> keys;
  
  //public User(String name)
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public Account getAccount() {
    return this.account;
  }
  
  public void setAccount(Account acct) {
    this.account = acct;
  }
}
