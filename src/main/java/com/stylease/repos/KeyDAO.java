package com.stylease.repos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.stylease.entities.Board;
import com.stylease.entities.Key;
import com.stylease.entities.User;

@Repository
public class KeyDAO extends AbstractIdDAO<Key> {
  
  private static final String KEYS_FOR_USER_SQL =
      "SELECT k.* FROM app_key k"
      + " INNER JOIN user_keys uk ON k.id = uk.keyid"
      + " INNER JOIN app_user u ON uk.userid = u.id"
      + " WHERE u.id = ?";
  
  private static final String KEYS_FOR_BOARD_SQL =
      "SELECT k.* FROM app_key k"
      + " INNER JOIN board_keys bk ON k.id = bk.keyid"
      + " INNER JOIN board b ON bk.boardid = b.id"
      + " WHERE b.id = ?";
  
  public KeyDAO() {
    super("app_key", "id");
  }
  
  public Key getForId(long id) {
    return super.getForId(id, new KeyRowMapper());
  }
  
  public List<Key> getForUser(User u) {
    return this.jdbcTemplate.query(KEYS_FOR_USER_SQL, new Object[]{u.getId()}, new KeyRowMapper());
  }
  
  public List<Key> getForBoard(Board b) {
    return this.jdbcTemplate.query(KEYS_FOR_BOARD_SQL, new Object[]{b.getId()}, new KeyRowMapper());
  }
  
  public class KeyRowMapper implements RowMapper<Key> {
    
    @Override
    public Key mapRow(ResultSet rs, int rowNum) throws SQLException {
      // TODO Auto-generated method stub
      Key key = new Key();
      key.setId(rs.getLong("id"));
      key.setPermission(Key.CAN_READ, rs.getBoolean("can_read"));
      key.setPermission(Key.CAN_WRITE, rs.getBoolean("can_write"));
      key.setPermission(Key.INVITE_USERS, rs.getBoolean("invite_users"));
      key.setPermission(Key.ADMINISTER, rs.getBoolean("administer"));
      
      return key;
    }
    
  }
  
}
