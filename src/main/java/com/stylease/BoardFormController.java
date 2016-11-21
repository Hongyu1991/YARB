package com.stylease;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.servlet.account.AccountResolver;
import com.stylease.entities.Board;
import com.stylease.entities.Key;
import com.stylease.entities.User;
import com.stylease.repos.KeyDAO;
import com.stylease.repos.UserDAO;

@Controller
public class BoardFormController {

  private static final String USEROP_ADD = "useradd";
  private static final String USEROP_MOD = "usermod";
  private static final String USEROP_REM = "userrem";
  
  @Autowired
  private KeyDAO keyDao;
  
  @Autowired
  private UserDAO userDao;
  
  @GetMapping("/b_add")
  public String addBoardForm(HttpServletRequest req, ModelMap model) {
    //model.addAttribute("board", boardId);
    Board b = new Board();
    b.setKeys(new LinkedList<>());
    
    HttpSession sesh = req.getSession();
    sesh.setAttribute("newboard", b);
    
    User u = getUserFromSession(req);
    Key ownerKey = new Key();
    ownerKey.setPermission(Key.CAN_READ, true);
    ownerKey.setPermission(Key.CAN_WRITE, true);
    ownerKey.setPermission(Key.INVITE_USERS, true);
    ownerKey.setPermission(Key.ADMINISTER, true);
    u.addKey(ownerKey);
    b.addKey(ownerKey);
    
    sesh.setAttribute("usertbl", new HashMap<Long, User>());
    sesh.setAttribute("userkeys", new HashMap<Long, Key>());
    
    setCreateModel(model);
    model.addAttribute("usertbl", new HashMap<Long, User>());
    model.addAttribute("board", b);
    return "b_form";
  }
  
  @PostMapping(path = "/b_add", params = "saveboard")
  public String addBoardSubmit(ModelMap model) {
    setCreateModel(model);
    
    System.out.println("saveboard");
    
    return "b_form";
  }
  
  @PostMapping(path = "/b_add", params = "userop")
  public String userBoardMod(HttpServletRequest req, 
      @RequestParam("userop") String op,
      @RequestParam("boardName") String boardName,
      ModelMap model) {
    
    User u = null;
    HttpSession sesh = req.getSession(false);
    
    HashMap<Long, User> userTbl = (HashMap<Long, User>)sesh.getAttribute("usertbl");
    Board board = (Board)sesh.getAttribute("newboard");
    HashMap<Long, Key> userKeys = (HashMap<Long, Key>)sesh.getAttribute("userkeys");
    
    switch(op) {
    case USEROP_ADD:
      String username = req.getParameter("user");
      u = userDao.getUserForName(username);
      if(u == null) {
       model.addAttribute("errors", new String[]{"The user " + username + " does not exist."});
       break;
      }
      
      userTbl.put(u.getId(), u);
      break;
    case USEROP_MOD:
      try {
        long uid = Long.parseLong(req.getParameter("users"));
        u = userTbl.get(uid);
      }
      catch(NumberFormatException ex) {}
      break;
    case USEROP_REM:
      try {
        long uid = Long.parseLong(req.getParameter("users"));
        userTbl.remove(uid);
        userKeys.remove(uid);
      }
      catch(NumberFormatException ex) {}
      break;
    }
    
    if(u != null) {
    
      Key k = userKeys.get(u.getId());
      if(k == null) {
        k = new Key();
        userKeys.put(u.getId(), k);
      }
      
      boolean canRead = req.getParameter("can_read") != null;
      boolean canWrite = req.getParameter("can_write") != null;
      boolean canInvite = req.getParameter("invite_users") != null;
      boolean administer = req.getParameter("administer") != null;
      
      k.setPermission(Key.CAN_READ, canRead);
      k.setPermission(Key.CAN_WRITE, canWrite);
      k.setPermission(Key.INVITE_USERS, canInvite);
      k.setPermission(Key.ADMINISTER, administer);
    }
    
    setCreateModel(model);
    model.addAttribute("usertbl", userTbl);
    model.addAttribute("userkeys", userKeys);
    //System.out.println(board.getName());
    /*System.out.println(canRead);
    System.out.println(canWrite);
    System.out.println(canInvite);
    System.out.println(administer);*/
    board.setName(boardName);
    model.addAttribute("board", board);
    System.out.println("Op: " + op);
    
    return "b_form";
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
  
  private void setCreateModel(ModelMap model) {
    model.addAttribute("board_action", "Create");
    model.addAttribute("submit_action", "Create");
    model.addAttribute("userop_add", USEROP_ADD);
    model.addAttribute("userop_mod", USEROP_MOD);
    model.addAttribute("userop_rem", USEROP_REM);
  }
  
}
