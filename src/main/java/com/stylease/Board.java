package com.stylease;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.servlet.account.AccountResolver;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
   

@Controller
public class Board {
    public ArrayList<Message> messages;
    String name;
    
    int id;
    
    public Board() {
        messages = new ArrayList<>();
    }
    
    public String getName() {
      return name;
    }
   
    public int getId() {
      return id;
    }
  
    public void setId(int id) {
        this.id = id;
    }
/*
    public ArrayList<Message> stringListToMessageList() {
        ArrayList<Message> messageList = new ArrayList<Message>(this.messages.size());
        for(int i = 0; i < this.messages.size(); i++) {
            messageList.add(new Message(i, this.messages.get(i)));
        }
        return messageList;
    }
*/    
    public void addMessage(Message m, int i) {
      m.id = i;
      this.messages.add(i, m);
      System.out.println("Added message " + m.id);
    }

}
