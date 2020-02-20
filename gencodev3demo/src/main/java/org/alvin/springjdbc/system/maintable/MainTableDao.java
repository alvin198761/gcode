package org.alvin.springjdbc.system.maintable;

/**
* 类说明: [主表]--数据访问层
 * @类说明: 收寄信息--
* @author 唐植超
* 生成日期 2020-02-20 20:09:06
**/
@lombok.extern.slf4j.Slf4j
@org.springframework.stereotype.Repository
public class MainTableDao extends org.alvin.code.gen.beans.BaseDao {

    /**
    * 方法说明：  新增主表记录
    */
    public int save(MainTable vo) {
	   String sql = "INSERT INTO t_main_table (main_title,a_id,b_id,date) VALUES (:mainTitle,:aId,:bId,:date)";
	   org.alvin.code.gen.beans.SaveKeyObj obj = saveKey(vo, sql, "id");
       vo.setId((Long)obj.getKey());
       return obj.getRes();
    }
    
    /**
    * 方法说明： 批量插入主表记录
    */
    public int[] insertBatch(java.util.List<MainTable> list) {
	   String sql = "INSERT INTO t_main_table (main_title,a_id,b_id,date) VALUES (:mainTitle,:aId,:bId,:date)";
       return batchOperate(list, sql);
    }
    
    /**
    * 方法说明：物理删除主表记录(多条)
    */
    public int delete(Long id) {
        String sql = "DELETE FROM t_main_table WHERE id = " + id;
        return jdbcTemplate.update(sql);
    }
    
    /**
    * 方法说明：更新主表记录
    */
    public int update(MainTable vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE t_main_table SET main_title=?,a_id=?,b_id=?,date=? ");
        sql.append(" WHERE id=? ");
        Object[] params = {vo.getMainTitle(),vo.getAId(),vo.getBId(),vo.getDate(),vo.getId()};
        return jdbcTemplate.update(sql.toString(), params);
      }


