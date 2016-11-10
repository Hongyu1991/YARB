package com.stylease.repos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.stormpath.sdk.account.Account;
import com.stylease.entities.User;
import com.stylease.repos.KeyDAO.KeyRowMapper;

@Repository
public class UserDAO extends AbstractIdDAO<User> {

  private static final String USER_FOR_NAME_SQL =
      "SELECT * FROM app_user WHERE stormpath_username = ?";
  
  public UserDAO() {
    super("app_user", "id");
  }
  
  public User getUserForName(String name) {
    List<User> lUser = this.jdbcTemplate.query(USER_FOR_NAME_SQL, new Object[]{name}, new UserRowMapper());
    
    if(lUser.size() == 0) {
      return null;
    }
    
    return lUser.get(0);
  }
  
  public User getUserForStormpathAccount(Account acct) {
    User u = getUserForName(acct.getUsername());
    if(u == null) {
      return null;
    }
    u.setAccount(acct);
    return u;
  }
  
  public class UserRowMapper implements RowMapper<User> {
    
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
      // TODO Auto-generated method stub
      User user = new User();
      user.setId(rs.getLong("id"));
      user.setName(rs.getString("stormpath_username"));
      
      return user;
    }
    
  }
}
