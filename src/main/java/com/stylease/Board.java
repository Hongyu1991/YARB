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
    private ArrayList<String> messages;
    
    public Board() {
        messages = new ArrayList<String>();
    }
   
    
    @GetMapping("/")
    public String welcomeHome(HttpServletRequest req, ModelMap model) {
        Account account = AccountResolver.INSTANCE.getAccount(req);
        if (account != null) {
            model.addAttribute(account);
        }
    	
    	return "home";
    }

	@GetMapping("/m/{messageId}")
	public String viewMessage(@PathVariable int messageId, ModelMap model) {
		String post = "No message with that ID.";
        if (this.messages.size() > messageId) {
            post = this.messages.get(messageId);
        }
		model.addAttribute("messageText", post);
		return "m";
	}

    @GetMapping("/m_list")
	public String viewAllMessages(ModelMap model) {
		model.addAttribute("allMessages", stringListToMessageList());
		return "m_list";
	}

    @GetMapping("/m_form")
    public String addMessageForm(ModelMap model) {
        return "m_form";
    }

    @PostMapping("/add")
	public String addMessage(@RequestParam("message") String message, ModelMap model) {
        this.messages.add(message);
		model.addAttribute("allMessages", stringListToMessageList());
		return "m_list";
	}

    private ArrayList<Message> stringListToMessageList() {
        ArrayList<Message> messageList = new ArrayList<Message>(this.messages.size());
        for(int i = 0; i < this.messages.size(); i++) {
            messageList.add(new Message(i, this.messages.get(i)));
        }
        return messageList;
    }

}
