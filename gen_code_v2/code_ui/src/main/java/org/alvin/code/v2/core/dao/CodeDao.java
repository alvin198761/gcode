package org.alvin.code.v2.core.dao;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.alvin.code.gen.beans.BaseDao;
import org.alvin.code.gen.utils.SqlUtil;
import org.alvin.code.v2.core.doc.DocField;
import org.alvin.code.v2.core.doc.DocTable;
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
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
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
//		DBUSER = this.username;
        DatabaseMetaData dbMetaData;
        try {
            dbMetaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
            ResultSet rs = dbMetaData.getTables(null, null, null, null);
            if (rs.getFetchSize() == 0) {
                return;
            }
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
                " CASE WHEN DATA_TYPE='varchar' OR DATA_TYPE='text' OR DATA_TYPE='char' OR DATA_TYPE='longtext' OR DATA_TYPE='mediumtext' THEN 'java.lang.String'");
        sb.append(" WHEN DATA_TYPE = 'tinyint' THEN 'java.lang.Byte'");
        sb.append(" WHEN DATA_TYPE = 'smallint' THEN 'java.lang.Short'");
        sb.append(" WHEN DATA_TYPE = 'int' OR DATA_TYPE = 'mediumint' THEN 'java.lang.Integer'");
        sb.append(
                " WHEN DATA_TYPE = 'datetime' OR DATA_TYPE = 'timestamp' OR DATA_TYPE = 'date' OR DATA_TYPE = 'time' THEN 'java.util.Date'");
        sb.append(" WHEN DATA_TYPE = 'bigint' THEN 'java.lang.Long'");
        sb.append(" WHEN DATA_TYPE = 'float' THEN 'java.lang.Float'");
        sb.append(" WHEN DATA_TYPE = 'double' THEN 'java.lang.Double'");
        sb.append(" WHEN DATA_TYPE = 'decimal' THEN 'java.math.BigDecimal'");
        sb.append(" WHEN DATA_TYPE = 'boolean' OR DATA_TYPE = 'bit' THEN 'java.lang.Boolean'");
        sb.append(" ELSE CONCAT ('无效数据类型', DATA_TYPE) END allTypeName");
        sb.append(" ,character_maximum_length as length ");
        sb.append(" ,column_key as column_key ");
        sb.append(" ,upper(DATA_TYPE) as dbType ");
        sb.append(" FROM INFORMATION_SCHEMA.COLUMNS WHERE 1 = 1");
        sb.append(cond.getCondition());
        sb.append(" ORDER BY ORDINAL_POSITION");
        log.info(SqlUtil.showSql(sb.toString(), cond.getArray()));
        List<Field> fields = jdbcTemplate.query(sb.toString(), cond.getArray(), new BeanPropertyRowMapper<Field>(Field.class));
        //查询出外键关系

        return fields;
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
            fieldConfig.setSqlType(jsonObject.getString("COLUMN_TYPE"));
            String regex = "([^()]+)[(](\\d+)[)]";
            Pattern pattern = Pattern.compile(regex);
            Matcher m = pattern.matcher(fieldConfig.getSqlType());
            if (m.find()) {
                fieldConfig.setType(SqlUtil.typeMap(m.group(1)));
                fieldConfig.setLength(Integer.valueOf(m.group(2)));
                System.out.println(m.group(1) + "----" + SqlUtil.typeMap(m.group(1)));
            } else {
                fieldConfig.setType(SqlUtil.typeMap(fieldConfig.getSqlType()));
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


    public void getFKList(String database, String table) {
//        SELECT
//        ic.*,
//                ii.*
//                        FROM
//        information_schema.`COLUMNS` AS ic
//        LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS ii ON ii.COLUMN_NAME = ic.COLUMN_NAME
//        WHERE
//        ii.`CONSTRAINT_SCHEMA` = 'mj_lng_calc'
//        AND ii.`TABLE_NAME` = 'calc_car_mileage'
//        AND ii.`REFERENCED_COLUMN_NAME` != 'null'
//        and ic.TABLE_SCHEMA='mj_lng_calc'
//        and ic.TABLE_NAME= 'calc_car_mileage';
    }

    public void getPKList(){
//        SELECT
//        k.column_name,
//                t.table_name,
//                table_schema
//        FROM
//        information_schema.table_constraints t
//        JOIN information_schema.key_column_usage k USING (
//                constraint_name,
//                table_schema,
//                table_name
//        )
//        WHERE
//        t.constraint_type = 'PRIMARY KEY'
//        AND t.table_schema = 'mj_lng_calc'
//        AND t.table_name = 'calc_car_mileage'
    }

    public List<DocField> queryFields(String dbName ,String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT  column_name, column_comment, column_type, is_nullable,");
        sb.append(" IF (column_key = 'pri', '是', '')  pri ");
        sb.append(" FROM");
        sb.append(" information_schema.columns");
        sb.append(" WHERE table_schema = '"+dbName+"' AND table_name = ?");
        return jdbcTemplate.query(sb.toString(), new Object[] { tableName }, new BeanPropertyRowMapper<>(DocField.class));
    }

    public List<DocTable> queryTables(String dbName) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT table_name  ,table_comment FROM information_schema.TABLES WHERE table_schema = '"+dbName+"'");
        return jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<>(DocTable.class));
    }
}
