package org.alvin.home.v3.code.system.gencode;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.alvin.home.v3.code.beans.FKFieldBean;
import org.alvin.home.v3.code.beans.FieldBean;
import org.alvin.home.v3.code.beans.TableBean;
import org.alvin.home.v3.code.system.doc.DocField;
import org.alvin.home.v3.code.system.doc.DocTable;
import org.alvin.home.v3.code.system.gencoderef.AlvinGenCodeRef;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class GenCodeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Value("${db_name}")
    private String dbName;
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    /**
     * 根据表名称和数据库获取表信息
     *
     * @param table
     * @return
     */
    public TableBean getTable(String table) {
        return null;
    }

    /**
     * 获取表的集合
     *
     * @param tables
     * @return
     */
    public List<TableBean> getTable(List<String> tables) {
        return null;
    }

    private String fieldsCommonSql() {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT TABLE_NAME as classVarName, COLUMN_NAME NAME,");
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
        return sb.toString();
    }

    /**
     * 获取表的属性
     *
     * @param table
     * @return
     */
    public List<FieldBean> getFields(String table) {
        StringBuilder sb = new StringBuilder(fieldsCommonSql());
        sb.append(" AND TABLE_NAME = ? AND table_schema = ? ");
        sb.append(" ORDER BY ORDINAL_POSITION");
        log.info(sb.toString());
        return jdbcTemplate.query(sb.toString(), new Object[]{table, dbName}, new BeanPropertyRowMapper<>(FieldBean.class));
    }

    /**
     * 外键属性对象
     *
     * @param refs
     * @return
     */
    public List<FieldBean> getRefFields(List<AlvinGenCodeRef> refs) {
        StringBuilder sb = new StringBuilder(fieldsCommonSql());
        sb.append(" AND table_schema = ? ");
        if (!refs.isEmpty()) {
            sb.append(" AND (");
            String cond = refs.parallelStream().map(item -> {
                return "( table_name = '" + item.getRefTableName() + "' AND column_name = '" + item.getRefColName() + "' )";
            }).collect(Collectors.joining(" OR "));
            sb.append(cond);
            sb.append(" ) ");
            log.info(sb.toString());
            return jdbcTemplate.query(sb.toString(), new Object[]{dbName}, new BeanPropertyRowMapper<>(FieldBean.class));
        }else{
            return Lists.newArrayList();
        }
    }

    /**
     * 获取主键
     *
     * @param table
     * @return
     */
    public List<String> getPrimaryKey(String table) {
        String sql = "SELECT column_name FROM INFORMATION_SCHEMA.`KEY_COLUMN_USAGE` WHERE table_name = ? AND CONSTRAINT_SCHEMA = ? AND constraint_name = 'PRIMARY'";
        List<Map<String, Object>> res = jdbcTemplate.queryForList(sql, new Object[]{table, dbName});
        return res.parallelStream().map(item -> item.get("column_name").toString()).collect(Collectors.toList());
    }

    /**
     * 获取外键
     *
     * @param table
     * @return
     */
    public List<FKFieldBean> getFKs(String table) {
        String sql = "SELECT * FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE CONSTRAINT_SCHEMA = ? AND REFERENCED_TABLE_NAME = ?";
        return jdbcTemplate.query(sql, new Object[]{table, dbName}, new BeanPropertyRowMapper<>(FKFieldBean.class));
    }


    /**
     * @功能描述: 查询表名列表
     */
    public List<TableBean> queryTables() {
        String sql = "SELECT table_name tableName,if(table_comment='',table_name,table_comment) comment FROM information_schema.tables WHERE table_schema=? AND TABLE_NAME NOT LIKE 't_alvin_gen_code_%'";
        return jdbcTemplate.query(sql, new Object[]{dbName}, new BeanPropertyRowMapper<>(TableBean.class));
    }

    /**
     * @功能描述: 查询表名列表
     */
    public List<RefTableDto> queryRefTables(String tableName) {
        String sql = "select t.table_name ,GROUP_CONCAT( t.column_name) as cols  FROM INFORMATION_SCHEMA.COLUMNS t where table_schema=? AND table_name <> ? AND TABLE_NAME NOT LIKE 't_alvin_gen_code_%' GROUP BY t.table_name ";
        return jdbcTemplate.query(sql, new Object[]{dbName, tableName}, new BeanPropertyRowMapper<>(RefTableDto.class));
    }

    /**
     * 查询表的列
     *
     * @param tableName
     * @return
     */
    public List<AlvinGenCodeRef> getRefFields(String tableName) {
        String sql = "SELECT t.table_name as tableName, t.column_name as colName FROM INFORMATION_SCHEMA. COLUMNS t WHERE t.table_schema = ? AND t.table_name = ?";
        return jdbcTemplate.query(sql, new Object[]{dbName, tableName}, new BeanPropertyRowMapper<>(AlvinGenCodeRef.class));
    }


    public SQLExec createSqlExec() {
        SQLExec sqlExec = new SQLExec();
        sqlExec.setDriver(this.driverClassName);
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


//    public String backup(String struts) throws Exception {
//        String cmd = "mysqldump  " + struts + " " + database + "  -u" + username + " " + " -p" + password + " -h " + host;
////		String cmd = "mysqldump --databases  -u" + username + " " + " -p" + password + " -h " + host;
//        File file = new File("db_backup", UUID.randomUUID().toString());
//        if (!file.exists()) {
//            file.getParentFile().mkdirs();
//        }
//        try {
//            int res = CMDUtils.execute(cmd + " > " + file.getAbsolutePath(), new StringBuffer(), new StringBuffer());
//            if (res == 0) {
//                return file.getAbsolutePath();
//            }
//            throw new Exception("导出出现故障");
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }

//    public String exportStruts() throws Exception {
//        return backup("-d");
//    }


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

    public void getPKList() {
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


    public List<DocField> getDocFields(String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT  column_name, column_comment, column_type, is_nullable,");
        sb.append(" IF (column_key = 'pri', '是', '')  pri ");
        sb.append(" FROM");
        sb.append(" information_schema.columns");
        sb.append(" WHERE table_schema = '" + dbName + "' AND table_name = ?");
        return jdbcTemplate.query(sb.toString(), new Object[]{tableName}, new BeanPropertyRowMapper<>(DocField.class));
    }

    public List<DocTable> getDocTables() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT table_name  ,table_comment FROM information_schema.TABLES WHERE table_schema = '" + dbName + "'");
        return jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<>(DocTable.class));
    }

    /**
     * 执行sql 代码
     *
     * @param sql
     * @return
     */
    public boolean executeSql(String sql) {
        try {
            SQLExec sqlExec = createSqlExec();
            sqlExec.addText(sql);
            sqlExec.execute();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 执行脚本文件
     *
     * @param sqlFile
     */
    public boolean executeSqlFileByAnt(File sqlFile) {
        try {
            SQLExec sqlExec = createSqlExec();
            sqlExec.setSrc(sqlFile);
            sqlExec.execute();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
