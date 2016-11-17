package com.stylease.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.stylease.Message;

@Controller
public class MessageController {

  private SimpMessagingTemplate template;
  
  @Autowired
  public MessageController(SimpMessagingTemplate template) {
    this.template = template;
  }
  
  @MessageMapping("/board/{boardid}")
  public void handle(String inbound, @DestinationVariable int boardid) {
    System.out.println("Message: " + inbound);
    System.out.println("BoardID: " + boardid);
    this.template.convertAndSend("/topic/board/" + boardid, inbound);
  }
  
}
