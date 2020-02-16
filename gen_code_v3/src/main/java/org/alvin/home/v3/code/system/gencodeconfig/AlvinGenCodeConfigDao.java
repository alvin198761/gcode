package org.alvin.home.v3.code.system.gencodeconfig;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.alvin.home.v3.code.beans.FKFieldBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @类说明: t_alvin_gen_code_config--数据访问层
 * @author: 唐植超
 * @date : 2020-02-14 20:14:36
 **/
@Slf4j
@Repository
public class AlvinGenCodeConfigDao {

    private StringBuilder insert = new StringBuilder();
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Value("${db_name}")
    private String dbName;
    /**
     * @方法说明： 构造方法, 用于拼加SQL及初始化工作
     */
    public AlvinGenCodeConfigDao() {
        insert.append("INSERT INTO t_alvin_gen_code_config (table_schema,table_name,label_name) ");
        insert.append(" VALUES (:tableSchema,:tableName,:labelName)");
    }

    /**
     * @方法说明： 新增t_alvin_gen_code_config记录
     */
    public int save(AlvinGenCodeConfig vo) {
        //删除相同的数据
        vo.setTableSchema(dbName);
        String deleteSql = "delete from t_alvin_gen_code_config where table_schema=? and  table_name= ?";
        this.jdbcTemplate.update(deleteSql,new Object[]{dbName,vo.getTableName()});
        if(Strings.isNullOrEmpty(vo.getLabelName())){
            return 1;
        }
        //添加
        StringBuilder sql = new StringBuilder();
        sql.append("REPLACE INTO t_alvin_gen_code_config (id,table_schema,table_name,label_name)");
        sql.append(" VALUES (?,?,?,?) ");
        Object[] params = {vo.getId(), vo.getTableSchema(), vo.getTableName(), vo.getLabelName()};
        //log.info(SqlUtil.showSql(sql.toString(), params));//显示SQL语句
        return jdbcTemplate.update(sql.toString(), params);
    }


    /**
     * @方法说明：更新t_alvin_gen_code_config记录
     */
    public int update(AlvinGenCodeConfig vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE t_alvin_gen_code_config SET table_schema=?,table_name=?,label_name=? ");
        sql.append(" WHERE id=? ");
        Object[] params = {vo.getId()};
        return jdbcTemplate.update(sql.toString(), params);
    }

    /**
     * @return
     * @方法说明：表连接代码
     */
    public String getJoinTables() {
        return "";
    }

    public List<AlvinGenCodeConfig> queryList(List<String> tableNames) {
        String sql = "select * from t_alvin_gen_code_config t where t.table_name in (" + tableNames.parallelStream().map(item -> "'".concat(item).concat("'")).collect(Collectors.joining(",")) + ")";
        return jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<>(AlvinGenCodeConfig.class));
    }
}