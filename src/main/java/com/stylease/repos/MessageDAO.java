package com.stylease.repos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.stylease.entities.Board;
import com.stylease.entities.Key;
import com.stylease.entities.Message;
import com.stylease.entities.User;
import com.stylease.repos.BoardDAO.BoardRowMapper;
import com.stylease.repos.KeyDAO.KeyRowMapper;

@Repository
public class MessageDAO extends AbstractIdDAO<Message> {

	@Autowired
	private BoardDAO boardDao;

	private SimpleJdbcInsert messageAdder;
	
	private static final String MSG_FOR_USER_SQL = "SELECT m.* FROM messages m WHERE author=?";
	
	private static final String MSG_FOR_BOARD_SQL = "SELECT m.* FROM messages m WHERE board=?";

	private static final String UPDATE_MSG_SQL = "UPDATE messages SET" + " content = ?" + " WHERE id = ?";

	private static final String DEL_MSG_SQL = "DELETE FROM messages WHERE id = ?";

	@Autowired
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);

		messageAdder = new SimpleJdbcInsert(dataSource).withTableName("messages")
				.usingColumns("content", "board", "author").usingGeneratedKeyColumns("id");
	}

	protected MessageDAO() {
		super("messages", "id");
	}

	public void addMessage(Message m) {
		HashMap<String, Object> args = new HashMap<>();
		args.put("content", m.getContent());
		args.put("board", m.getBoard());
		args.put("author", m.getAuthor());

		Number id = messageAdder.executeAndReturnKey(args);
		m.setId(id.longValue());
	}

	public Message getForId(long id) {
		Message m = super.getForId(id, new MessageRowMapper());
		if (m == null) {
			return null;
		}

		return m;
	}
	
	public List<Message> getForBoard(Board b) {
		return this.jdbcTemplate.query(MSG_FOR_BOARD_SQL, new Object[]{b.getId()}, new MessageRowMapper());
	}
	
	public List<Message> getForUser(User u) {
		return this.jdbcTemplate.query(MSG_FOR_USER_SQL, new Object[]{u.getId()}, new MessageRowMapper());
	}

	public int updateMessage(Message m) {
		return this.jdbcTemplate.update(UPDATE_MSG_SQL, m.getContent(), m.getId());
	}

	public int deleteMessage(Message m) {
		return this.jdbcTemplate.update(DEL_MSG_SQL, m.getId());
	}

	public class MessageRowMapper implements RowMapper<Message> {
		@Override
		public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
			Message m = new Message();

			m.setId(rs.getLong("id"));
			m.setContent(rs.getString("content"));
			m.setPosted(rs.getDate("posted"));
			m.setBoard((int) rs.getLong("board"));
			m.setAuthor((int) rs.getLong("author"));

			return m;
		}
	}
}