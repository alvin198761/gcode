package org.alvin.jdbc_demo.system.address;

import org.alvin.code.jdbc.annotations.JDBCTransction;
import org.alvin.code.jdbc.beans.Page;
import org.alvin.mini_inject.annotations.MiniComponent;
import org.alvin.mini_inject.annotations.MiniInject;

import java.sql.SQLException;
import java.util.List;

/**
 * @类说明: 收寄信息--数据逻辑层
 * @author: 唐植超
 * @date : 2019-08-09 19:18:43
 **/
@MiniComponent
public class AddressService {


    @MiniInject
    private AddressDao dao; //注入收寄信息数据访问层

    /**
     * @方法说明： 新增[收寄信息]记录
     */
    @JDBCTransction
    public int save(Address address) throws SQLException {
        Long count = this.queryCount(AddressCond.builder().build());
        System.out.println("count111:" + count);
        int res = dao.save(address);
        count = this.queryCount(AddressCond.builder().build());
        System.out.println("count122:" + count);
        return res;
    }

    /**
     * @方法说明： 新增[收寄信息]记录
     */
    @JDBCTransction
    public int[] batchSave(List<Address> list) throws SQLException {
        return this.dao.insertBatch(list);
    }

    /**
     * @方法说明： 删除收寄信息记录(多条)
     */
    @JDBCTransction
    public int delete(Long ids[]) throws SQLException {
//			return dao.deleteLogic(ids);//逻辑删除
        return dao.delete(ids);//物理删除
    }

    /**
     * @方法说明： 更新收寄信息记录
     */
    @JDBCTransction
    public int update(Address address) throws SQLException {
        return dao.update(address);
    }

    /**
     * @方法说明：更新收寄信息记录,不为空的都更新掉
     */
    @JDBCTransction
    public int updateNotNull(Address address) throws SQLException {
        return dao.updateNotNull(address);
    }

    /**
     * @方法说明： 按条件查询分页收寄信息列表
     */
    public Page<Address> queryPage(AddressCond cond) throws SQLException {
        cond.getOrder().put("t.id", "desc");
        return dao.queryPage(cond);
    }

    /**
     * @方法说明： 按条件查询不分页收寄信息列表(使用范型)
     */
    public List<Address> queryList(AddressCond cond) throws SQLException {
        cond.getOrder().put("t.id", "desc");
        return dao.queryList(cond);
    }

    /**
     * @方法说明： 按条件查询一个 收寄信息 对象
     */
    public Address findOne(AddressCond cond) throws SQLException {
        cond.getOrder().put("t.id", "desc");
        return dao.findOne(cond);
    }

    /**
     * @方法说明： 按ID查找单个收寄信息记录
     */
    public Address findById(Long id) throws SQLException {
        return dao.findById(id);
    }

    /**
     * @方法说明： 按条件查询收寄信息记录个数
     */
    public long queryCount(AddressCond cond) throws SQLException {
        return dao.queryCount(cond);
    }
}