     /**
    * 方法说明： 更新主表记录,不为空的都更新掉
    */
    public int updateNotNull(MainTable vo) {
        java.util.List<String> fields = new java.util.ArrayList<>();
        java.util.List<Object> values = new java.util.ArrayList<>();
                if(vo.getId() != null){
            fields.add(" id = ? ");
            values.add(vo.getId());
        }   
                if(vo.getMainTitle() != null){
            fields.add(" main_title = ? ");
            values.add(vo.getMainTitle());
        }   
                if(vo.getAId() != null){
            fields.add(" a_id = ? ");
            values.add(vo.getAId());
        }   
                if(vo.getBId() != null){
            fields.add(" b_id = ? ");
            values.add(vo.getBId());
        }   
                if(vo.getDate() != null){
            fields.add(" date = ? ");
            values.add(vo.getDate());
        }   
                
        if(fields.isEmpty()){
            return 0;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE t_main_table SET ");
        sql.append(com.google.common.base.Joiner.on(",").join(fields));
        sql.append(" WHERE ");
        Object[] params = values.toArray();
        return jdbcTemplate.update(sql.toString(), params);
      }

        /**
        * 方法说明： 按条件查询分页主表列表
        */
    public org.alvin.code.gen.utils.Page<MainTable> queryPage(MainTableCond cond) {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(this.getSelectedItems(cond));
        sb.append(" FROM t_main_table t ");
        sb.append(this.getJoinTables());
        sb.append(" WHERE 1=1 ");
        sb.append(cond.getCondition());
        sb.append(cond.getOrderSql());//增加排序子句;
        //log.info(org.alvin.code.gen.utils.SqlUtil.showSql(sb.toString(),cond.getArray()));//显示SQL语句
        return queryPage(sb.toString(), cond, MainTable.class);
    }
    
    /**
    * 方法说明：按条件查询不分页主表列表
    */
    public java.util.List<MainTable> queryList(MainTableCond cond) {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(this.getSelectedItems(cond));
        sb.append(" FROM t_main_table t ");
        sb.append(this.getJoinTables());
        sb.append(" WHERE 1=1 ");
    	sb.append(cond.getCondition());
    	sb.append(cond.getOrderSql());//增加排序子句;
    	return jdbcTemplate.query(sb.toString(), cond.getArray(), new org.springframework.jdbc.core.BeanPropertyRowMapper<>(MainTable.class));
    }
    
    /**
    * 方法说明：按ID查找单个主表实体
    */
    public MainTable findById(Long id) {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(this.getSelectedItems(null));
        sb.append(" FROM t_main_table t ");
        sb.append(getJoinTables());
        sb.append(" WHERE 1=1 ");
    	sb.append(" AND t.id=?");
    	return jdbcTemplate.queryForObject(sb.toString(), new Object[]{id}, new org.springframework.jdbc.core.BeanPropertyRowMapper<>(MainTable.class));
    }
	
	 /**
     * 方法说明：按条件查询一个 收寄信息 对象
     */
    public MainTable findOne(MainTableCond cond) {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(this.getSelectedItems(cond));
        sb.append(" FROM t_main_table t ");
        sb.append(this.getJoinTables());
        sb.append(" WHERE 1=1 ");
        sb.append(cond.getCondition());
        sb.append(cond.getOrderSql());//增加排序子句;
        sb.append(" limit 0,1");
        return jdbcTemplate.queryForObject(sb.toString(), cond.getArray(), new org.springframework.jdbc.core.BeanPropertyRowMapper<>(MainTable.class));
    }
    
    /**
    * 方法说明：按条件查询主表记录个数
    */
    public long queryCount(MainTableCond cond) {
    	String countSql = "SELECT COUNT(1) FROM t_main_table t WHERE 1=1" + cond.getCondition();
    	return jdbcTemplate.queryForObject(countSql, cond.getArray(), Long.class);
    }
    
    /**
    * 方法说明：按条件查询主表记录个数
    */
    public int deleteLogic(Long id) {
    	String sql = "UPDATE t_main_table SET delete_remark=1 WHERE id = " + id;
    	return jdbcTemplate.update(sql);
    }

    /**
    * 方法说明：查询参数定制
    */
    public String getSelectedItems(MainTableCond cond){
        if(cond == null || cond.getSelectedFields() == null || cond.getSelectedFields().isEmpty()){
        return "t.id,t.main_title,t.a_id,t.b_id,t.date"; //默认所有字段
        }
        return com.google.common.base.Joiner.on(",").join(cond.getSelectedFields());
    }

    /**
    * 方法说明：表连接代码
    * @return
    */
    public String getJoinTables(){
        return "";
    }

    /**
     * 方法说明: 查询数据包含外键对象和显示标签
     *
     * @param id
     * @return
     */
    public MainTableFk findFkById(Long id) {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(this.getFkSelectedItems(null));
        sb.append(" FROM t_main_table t ");
        sb.append(this.getFkJoinTables());
        sb.append(" WHERE 1=1 ");
        sb.append(" AND t.id=? ");
        return jdbcTemplate.queryForObject(sb.toString(), new Object[]{id}, new org.springframework.jdbc.core.BeanPropertyRowMapper<>(MainTableFk.class));
    }

    /**
     * 方法说明: 查询数据包含外键对象和显示标签
     *
     * @param cond
     * @return
     */
    public MainTableFk findFkOne(MainTableFkCond cond) {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(this.getFkSelectedItems(cond));
        sb.append(" FROM t_main_table t ");
        sb.append(this.getFkJoinTables());
        sb.append(" WHERE 1=1 ");
        sb.append(cond.getCondition());
        sb.append(" limit 0,1");
        return jdbcTemplate.queryForObject(sb.toString(), cond.getArray(), new org.springframework.jdbc.core.BeanPropertyRowMapper<>(MainTableFk.class));
    }

    /**
     * 方法说明: 只显示标签和id的列表
     *
     * @param cond
     * @return
     */
    public java.util.List<org.alvin.code.gen.beans.SelectOption> queryLabelList(MainTableFkCond cond) {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append("t.id as value ,t.main_title as label");
        sb.append(" FROM t_main_table t ");
        sb.append(this.getFkJoinTables());
        sb.append(" WHERE 1=1 ");
        sb.append(cond.getCondition());
        sb.append(cond.getOrderSql());//增加排序子句
        return jdbcTemplate.query(sb.toString(), cond.getArray(), new org.springframework.jdbc.core.BeanPropertyRowMapper<>(org.alvin.code.gen.beans.SelectOption.class));
    }

    /**
     * 方法说明：按条件查询不分页主表列表 (带关系表标签)
     */
    public java.util.List<MainTableFk> queryFkList(MainTableFkCond cond) {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(this.getFkSelectedItems(cond));
        sb.append(" FROM t_main_table t ");
        sb.append(this.getFkJoinTables());
        sb.append(" WHERE 1=1 ");
        sb.append(cond.getCondition());
        sb.append(cond.getOrderSql());//增加排序子句;
        log.info(org.alvin.code.gen.utils.SqlUtil.showSql(sb.toString(), cond.getArray()));//显示SQL语句
        return jdbcTemplate.query(sb.toString(), cond.getArray(), new org.springframework.jdbc.core.BeanPropertyRowMapper<>(MainTableFk.class));
    }

    /**
     * 方法说明：按条件查询分页主表列表
     */
    public org.alvin.code.gen.utils.Page<MainTableFk> queryFkPage(MainTableFkCond cond) {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(this.getFkSelectedItems(cond));
        sb.append(" FROM t_main_table t ");
        sb.append(this.getFkJoinTables());
        sb.append(" WHERE 1=1 ");
        sb.append(cond.getCondition());
        sb.append(cond.getOrderSql());//增加排序子句;
        log.info(org.alvin.code.gen.utils.SqlUtil.showSql(sb.toString(), cond.getArray()));//显示SQL语句
        return queryPage(sb.toString(), cond, MainTableFk.class);
    }

    /**
     * @return 方法说明：表连接的所有字段
     */
    public String getFkSelectedItems(MainTableCond cond) {
        if (cond == null || cond.getSelectedFields() == null || cond.getSelectedFields().isEmpty()) {
			StringBuilder sb = new StringBuilder();
			sb.append("t.id,t.main_title,t.a_id,t.b_id,t.date");
							sb.append(" ,aaTable.id ");
							sb.append(" ,bbTest.id ");
			            return sb.toString(); //默认所有字段
        }
        return com.google.common.base.Joiner.on(",").join(cond.getSelectedFields());
    }

    /**
     * @return 方法说明：表连接代码
     */
    public String getFkJoinTables() {
        StringBuilder sb = new StringBuilder();
					sb.append(" LEFT JOIN aa_table AS aaTable ON t.a_id = aaTable.id ");
					sb.append(" LEFT JOIN bb_test AS bbTest ON t.b_id = bbTest.id ");
		        return sb.toString();
    }

}