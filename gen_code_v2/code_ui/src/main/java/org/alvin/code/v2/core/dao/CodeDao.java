package org.alvin.code.v2.core.dao;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.alvin.code.gen.beans.BaseDao;
import org.alvin.code.gen.utils.SqlUtil;
import org.alvin.code.v2.core.model.CodeCond;
import org.alvin.code.v2.core.model.Field;
import org.alvin.code.v2.core.model.Table;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

	public void executeSql(String sql) {
		jdbcTemplate.execute(sql);
	}


	public SQLExec createSqlExec() {
		SQLExec sqlExec = new SQLExec();
		sqlExec.setDriver(driverClass);
		sqlExec.setUrl(url);
		sqlExec.setUserid(username);
		sqlExec.setPassword(password);
		//如果有出错的语句继续执行.
		sqlExec.setOnerror((SQLExec.OnError) (EnumeratedAttribute.getInstance(SQLExec.OnError.class, "continue")));
//		sqlExec.setPrint(true);
//		sqlExec.setOutput(new File("D:/userinfo.txt"));
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

	/**
	 * 执行脚本文件
	 *
	 * @param sqlFile
	 */
	public void executeSqlFileByAnt(File sqlFile) {
		SQLExec sqlExec = createSqlExec();
		sqlExec.setSrc(sqlFile);
		sqlExec.execute();
	}

}
