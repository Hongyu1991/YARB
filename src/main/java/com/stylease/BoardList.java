package com.stylease;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.servlet.account.AccountResolver;
import com.stylease.entities.Key;
import com.stylease.entities.Message;
import com.stylease.entities.User;
import com.stylease.repos.BoardDAO;
import com.stylease.repos.KeyDAO;
import com.stylease.repos.MessageDAO;
import com.stylease.repos.UserDAO;
import com.stylease.entities.Board;

import java.io.IOException;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class BoardList {
  
  private ArrayList<Board> boards;
  
  @Autowired
  private KeyDAO keyDao;
  
  @Autowired
  private UserDAO userDao;
  
  @Autowired
  private BoardDAO boardDao;
  
  @Autowired
  private MessageDAO messageDao;

  private ArrayList<Board> getBoards(User u) {
	  ArrayList<Board> boardList = new ArrayList<>();
	  for(Board b : boardDao.getUserBoards(u)) {
	    	b.setMessages(messageDao.getForBoard(b));
	    	boardList.add(b);
	  }	  
	  return boardList;
  }
  
  public BoardList() {
	  boards = new ArrayList<>();
  }
  
  @GetMapping("/b_list")
  public String showBoards(HttpServletRequest req, ModelMap model) {
	User u = getUserFromSession(req);
	boards = getBoards(u);
    model.addAttribute("boards", boards);
    return "b_list";
  }
  
  @GetMapping("/m_list/{boardId}")
  public String viewAllMessages(HttpServletRequest req, @PathVariable int boardId, ModelMap model) {
    
    Board b = boardDao.getForId(boardId);
    
    if(b == null) {
      if(boardId >= boards.size()) {
        throw new ResourceNotFoundException();
      }
      
      b = boards.get(boardId);
    }
    
    User u = getUserFromSession(req);
    Key perms = keyDao.getBoardPermissions(u, b);
    
    model.addAttribute("canEdit", (perms.canInvite() || perms.isAdmin()));
    model.addAttribute("title", b.getName());
    model.addAttribute("allMessages", b.getMessages());
    model.addAttribute("board", boardId);
    return "m_list";
  }
  
  @GetMapping("/")
  public String welcomeHome(HttpServletRequest req, ModelMap model) {
      Account account = AccountResolver.INSTANCE.getAccount(req);
      if (account != null) {
          model.addAttribute(account);
          User u = userDao.getUserForStormpathAccount(account);
          if(u == null) {
            u = new User();
            u.setAccount(account);
            
            ArrayList<Key> keys = new ArrayList<>(1);
            keys.add(keyDao.getPublicKey());
            u.setKeys(keys);
            
            userDao.addUser(u);
          }
          
          req.getSession().setAttribute("user", u);
      }
    
    //return "home";
      return showBoards(req, model) ;
  }

  @GetMapping("/m/{boardId}/{messageId}")
  public String viewMessage(@PathVariable int boardId, @PathVariable int messageId, ModelMap model) {
    
    String post = "No message with that ID.";
    if(this.boards.size() > boardId) {
      Board b = boards.get(boardId);
      if (b.getMessages().size() > messageId) {
          post = b.getMessages().get(messageId).getContent();
      }
      
      System.out.println(messageId);
      /*for(int i = 0; i < b.messages.size(); i++) {
        System.out.println(messages.get(i).id);
      }*/
    }
        
    model.addAttribute("messageText", post);
    model.addAttribute("board", boardId);
    return "m";
  }
  
    @GetMapping("/m_form/{boardId}")
    public String addMessageForm(@PathVariable int boardId, ModelMap model) {
      model.addAttribute("board", boardId);
        return "m_form";
    }
  
    @PostMapping("/b/{boardId}/add")
  public String addMessage(HttpServletRequest req, @PathVariable int boardId, 
		  @RequestParam("message") String message, ModelMap model) {
    	
    if(boards.size() <= boardId){
    	throw new ResourceNotFoundException();
    }
    
    Board b = boards.get(boardId);    
    User u = userDao.getUserForStormpathAccount(AccountResolver.INSTANCE.getAccount(req));
    Message m = new Message(); 
    m.setContent(message);
    m.setBoard(boardId);
    m.setAuthor(u.getId().intValue());
    b.addMessage(m, b.getSize());
    model.addAttribute("allMessages", b.getMessages());
    model.addAttribute("board", boardId);
    return "m_list";
  }
    
    @GetMapping("/m_list/{boardId}/delete") 
    public String deleteBoard(HttpServletRequest req, @PathVariable int boardId, ModelMap model) {
      Board b = boardDao.getForId(boardId);
      if(b == null) {
        throw new ResourceNotFoundException();
      }
      
      User u = getUserFromSession(req);
      
      Key perms = keyDao.getBoardPermissions(u, b);
      if(!perms.isAdmin()) {
        throw new ResourceForbiddenException();
      }
      
      model.addAttribute("boardName", b.getName());
      String referrer = req.getHeader("Referer");
      if(referrer == null) {
        referrer = "/";
      }
      
      model.addAttribute("nolink", referrer);
      
      return "b_del";
    }
    
    @PostMapping(path = "/b/{boardId}/delete", params = "del")
    public String deleteBoard(HttpServletRequest req, HttpServletResponse resp, @PathVariable int boardId, ModelMap model) {
      
      Board b = boardDao.getForId(boardId);
      if(b == null) {
        throw new ResourceNotFoundException();
      }
      
      User u = getUserFromSession(req);
      
      Key perms = keyDao.getBoardPermissions(u, b);
      if(!perms.isAdmin()) {
        throw new ResourceForbiddenException();
      }
      
      boardDao.deleteBoard(b);
      try {
        resp.sendRedirect("/b_list");
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        model.addAttribute("errors", new String[]{"Board deleted; error redirecting"});
      }
      
      return "b_del";
    }
    
    private User getUserFromSession(HttpServletRequest r) {
      Object o = r.getSession().getAttribute("user");
      if(o == null) {
        Account account = AccountResolver.INSTANCE.getAccount(r);
        if (account != null) {
          
          User u = userDao.getUserForStormpathAccount(account);
          if(u == null) {
            u = new User();
            u.setAccount(account);
            
            ArrayList<Key> keys = new ArrayList<>(1);
            keys.add(keyDao.getPublicKey());
            u.setKeys(keys);
            
            userDao.addUser(u);
          }
          
          r.getSession().setAttribute("user", u);
          
          return u;
        }
      }
      
      return (User)o;
    }
}
