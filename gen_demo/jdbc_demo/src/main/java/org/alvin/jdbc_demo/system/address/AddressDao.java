package org.alvin.jdbc_demo.system.address;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.alvin.code.jdbc.JDBCBaseDao;
import org.alvin.code.jdbc.beans.Page;
import org.alvin.code.jdbc.utils.SqlUtil;
import org.alvin.mini_inject.annotations.MiniComponent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @类说明: 收寄信息--数据访问层
 * @author: 唐植超
 * @date : 2019-08-09 19:18:43
 **/
@MiniComponent
public class AddressDao extends JDBCBaseDao {

    /**
     * @方法说明： 新增收寄信息记录
     */
    public int save(Address vo) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("REPLACE INTO address (id,type,target_phone,target_addr,target_name,target_id,remark)");
        sql.append(" VALUES (?,?,?,?,?,?,?) ");
        Object[] params = {vo.getId(), vo.getType(), vo.getTargetPhone(), vo.getTargetAddr(), vo.getTargetName(), vo.getTargetId(), vo.getRemark()};
        System.out.println(SqlUtil.showSql(sql.toString(), params));//显示SQL语句
        Map<String, Object> keyMap = Maps.newHashMap();
        int res = this.saveReturnPK(sql.toString(), params, keyMap);
        vo.setId((Long) keyMap.get("id"));
        return res;
    }

    /**
     * @方法说明：批量插入收寄信息记录
     */
    public int[] insertBatch(List<Address> list) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("REPLACE INTO address (id,type,target_phone,target_addr,target_name,target_id,remark)");
        sql.append(" VALUES (?,?,?,?,?,?,?) ");
        List<Object[]> params = list.stream().map(vo -> {
            Object[] p = {vo.getId(), vo.getType(), vo.getTargetPhone(), vo.getTargetAddr(), vo.getTargetName(), vo.getTargetId(), vo.getRemark()};
            return p;
        }).collect(Collectors.toList());
        List<Map<String, Object>> keyMpas = Lists.newArrayList();
        int[] res = this.saveBatchReturnPk(sql.toString(), params, keyMpas);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setId((Long) keyMpas.get(i).get("id"));
        }
        return res;
    }

    /**
     * @方法说明：物理删除收寄信息记录(多条)
     */
    public int delete(Long ids[]) throws SQLException {
        String sql = "DELETE FROM address WHERE id " + SqlUtil.ArrayToIn(ids);
        return this.update(sql);
    }

    /**
     * @方法说明：更新收寄信息记录
     */
    public int update(Address vo) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE address SET type=?,target_phone=?,target_addr=?,target_name=?,target_id=?,remark=? ");
        sql.append(" WHERE id=? ");
        Object[] params = {vo.getType(), vo.getTargetPhone(), vo.getTargetAddr(), vo.getTargetName(), vo.getTargetId(), vo.getRemark(), vo.getId()};
        return this.update(sql.toString(), params);
    }


    /**
     * @方法说明：更新收寄信息记录,不为空的都更新掉
     */
    public int updateNotNull(Address vo) throws SQLException {
        List<String> fields = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        if (vo.getType() != null) {
            fields.add(" type = ? ");
            values.add(vo.getType());
        }

        if (vo.getTargetPhone() != null) {
            fields.add(" target_phone = ? ");
            values.add(vo.getTargetPhone());
        }

        if (vo.getTargetAddr() != null) {
            fields.add(" target_addr = ? ");
            values.add(vo.getTargetAddr());
        }

        if (vo.getTargetName() != null) {
            fields.add(" target_name = ? ");
            values.add(vo.getTargetName());
        }

        if (vo.getTargetId() != null) {
            fields.add(" target_id = ? ");
            values.add(vo.getTargetId());
        }

        if (vo.getRemark() != null) {
            fields.add(" remark = ? ");
            values.add(vo.getRemark());
        }

        if (fields.isEmpty()) {
            return 0;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE address SET ");
        sql.append(Joiner.on(",").join(fields));
        sql.append(" WHERE id=? ");
        values.add(vo.getId());
        Object[] params = {values.toArray()};
        return this.update(sql.toString(), params);
    }

    /**
     * @方法说明：按条件查询分页收寄信息列表
     */
    public Page<Address> queryPage(AddressCond cond) throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(" t.id,t.type,t.target_phone,t.target_addr,t.target_name,t.target_id,t.remark ");
        sb.append(" FROM address t ");
        sb.append(" WHERE 1=1 ");
        sb.append(cond.getCondition());
        sb.append(cond.getOrderSql());//增加排序子句;
//        log.info(SqlUtil.showSql(sb.toString(), cond.getArray()));//显示SQL语句
        return queryPage(sb.toString(), cond, Address.class);
    }

    /**
     * @方法说明：按条件查询不分页收寄信息列表
     */
    public List<Address> queryList(AddressCond cond) throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(" t.id,t.type,t.target_phone,t.target_addr,t.target_name,t.target_id,t.remark ");
        sb.append(" FROM address t ");
        sb.append(" WHERE 1=1 ");
        sb.append(cond.getCondition());
        sb.append(cond.getOrderSql());//增加排序子句;
        System.out.println(SqlUtil.showSql(sb.toString(), cond.getArray()));//显示SQL语句
        return this.queryList(sb.toString(), cond, Address.class);
    }

    /**
     * @方法说明：按条件查询一个 收寄信息 对象
     */
    public Address findOne(AddressCond cond) throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(" t.id,t.type,t.target_phone,t.target_addr,t.target_name,t.target_id,t.remark ");
        sb.append(" FROM address t ");
        sb.append(" WHERE 1=1 ");
        sb.append(cond.getCondition());
        sb.append(" limit 0,1");
        return this.findOne(sb.toString(), cond.getArray(), Address.class);
    }

    /**
     * @方法说明：按ID查找单个收寄信息实体
     */
    public Address findById(Long id) throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(" t.id,t.type,t.target_phone,t.target_addr,t.target_name,t.target_id,t.remark ");
        sb.append(" FROM address t ");
        sb.append(" WHERE 1=1 ");
        sb.append(" AND t.id=?");
        return this.findOne(sb.toString(), new Object[]{id}, Address.class);
    }

    /**
     * @方法说明：按条件查询收寄信息记录个数
     */
    public long queryCount(AddressCond cond) throws SQLException {
        String countSql = "SELECT COUNT(1) FROM address t WHERE 1=1" + cond.getCondition();
        return this.findCell(countSql, cond.getArray(), Long.class);
    }

    /**
     * @方法说明：按条件查询收寄信息记录个数
     */
    public int deleteLogic(Long ids[]) throws SQLException {
        String sql = "UPDATE address SET status=? WHERE id" + SqlUtil.ArrayToIn(ids);
        return this.update(sql, new Object[]{-1});
    }


}