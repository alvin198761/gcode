package org.alvin.code.gen.beans;


import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.alvin.code.gen.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gzz
 * @功能描述:dao类公共类
 */
@Scope("prototype")
public abstract class BaseDao {
	@Autowired
	protected JdbcTemplate jdbcTemplate;// jdbc模版类
	@Autowired
	protected NamedParameterJdbcTemplate nameJdbcTemplate;// jdbc模版类

	protected StringBuilder select = new StringBuilder();//原表所有字段
	protected StringBuilder joinSelect = new StringBuilder(); //连接需要的所有字段

	protected <T> Page<T> queryPage(String sql, BaseCondition cond, Class<T> clazz) {
		String countSQL = "SELECT count(1) FROM (" + sql + ") t";// 统计记录个数的SQL语句
		int rowCount = jdbcTemplate.queryForObject(countSQL, cond.getArray(), Integer.class);// 查询记录个数
		int pageSize = cond.getSize();// 页大小
		int curPage = cond.getPage();// 当前页
		String listSql = sql + " LIMIT " + curPage * pageSize + "," + pageSize;// 查询分页数据列表的SQL语句
		List<T> dataList = jdbcTemplate.query(listSql, cond.getArray(), new BeanPropertyRowMapper<T>(clazz));
		return Page.map(dataList, cond.getPage(), cond.getSize(), rowCount);
	}

	protected <T> Page<T> queryPage(String sql, Object [] params , int curPage, int pageSize, Class<T> clazz) {
		String countSQL = "SELECT count(1) FROM (" + sql + ") t";// 统计记录个数的SQL语句
		int rowCount = jdbcTemplate.queryForObject(countSQL, params, Integer.class);// 查询记录个数
		String listSql = sql + " LIMIT " + curPage * pageSize + "," + pageSize;// 查询分页数据列表的SQL语句
		List<T> dataList = jdbcTemplate.query(listSql, params, new BeanPropertyRowMapper<T>(clazz));
		return Page.map(dataList, curPage, pageSize, rowCount);
	}

	protected <T> int[] batchOperate(List<T> list, String sql) {
		SqlParameterSource[] params = new SqlParameterSource[list.size()];
		for (int i = 0; i < list.size(); i++) {
			params[i] = new BeanPropertySqlParameterSource(list.get(i));
		}
		return nameJdbcTemplate.batchUpdate(sql, params);
	}

	protected <T> long saveKey(T t, String sql, String id) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource params = new BeanPropertySqlParameterSource(t);
		nameJdbcTemplate.update(sql, params, keyHolder, new String[]{id});
		return keyHolder.getKey().longValue();
	}

	/**
	 * @方法说明：查询参数定制
	 */
	public String getSelectedItems(BaseCondition cond) {
		//其他情况都查所有
		if (cond == null
				|| cond.getSelectedFields() == null
				|| cond.getSelectedFields().isEmpty()
				|| cond.getQueryMode() == BaseCondition.QUERY_MODE_JOIN_ALL) {
			return select.toString().concat(joinSelect.toString()); //默认所有字段
		}
		//只查单表
		if (cond.getQueryMode() == BaseCondition.QUERY_MODE_SINGLE_ALL) {
			return select.toString();
		}
		//根据提交的字段选查
		return Joiner.on(",").join(cond.getSelectedFields());
	}

	/**
	 * @return
	 * @方法说明：表连接代码
	 */
	public String getJoinTables(BaseCondition cond) {
		//没有或者全连
		if (cond == null
				|| cond.getQueryMode() == BaseCondition.QUERY_MODE_JOIN_ALL
				|| cond.getQueryMode() == BaseCondition.QUERY_MODE_JOIN_ALL_QUERY_SELECT) {
			return this.joinTables();
		}
		//选连选查
		if (cond.getQueryMode() == BaseCondition.QUERY_MODE_JOIN_SELECT_QUERY_SELECT) {
			return cond.getJoinTables().toString();
		}
		//其他条件只查本表
		return "";
	}

	/**
	 * 根据 id,列 来更新
	 *
	 * @param cols
	 * @param vo
	 * @param <T>
	 * @return
	 */
	public <T> int update(List<String> cols, T vo, String tableName, String idName) {
		if (cols == null || cols.isEmpty()) {
			return 0;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE " + tableName + " SET " + Joiner.on(",").join(cols));
		sql.append(" WHERE " + idName + "=? ");
		//组装所有的更新字段
		return jdbcTemplate.update(sql.toString(), getParams(vo, cols));
	}

	/**
	 * 更新非空字段(只针对ID)
	 *
	 * @param vo
	 * @param <T>
	 * @return
	 */
	public <T> int updateFieldNotNull(T vo, String tableName, String idName) {
		List<Field> fields = Lists.newArrayList(vo.getClass().getFields());
		List<String> cols = fields.stream().filter(field ->
				{
					try {
						return field.get(vo) != null;
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					return false;
				}
		).map(Field::getName).collect(Collectors.toList());
		return update(cols, vo, tableName, idName);
	}

	/**
	 * 根据条件更新
	 *
	 * @param condition
	 * @return
	 */
	public int updateAllByCondition(BaseCondition condition, String tableName, List<String> fields) {
		if (Strings.isNullOrEmpty(condition.getCondition())) {
			return 0;
		}
		if (condition.getCondition().trim().isEmpty()) {
			return 0;
		}
		String cols = Joiner.on(" = ? ,").join(fields);
		cols = cols.substring(0, cols.length() - 1); //去掉最后一个逗号
		String sql = "UPDATE " + tableName + " SET " + cols + " WHERE " + condition.getCondition();
		return jdbcTemplate.update(sql, condition.getArray());
	}

	/**
	 * 根据条件删除
	 *
	 * @param condition
	 * @param tableName
	 * @return
	 */
	public int deleteByCondition(BaseCondition condition, String tableName) {
		if (Strings.isNullOrEmpty(condition.getCondition())) {
			return 0;
		}
		if (condition.getCondition().trim().isEmpty()) {
			return 0;
		}
		String sql = "DELETE FROM " + tableName + " WHERE " + condition.getCondition();
		return jdbcTemplate.update(sql, condition.getArray());
	}

	/**
	 * 获取属性值
	 *
	 * @param vo
	 * @param fields
	 * @param <T>
	 * @return
	 */
	private <T> Object[] getParams(T vo, List<String> fields) {
		return fields.stream().map(item -> {
			try {
				Field field = vo.getClass().getField(item);
				return field.get(vo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList()).toArray();
	}

	/**
	 * 实现自定义的表连接语句
	 *
	 * @return
	 */
	protected String joinTables() {
		return "";
	}

}