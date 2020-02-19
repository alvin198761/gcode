package org.alvin.springjdbc.system.aatable;

/**
* 类说明: [测试A]--数据逻辑层
* @author 唐植超
* 生成日期 2020-02-19 23:05:35
**/
@lombok.extern.slf4j.Slf4j
@org.springframework.stereotype.Service
public class AaTableService {

	@org.springframework.beans.factory.annotation.Autowired
	private AaTableDao aaTableDao; //注入收寄信息数据访问层

	/**
	* 方法说明：  新增[测试A]记录
	*/
	@org.springframework.transaction.annotation.Transactional
	public int save(AaTable aaTable) {
		return this.aaTableDao.save(aaTable);
	}

	/**
	* 方法说明：  删除测试A记录(多条)
	*/
	@org.springframework.transaction.annotation.Transactional
	public int delete(Long id) {
		//return this.aaTableDao.deleteLogic(id);//逻辑删除
		return this.aaTableDao.delete(id);//物理删除
	}

	/**
	* 方法说明：  更新测试A记录
	*/
	@org.springframework.transaction.annotation.Transactional
	public int update(AaTable aaTable) {
		return this.aaTableDao.update(aaTable); 
	}

	 /**
    * 方法说明：更新测试A记录,不为空的都更新掉
    */
    @org.springframework.transaction.annotation.Transactional
    public int updateNotNull(AaTable aaTable) {
		return this.aaTableDao.updateNotNull(aaTable); 
    }

	/**
	* 方法说明： 按条件查询分页测试A列表
	*/
	public org.alvin.code.gen.utils.Page<AaTable> queryPage(AaTableCond aaTableCond) {
		aaTableCond.getOrder().put("t.id" , "desc");
		return this.aaTableDao.queryPage(aaTableCond);
	}

	/**
	* 方法说明： 按条件查询不分页测试A列表(使用范型)
	*/
	public java.util.List<AaTable> queryList(AaTableCond aaTableCond) {
		aaTableCond.getOrder().put("t.id" , "desc");
		return this.aaTableDao.queryList(aaTableCond);
	}
	
	/**
	* 方法说明： 按条件查询一个 测试A 对象
	*/
	public AaTable findOne(AaTableCond aaTableCond) {
		return this.aaTableDao.findOne(aaTableCond);
	}

	/**
	* 方法说明： 按ID查找单个测试A记录
	*/
	public AaTable findById(Long id) {
		return this.aaTableDao.findById(id);
	}

	/**
	* 方法说明： 按条件查询测试A记录个数
	*/
	public long queryCount(AaTableCond aaTableCond) {
		return this.aaTableDao.queryCount(aaTableCond);
	}


	/**
	 * 方法说明: 查询数据包含外键对象和显示标签
	 *
	 * @param id
	 * @return
	 */
	public AaTableFk findFkById(Long id) {
		return this.aaTableDao.findFkById(id);
	}

	/**
	 * 方法说明: 查询数据包含外键对象和显示标签
	 *
	 * @param aaTableFkCond
	 * @return
	 */
	public AaTableFk findFkOne(AaTableFkCond aaTableFkCond) {
		return this.aaTableDao.findFkOne(aaTableFkCond);
	}

	/**
	 * 方法说明: 只显示标签和id的列表
	 *
	 * @param aaTableFkCond
	 * @return
	 */
	public java.util.List<org.alvin.code.gen.beans.SelectOption> queryLabelList(AaTableFkCond aaTableFkCond) {
		aaTableFkCond.getOrder().put("t.id" , "desc");
		return this.aaTableDao.queryLabelList(aaTableFkCond);
	}

	/**
	 * 方法说明：按条件查询不分页测试A列表 (带关系表标签)
	 */
	public java.util.List<AaTableFk> queryFkList(AaTableFkCond aaTableFkCond) {
		aaTableFkCond.getOrder().put("t.id" , "desc");
		return this.aaTableDao.queryFkList(aaTableFkCond);
	}

	/**
	 * 方法说明：按条件查询分页测试A列表
	 */
	public org.alvin.code.gen.utils.Page<AaTableFk> queryFkPage(AaTableFkCond aaTableFkCond) {
		aaTableFkCond.getOrder().put("t.id" , "desc");
		return this.aaTableDao.queryFkPage(aaTableFkCond);
	}

}