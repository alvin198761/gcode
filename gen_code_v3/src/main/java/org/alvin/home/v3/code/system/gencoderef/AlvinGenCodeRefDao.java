package org.alvin.home.v3.code.system.gencoderef;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.alvin.home.v3.code.system.gencode.TableSettingsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @类说明: t_alvin_gen_code_ref--数据访问层
 * @author: 唐植超
 * @date : 2020-02-15 11:28:05
 **/
@Slf4j
@Repository
public class AlvinGenCodeRefDao {

    private StringBuilder insert = new StringBuilder();
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Value("${db_name}")
    private String dbName;

    /**
     * @方法说明： 构造方法, 用于拼加SQL及初始化工作
     */
    public AlvinGenCodeRefDao() {
        insert.append("INSERT INTO t_alvin_gen_code_ref (table_schema,table_name,col_name,ref_table_name,ref_col_name) ");
        insert.append(" VALUES (?,?,?,?,?)");
    }

    /**
     * @方法说明： 新增t_alvin_gen_code_ref记录
     */
    public int save(AlvinGenCodeRef vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("REPLACE INTO t_alvin_gen_code_ref (id,table_schema,table_name,col_name,ref_table_name,ref_col_name)");
        sql.append(" VALUES (?,?,?,?,?,?) ");
        Object[] params = {vo.getId(), vo.getTableSchema(), vo.getTableName(), vo.getColName(), vo.getRefTableName(), vo.getRefColName()};
        //log.info(SqlUtil.showSql(sql.toString(), params));//显示SQL语句
        return jdbcTemplate.update(sql.toString(), params);
    }


    /**
     * @方法说明：更新t_alvin_gen_code_ref记录
     */
    public int update(AlvinGenCodeRef vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE t_alvin_gen_code_ref SET table_schema=?,table_name=?,col_name=?,ref_table_name=?,ref_col_name=? ");
        sql.append(" WHERE id=? ");
        Object[] params = {vo.getId()};
        return jdbcTemplate.update(sql.toString(), params);
    }


    /**
     * @方法说明：更新t_alvin_gen_code_ref记录,不为空的都更新掉
     */
    public int updateNotNull(AlvinGenCodeRef vo) {
        List<String> fields = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        if (vo.getTableSchema() != null) {
            fields.add(" table_schema = ? ");
            values.add(vo.getTableSchema());
        }

        if (vo.getTableName() != null) {
            fields.add(" table_name = ? ");
            values.add(vo.getTableName());
        }

        if (vo.getColName() != null) {
            fields.add(" col_name = ? ");
            values.add(vo.getColName());
        }

        if (vo.getRefTableName() != null) {
            fields.add(" ref_table_name = ? ");
            values.add(vo.getRefTableName());
        }

        if (vo.getRefColName() != null) {
            fields.add(" ref_col_name = ? ");
            values.add(vo.getRefColName());
        }

        if (fields.isEmpty()) {
            return 0;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE t_alvin_gen_code_ref SET ");
        sql.append(Joiner.on(",").join(fields));
        sql.append(" WHERE id=? ");
        values.add(vo.getId());
        Object[] params = values.toArray();
        return jdbcTemplate.update(sql.toString(), params);
    }

    /**
     * 根据表名查询相应关系配置
     *
     * @param tableName
     * @return
     */
    public List<AlvinGenCodeRef> queryListByTableName(String tableName) {
        String sql = "SELECT t.id,t.table_schema,t.table_name,t.col_name,t.ref_table_name,t.ref_col_name from  t_alvin_gen_code_ref t where t.table_name=?";
        return jdbcTemplate.query(sql, new Object[]{tableName}, new BeanPropertyRowMapper<>(AlvinGenCodeRef.class));
    }

    /**
     * 批量添加关系
     *
     * @param settings
     */
    public void batchInsert(TableSettingsDto settings) {
        String deleteSql = "delete from t_alvin_gen_code_ref where table_name = ? and table_schema=?";
        this.jdbcTemplate.update(deleteSql, new Object[]{settings.getTableName(), dbName});
        List<Object[]> params = Lists.newArrayList();
        for (AlvinGenCodeRef vo : settings.getFields()) {
            params.add(new Object[]{ dbName, vo.getTableName(), vo.getColName(), vo.getRefTableName(), vo.getRefColName()});
        }
        this.jdbcTemplate.batchUpdate(insert.toString(), params);
    }
}