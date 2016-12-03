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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class BoardList {
  
  private List<Board> boards;
  
  @Autowired
  private KeyDAO keyDao;
  
  @Autowired
  private UserDAO userDao;
  
  @Autowired
  private BoardDAO boardDao;
  
  @Autowired
  private MessageDAO messageDao;
  
  private void updateMessages(Board b){
	  for(Message m : messageDao.getForBoard(b)) {
		  b.addMessage(m);
	  }
  }

  private List<Board> getBoards(User u) {
	  List<Board> boardList = new ArrayList<>();
	  for(Board b : boardDao.getUserBoards(u)) {
		  updateMessages(b);
	      boardList.add(b);
	  }	  
	  return boardList;
  }
  
  public BoardList() {
	  boards = new ArrayList<>();
  }
  
  @GetMapping("/b_list")
  public String showBoards(User u, ModelMap model) {
	boards = getBoards(u);
    model.addAttribute("boards", boards);
    return "b_list";
  }
  
  @GetMapping("/m_list/{boardId}")
  public String viewAllMessages(HttpServletRequest req, @PathVariable int boardId, ModelMap model) {
    
    Board b = boards.get(boardId - 1);  
    if(b == null && boardId > boards.size()) {
        throw new ResourceNotFoundException();
    }else{
        updateMessages(b);
    }
    
    //User u = getUserFromSession(req);
    //Key perms = keyDao.getBoardPermissions(u, b);
    
    //model.addAttribute("canEdit", (perms.canInvite() || perms.isAdmin()));
    
    model.addAttribute("title", b.getName());
    model.addAttribute("allMessages", b.getMessageList());
    model.addAttribute("board", boardId);
    return "m_list";
  }
  
  @GetMapping("/")
  public String welcomeHome(HttpServletRequest req, ModelMap model) {
	  User u = getUserFromSession(req);
      return showBoards(u, model) ;
  }

  @GetMapping("/m/{boardId}/{messageId}")
  public String viewMessage(@PathVariable int boardId, @PathVariable int messageId, ModelMap model) {
    
    String post = "No message with that ID";
    if(this.boards.size() >= boardId) {
      Board b = boards.get(boardId - 1);
      updateMessages(b);
      post = b.getMessage(messageId).getContent();
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
    	
    if(boards.size() < boardId){
    	throw new ResourceNotFoundException();
    }
    
    User u = getUserFromSession(req);
    Board b = boards.get(boardId - 1);
    updateMessages(b);
    
    Message m = new Message(); 
    m.setContent(message);
    m.setBoard(boardId);
    m.setAuthor(u.getId().intValue());
    b.addMessage(m);
    messageDao.addMessage(m);    
    
    model.addAttribute("allMessages", b.getMessageList());
    model.addAttribute("board", boardId);
    return "m_list";
  }
    
    @GetMapping("/m_list/{boardId}/delete") 
    public String deleteBoard(HttpServletRequest req, @PathVariable int boardId, ModelMap model) {
      Board b = boards.get(boardId - 1);
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
