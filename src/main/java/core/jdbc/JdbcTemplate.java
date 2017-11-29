package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcTemplate {
	private final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

	public void update(String sql, Object... params) {
		try(Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {

			setParameter(pstmt, params);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public <T> List<T> selectList(RowMapper<T> rowMapper, String sql, Object... params) {
		ResultSet rs = null;
		List<T> result = new ArrayList<>();

		try(Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {

			setParameter(pstmt, params);
			rs = pstmt.executeQuery();

			while(rs.next()) {
				result.add(rowMapper.mapRow(rs));
			}

			return result;
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			if(rs != null) {
				try { rs.close(); } catch (SQLException e) { LOGGER.error(e.getMessage(), e); }
			}
		}

		return result;
	}

	public <T> T selectOne(RowMapper<T> rowMapper, String sql, Object... params) {
		List<T> resultList = selectList(rowMapper, sql, params);

		if(resultList == null || resultList.size() == 0) {
			return null;
		}
		return resultList.get(0);
	}

	private void setParameter(PreparedStatement pstmt, Object... params) throws SQLException {
		for(int i = 0 ; i < params.length ; i++) {
			pstmt.setObject(i+1, params[i]);
		}
	}
}