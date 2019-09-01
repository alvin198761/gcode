package org.alvin.mybatis.system.address;
import java.util.List;

/**
* @类说明: 收寄信息--数据访问层
* @author: 唐植超
* @date : 2019-08-09 13:36:03
**/
public interface AddressDao {

	/**
    * @方法说明：物理删除收寄信息记录(多条)
    */
    int delete(Long ids[]) ;
	
	/**
    * @方法说明：更新收寄信息记录
    */
    int update(Address vo) ;
	
	/**
    * @方法说明：更新收寄信息记录,不为空的都更新掉
    */
    int updateNotNull(Address vo);
	
	/**
    * @方法说明：新增收寄信息记录
    */
    int save(Address vo);

	/**
    * @方法说明：按条件查询不分页收寄信息列表
    */
    List<Address> queryList(AddressCond cond) ;
	
	/**
    * @方法说明：按条件查询一个 收寄信息 对象
    */
    Address findOne(AddressCond cond) ;
	
	/**
    * @方法说明：按ID查找单个收寄信息实体
    */
    Address findById(Long id);
	
	
	/**
    * @方法说明：按条件查询收寄信息记录个数
    */
    long queryCount(AddressCond cond);
	
	/**
    * @方法说明：按条件查询收寄信息记录个数
    */
    int deleteLogic(Long ids[]);
	
	/**
    * @方法说明： 批量添加 收寄信息
    */
	int insertBatch(List<Address> list);
}