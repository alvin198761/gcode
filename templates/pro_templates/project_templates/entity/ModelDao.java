package ${pName}.sys.${low} ;
import java.util.List;
import org.alvin.code.gen.beans.BaseDao;
import org.alvin.code.gen.utils.Page;
import org.alvin.code.gen.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import com.google.common.base.Joiner;
/**
* @类说明: ${cName}--数据访问层
* @author: ${auth}
* @date : ${time}
**/
@Repository
public class ${clsUpp}Dao extends BaseDao{

    /**
    * @方法说明：  构造方法,用于拼加SQL及初始化工作
    */
    public ${clsUpp}Dao () {
        insert.append("INSERT INTO ${tName} (${insertFields}) ");
        insert.append(" VALUES (${insertValuesFields})");
    }

    /**
    * @方法说明：  新增${cName}记录
    */
    public int save(${clsUpp} vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("REPLACE INTO ${tName} (${replaceFields})");
        sql.append(" VALUES ${replaceValuesFields} ");
        Object[] params ={ ${paramsFields} };
        //logger.info(SqlUtil.showSql(sql.toString(), params));//显示SQL语句
        return jdbcTemplate.update(sql.toString(), params);
    }
    
    /**
    * @方法说明：新增${cName}记录并返回自增涨主键值
    */
    public long saveReturnPK(${clsUpp} vo) {
         return saveKey(vo, insert.toString(), "${idName}");
    }
    
    /**
    * @方法说明：批量插入${cName}记录
    */
    public int[] insertBatch(List<${clsUpp}> list) {
       return batchOperate(list, insert.toString());
    }
    
    /**
    * @方法说明：物理删除${cName}记录(多条)
    */
    public int delete(${idType} ids[]) {
        String sql = "DELETE FROM ${tName} WHERE ${idName}" + SqlUtil.ArrayToIn(ids);
        return jdbcTemplate.update(sql);
    }
    
    /**
    * @方法说明：更新${cName}记录
    */
    public int update(${clsUpp} vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ${tName} SET ${updateFields} ");
        sql.append(" WHERE ${idName}=? ");
        Object[] params = {${updateParams}};
        return jdbcTemplate.update(sql.toString(), params);
      }

        /**
        * @方法说明：按条件查询分页${cName}列表
        */
    public Page<${clsUpp}> queryPage(${clsUpp}Cond cond) {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(this.getSelectedItems(cond));
        sb.append(" FROM ${tName} t ");
        sb.append(super.getJoinTables(cond));
        sb.append(" WHERE 1=1 ");
        sb.append(cond.getCondition());
        //sb.append(cond.getOrderSql());//增加排序子句;
        //logger.info(SqlUtil.showSql(sb.toString(),cond.getArray()));//显示SQL语句
        return queryPage(sb.toString(), cond, ${clsUpp}.class);
    }
    
    /**
    * @方法说明：按条件查询不分页${cName}列表
    */
    public List<${clsUpp}> queryList(${clsUpp}Cond cond) {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(this.getSelectedItems(cond));
        sb.append(" FROM ${tName} t ");
        sb.append(super.getJoinTables(cond));
        sb.append(" WHERE 1=1 ");
    	sb.append(cond.getCondition());
    	//sb.append(" ORDER BY operate_time DESC");
    	return jdbcTemplate.query(sb.toString(), cond.getArray(), new BeanPropertyRowMapper<>(${clsUpp}.class));
    }
    
    /**
    * @方法说明：按ID查找单个${cName}实体
    */
    public ${clsUpp} findById(${idType} id) {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(this.getSelectedItems(null));
        sb.append(" FROM ${tName} t ");
        sb.append(super.getJoinTables(null));
        sb.append(" WHERE 1=1 ");
    	sb.append(" AND t.${idName}=?");
    	return jdbcTemplate.queryForObject(sb.toString(), new Object[]{id}, new BeanPropertyRowMapper<>(${clsUpp}.class));
    }
    
    /**
    * @方法说明：按条件查询${cName}记录个数
    */
    public long queryCount(${clsUpp}Cond cond) {
    	String countSql = "SELECT COUNT(1) FROM ${tName} t WHERE 1=1" + cond.getCondition();
    	return jdbcTemplate.queryForObject(countSql, cond.getArray(), Long.class);
    }
    
    /**
    * @方法说明：按条件查询${cName}记录个数
    */
    public int deleteLogic(${idType} ids[]) {
    	String sql = "UPDATE ${tName} SET delete_remark=1 WHERE ${idName}" + SqlUtil.ArrayToIn(ids);
    	return jdbcTemplate.update(sql);
    }

    /**
    * 所有表连接语句
    * @return
    */
    @Override
    protected String joinTables() {
     return "${joinTables}";
    }

    /**
    * 根据传入的列 和 ID 更新某一条数据
    *
    * @param cols
    * @param vo
    * @return
    */
    public int update(List<String> cols, ${clsUpp} vo) {
        return super.update(cols, vo, "${tName}", "${idName}");
    }

    /**
    * 根据传入的属性和更新条件更新多条数据
    *
    * @param condition
    * @param fields
    * @return
    */
    public int updateAllByCondition(${clsUpp}Cond condition, List<String> fields) {
        return super.updateAllByCondition(condition, "${tName}", fields);
     }

    /**
    * 将对象不为空的属性更新到数据库（一条数据）
    *
    * @param vo
    * @return
    */
    public int updateFieldNotNull(${clsUpp} vo) {
        return super.updateFieldNotNull(vo, "${tName}", "${idName}");
    }

}