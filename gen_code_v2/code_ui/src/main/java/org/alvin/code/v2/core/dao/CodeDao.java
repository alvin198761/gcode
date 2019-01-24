package org.alvin.code.v2.core.dao;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.alvin.code.gen.beans.BaseDao;
import org.alvin.code.gen.utils.SqlUtil;
import org.alvin.code.v2.core.model.CodeCond;
import org.alvin.code.v2.core.model.Field;
import org.alvin.code.v2.core.model.Table;
import org.alvin.code.v2.sys.pro.FieldConfig;
import org.alvin.utils.CMDUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author gzz_gzz@163.com
 * @功能描述:MYSQL数据访问类
 * @date 2018-02-15
 */
@Repository
@Slf4j

@Data
public class CodeDao extends BaseDao {
	public static String DBUSER;// 数据库用户名
	@Value("${spring.datasource.driverClassName}")
	private String driverClass;
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	@Value("${db_name}")
	private String database;
	@Value("${db_ip}")
	private String host;

	/**
	 * @功能描述 系统变量及初始化
	 */
	@PostConstruct
	private void init() {
		DatabaseMetaData dbMetaData;
		try {
			dbMetaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
			ResultSet rs = dbMetaData.getTables(null, null, null, null);
			rs.next();
			DBUSER = rs.getString(1);
		} catch (SQLException e) {
			log.error("获取数据产品名称时出错");
			e.printStackTrace();
		}

	}

	/**
	 * @功能描述: 查询表名列表
	 */
	public List<Table> queryTables(CodeCond cond) {
		StringBuilder sb = new StringBuilder();
		sb.append(
				"SELECT table_name t_name,if(table_comment='',table_name,table_comment) comment FROM information_schema.tables WHERE 1=1");
		sb.append(cond.getCondition());
		log.info(SqlUtil.showSql(sb.toString(), cond.getArray()));
		return jdbcTemplate.query(sb.toString(), cond.getArray(), new BeanPropertyRowMapper<>(Table.class));
	}

	/**
	 * @功能描述: 查询字段名列表
	 */

