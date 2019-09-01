package org.alvin.springjdbc.system.address;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.alvin.code.gen.beans.BaseDao;
import org.alvin.code.gen.utils.Page;
import org.alvin.code.gen.utils.SqlUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
* @类说明: 收寄信息--数据访问层
* @author: 唐植超
* @date : 2019-08-09 19:18:43
**/
@Slf4j
@Repository
public class AddressDao extends BaseDao{

    private StringBuilder insert = new StringBuilder();

    /**
    * @方法说明：  构造方法,用于拼加SQL及初始化工作
    */
    public AddressDao () {
        insert.append("INSERT INTO address (type,target_phone,target_addr,target_name,target_id,remark) ");
        insert.append(" VALUES (:type,:target_phone,:target_addr,:target_name,:target_id,:remark)");
    }

    /**
    * @方法说明：  新增收寄信息记录
    */
    public int save(Address vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("REPLACE INTO address (id,type,target_phone,target_addr,target_name,target_id,remark)");
        sql.append(" VALUES (?,?,?,?,?,?,?) ");
        Object[] params ={ vo.getId(),vo.getType(),vo.getTargetPhone(),vo.getTargetAddr(),vo.getTargetName(),vo.getTargetId(),vo.getRemark() };
        //logger.info(SqlUtil.showSql(sql.toString(), params));//显示SQL语句
        return jdbcTemplate.update(sql.toString(), params);
    }
    
    /**
    * @方法说明：新增收寄信息记录并返回自增涨主键值
    */
    public long saveReturnPK(Address vo) {
         return saveKey(vo, insert.toString(), "id");
    }
    
    /**
    * @方法说明：批量插入收寄信息记录
    */
    public int[] insertBatch(List<Address> list) {
       return batchOperate(list, insert.toString());
    }
    
    /**
    * @方法说明：物理删除收寄信息记录(多条)
    */
    public int delete(Long ids[]) {
        String sql = "DELETE FROM address WHERE id" + SqlUtil.ArrayToIn(ids);
        return jdbcTemplate.update(sql);
    }
    
    /**
    * @方法说明：更新收寄信息记录
    */
    public int update(Address vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE address SET type=?,target_phone=?,target_addr=?,target_name=?,target_id=?,remark=? ");
        sql.append(" WHERE id=? ");
        Object[] params = {vo.getType(),vo.getTargetPhone(),vo.getTargetAddr(),vo.getTargetName(),vo.getTargetId(),vo.getRemark(),vo.getId()};
        return jdbcTemplate.update(sql.toString(), params);
      }


      /**
    * @方法说明：更新收寄信息记录,不为空的都更新掉
    */
    public int updateNotNull(Address vo) {
        List<String> fields = new ArrayList<>();
        List<Object> values = new ArrayList<>();
                                                        
        if(vo.getType() != null){
        	fields.add(" type = ? ");
        	values.add(vo.getType());
        }	
                                
        if(vo.getTargetPhone() != null){
        	fields.add(" target_phone = ? ");
        	values.add(vo.getTargetPhone());
        }	
                                
        if(vo.getTargetAddr() != null){
        	fields.add(" target_addr = ? ");
        	values.add(vo.getTargetAddr());
        }	
                                
        if(vo.getTargetName() != null){
        	fields.add(" target_name = ? ");
        	values.add(vo.getTargetName());
        }	
                                
        if(vo.getTargetId() != null){
        	fields.add(" target_id = ? ");
        	values.add(vo.getTargetId());
        }	
                                
        if(vo.getRemark() != null){
        	fields.add(" remark = ? ");
        	values.add(vo.getRemark());
        }	
                        
        if(fields.isEmpty()){
        	return 0;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE address SET ");
        sql.append(Joiner.on(",").join(fields));
        sql.append(" WHERE id=? ");
        values.add(vo.getId());
        Object[] params = {values.toArray()};
        return jdbcTemplate.update(sql.toString(), params);
      }

        /**
        * @方法说明：按条件查询分页收寄信息列表
        */
    public Page<Address> queryPage(AddressCond cond) {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(this.getSelectedItems(cond));
        sb.append(" FROM address t ");
        sb.append(getJoinTables());
        sb.append(" WHERE 1=1 ");
        sb.append(cond.getCondition());
        sb.append(cond.getOrderSql());//增加排序子句;
        log.info(SqlUtil.showSql(sb.toString(),cond.getArray()));//显示SQL语句
        return queryPage(sb.toString(), cond, Address.class);
    }
    
    /**
    * @方法说明：按条件查询不分页收寄信息列表
    */
    public List<Address> queryList(AddressCond cond) {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(this.getSelectedItems(cond));
        sb.append(" FROM address t ");
        sb.append(getJoinTables());
        sb.append(" WHERE 1=1 ");
		sb.append(cond.getCondition());
        sb.append(cond.getOrderSql());//增加排序子句;
        log.info(SqlUtil.showSql(sb.toString(),cond.getArray()));//显示SQL语句
    	return jdbcTemplate.query(sb.toString(), cond.getArray(), new BeanPropertyRowMapper<>(Address.class));
    }
    
	/**
    * @方法说明：按条件查询一个 收寄信息 对象
    */
    public Address findOne(AddressCond cond) {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(this.getSelectedItems(cond));
        sb.append(" FROM address t ");
        sb.append(getJoinTables());
        sb.append(" WHERE 1=1 ");
    	sb.append(cond.getCondition());
    	//sb.append(" ORDER BY operate_time DESC");
		sb.append(" limit 0,1");
    	return jdbcTemplate.queryForObject(sb.toString(), cond.getArray(), new BeanPropertyRowMapper<>(Address.class));
    }
	
    /**
    * @方法说明：按ID查找单个收寄信息实体
    */
    public Address findById(Long id) {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(this.getSelectedItems(null));
        sb.append(" FROM address t ");
        sb.append(getJoinTables());
        sb.append(" WHERE 1=1 ");
    	sb.append(" AND t.id=?");
    	return jdbcTemplate.queryForObject(sb.toString(), new Object[]{id}, new BeanPropertyRowMapper<>(Address.class));
    }
    
    /**
    * @方法说明：按条件查询收寄信息记录个数
    */
    public long queryCount(AddressCond cond) {
    	String countSql = "SELECT COUNT(1) FROM address t WHERE 1=1" + cond.getCondition();
    	return jdbcTemplate.queryForObject(countSql, cond.getArray(), Long.class);
    }
    
    /**
    * @方法说明：按条件查询收寄信息记录个数
    */
    public int deleteLogic(Long ids[]) {
    	String sql = "UPDATE address SET status=? WHERE id" + SqlUtil.ArrayToIn(ids);
    	return jdbcTemplate.update(sql ,  new Object[]{-1});
    }

    /**
    * @方法说明：查询参数定制
    */
    public String getSelectedItems(AddressCond cond){
        if(cond == null || cond.getSelectedFields() == null || cond.getSelectedFields().isEmpty()){
        return "t.id,t.type,t.target_phone,t.target_addr,t.target_name,t.target_id,t.remark"; //默认所有字段
        }
        return Joiner.on(",").join(cond.getSelectedFields());
    }

    /**
    * @方法说明：表连接代码
    * @return
    */
    public String getJoinTables(){
        return "";
    }
}