package org.alvin.springjdbc.system.address;

/**
* 类说明: [收寄信息]--数据逻辑层
* @author 唐植超
* 生成日期 2020-02-17 22:40:15
**/
@lombok.extern.slf4j.Slf4j
@org.springframework.stereotype.Service
public class AddressService {

	@org.springframework.beans.factory.annotation.Autowired
	private AddressDao addressDao; //注入收寄信息数据访问层

	/**
	* 方法说明：  新增[收寄信息]记录
	*/
	@org.springframework.transaction.annotation.Transactional
	public int save(Address address) {
		return this.addressDao.save(address);
	}

	/**
	* 方法说明：  删除收寄信息记录(多条)
	*/
	@org.springframework.transaction.annotation.Transactional
	public int delete(Long id) {
		return this.addressDao.deleteLogic(id);//逻辑删除
		//return this.addressDao.delete(id);//物理删除
	}

	/**
	* 方法说明：  更新收寄信息记录
	*/
	@org.springframework.transaction.annotation.Transactional
	public int update(Address address) {
		return this.addressDao.update(address); 
	}

	 /**
    * 方法说明：更新收寄信息记录,不为空的都更新掉
    */
    @org.springframework.transaction.annotation.Transactional
    public int updateNotNull(Address address) {
		return this.addressDao.updateNotNull(address); 
    }

	/**
	* 方法说明： 按条件查询分页收寄信息列表
	*/
	public org.alvin.code.gen.utils.Page<Address> queryPage(AddressCond addressCond) {
		addressCond.getOrder().put("t.id" , "desc");
		return this.addressDao.queryPage(addressCond);
	}

	/**
	* 方法说明： 按条件查询不分页收寄信息列表(使用范型)
	*/
	public java.util.List<Address> queryList(AddressCond addressCond) {
		addressCond.getOrder().put("t.id" , "desc");
		return this.addressDao.queryList(addressCond);
	}
	
	/**
	* 方法说明： 按条件查询一个 收寄信息 对象
	*/
	public Address findOne(AddressCond addressCond) {
		return this.addressDao.findOne(addressCond);
	}

	/**
	* 方法说明： 按ID查找单个收寄信息记录
	*/
	public Address findById(Long id) {
		return this.addressDao.findById(id);
	}

	/**
	* 方法说明： 按条件查询收寄信息记录个数
	*/
	public long queryCount(AddressCond addressCond) {
		return this.addressDao.queryCount(addressCond);
	}


	/**
	 * 方法说明: 查询数据包含外键对象和显示标签
	 *
	 * @param id
	 * @return
	 */
	public AddressFk findFkById(Long id) {
		return this.addressDao.findFkById(id);
	}

	/**
	 * 方法说明: 查询数据包含外键对象和显示标签
	 *
	 * @param addressFkCond
	 * @return
	 */
	public AddressFk findFkOne(AddressFkCond addressFkCond) {
		return this.addressDao.findFkOne(addressFkCond);
	}

	/**
	 * 方法说明: 只显示标签和id的列表
	 *
	 * @param addressFkCond
	 * @return
	 */
	public java.util.List<org.alvin.code.gen.beans.SelectOption> queryLabelList(AddressFkCond addressFkCond) {
		addressFkCond.getOrder().put("t.id" , "desc");
		return this.addressDao.queryLabelList(addressFkCond);
	}

	/**
	 * 方法说明：按条件查询不分页收寄信息列表 (带关系表标签)
	 */
	public java.util.List<AddressFk> queryFkList(AddressFkCond addressFkCond) {
		addressFkCond.getOrder().put("t.id" , "desc");
		return this.addressDao.queryFkList(addressFkCond);
	}

	/**
	 * 方法说明：按条件查询分页收寄信息列表
	 */
	public org.alvin.code.gen.utils.Page<AddressFk> queryFkPage(AddressFkCond addressFkCond) {
		addressFkCond.getOrder().put("t.id" , "desc");
		return this.addressDao.queryFkPage(addressFkCond);
	}

}