	public List<Field> queryFields(CodeCond cond) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT COLUMN_NAME NAME,");
		sb.append(" CASE WHEN COLUMN_COMMENT = '' THEN COLUMN_NAME ELSE COLUMN_COMMENT	END COMMENT,");
		sb.append(
				" CASE WHEN DATA_TYPE='varchar' OR DATA_TYPE='text' OR DATA_TYPE='char' OR DATA_TYPE='longtext' OR DATA_TYPE='mediumtext' THEN 'String'");
		sb.append(" WHEN DATA_TYPE = 'tinyint' THEN 'Byte'");
		sb.append(" WHEN DATA_TYPE = 'smallint' THEN 'Short'");
		sb.append(" WHEN DATA_TYPE = 'int' OR DATA_TYPE = 'mediumint' THEN 'Integer'");
		sb.append(
				" WHEN DATA_TYPE = 'datetime' OR DATA_TYPE = 'timestamp' OR DATA_TYPE = 'date' OR DATA_TYPE = 'time' THEN 'Date'");
		sb.append(" WHEN DATA_TYPE = 'bigint' THEN 'Long'");
		sb.append(" WHEN DATA_TYPE = 'float' THEN 'Float'");
		sb.append(" WHEN DATA_TYPE = 'double' THEN 'Double'");
		sb.append(" WHEN DATA_TYPE = 'decimal' THEN 'BigDecimal'");
		sb.append(" WHEN DATA_TYPE = 'boolean' OR DATA_TYPE = 'bit' THEN 'Boolean'");
		sb.append(" ELSE CONCAT ('无效数据类型', DATA_TYPE) END type");
		sb.append(" FROM INFORMATION_SCHEMA.COLUMNS WHERE 1 = 1");
		sb.append(cond.getCondition());
		sb.append(" ORDER BY ORDINAL_POSITION");
		log.info(SqlUtil.showSql(sb.toString(), cond.getArray()));
		return jdbcTemplate.query(sb.toString(), cond.getArray(), new BeanPropertyRowMapper<Field>(Field.class));
	}

	/**
	 * 新的获取属性方法
	 *
	 * @param table_name
	 * @return
	 */
	public List<FieldConfig> queryFieldConfig(String table_name) {
		String sql = "select * from INFORMATION_SCHEMA.Columns where TABLE_NAME = ? and TABLE_SCHEMA = ?  ORDER BY ORDINAL_POSITION ";
		return jdbcTemplate.queryForList(sql, new Object[]{table_name, database}).stream().map(item -> {
			JSONObject jsonObject = new JSONObject(item);
			FieldConfig fieldConfig = new FieldConfig();
			fieldConfig.setName(jsonObject.getString("COLUMN_NAME"));
			fieldConfig.setSql_type(jsonObject.getString("COLUMN_TYPE"));
			String regex = "([^()]+)[(](\\d+)[)]";
			Pattern pattern = Pattern.compile(regex);
			Matcher m = pattern.matcher(fieldConfig.getSql_type());
			if (m.find()) {
				fieldConfig.setType(SqlUtil.typeMap(m.group(1)));
				fieldConfig.setLength(Integer.valueOf(m.group(2)));
				System.out.println(m.group(1) + "----" + SqlUtil.typeMap(m.group(1)));
			} else {
				fieldConfig.setType(SqlUtil.typeMap(fieldConfig.getSql_type()));
//				fieldConfig.setLength(0);
			}
			fieldConfig.setIsNull(jsonObject.getString("IS_NULLABLE").equals("NO") ? "NOT NULL" : "NULL");
			fieldConfig.setRemark(jsonObject.getString("COLUMN_COMMENT"));
			fieldConfig.setIsKey(jsonObject.getString("COLUMN_KEY").equals("PRI"));
			fieldConfig.setExtra(jsonObject.getString("EXTRA"));
			return fieldConfig;
		}).collect(Collectors.toList());
	}

	public SQLExec createSqlExec() {
		SQLExec sqlExec = new SQLExec();
		sqlExec.setDriver(driverClass);
		sqlExec.setUrl(url);
		sqlExec.setUserid(username);
		sqlExec.setPassword(password);
		//如果有出错的语句继续执行.
		sqlExec.setOnerror((SQLExec.OnError) (EnumeratedAttribute.getInstance(SQLExec.OnError.class, "continue")));
		sqlExec.setPrint(true);
		sqlExec.setOutput(new File("sql.log"));
		sqlExec.setProject(new Project());
		return sqlExec;
	}

	/**
	 * 执行脚本
	 *
	 * @param sql
	 */
	public void executeSqlCmdByAnt(String sql) {
		SQLExec sqlExec = createSqlExec();
		sqlExec.addText(sql);
		sqlExec.execute();
	}
//
//	/**
//	 * 执行脚本文件
//	 *
//	 * @param sqlFile
//	 */
//	public void executeSqlFileByAnt(File sqlFile) {
//		SQLExec sqlExec = createSqlExec();
//		sqlExec.setSrc(sqlFile);
//		sqlExec.execute();
//	}


	public String backup(String struts) throws Exception {
		String cmd = "mysqldump  " + struts + " " + database + "  -u" + username + " " + " -p" + password + " -h " + host;
//		String cmd = "mysqldump --databases  -u" + username + " " + " -p" + password + " -h " + host;
		File file = new File("db_backup", UUID.randomUUID().toString());
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		}
		try {
			int res = CMDUtils.execute(cmd + " > " + file.getAbsolutePath(), new StringBuffer(), new StringBuffer());
			if (res == 0) {
				return file.getAbsolutePath();
			}
			throw new Exception("导出出现故障");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public String exportStruts() throws Exception {
		return backup("-d");
	}
}
