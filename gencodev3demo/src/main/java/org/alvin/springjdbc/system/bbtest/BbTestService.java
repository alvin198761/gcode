package org.alvin.springjdbc.system.bbtest;

/**
* 类说明: [测试表B]--数据逻辑层
* @author 唐植超
* 生成日期 2020-02-20 20:09:06
**/
@lombok.extern.slf4j.Slf4j
@org.springframework.stereotype.Service
public class BbTestService {

	@org.springframework.beans.factory.annotation.Autowired
	private BbTestDao bbTestDao; //注入收寄信息数据访问层

	/**
	* 方法说明：  新增[测试表B]记录
	*/
	@org.springframework.transaction.annotation.Transactional
	public int save(BbTest bbTest) {
		return this.bbTestDao.save(bbTest);
	}

	/**
	* 方法说明：  删除测试表B记录(多条)
	*/
	@org.springframework.transaction.annotation.Transactional
	public int delete(Long id) {
		//return this.bbTestDao.deleteLogic(id);//逻辑删除
		return this.bbTestDao.delete(id);//物理删除
	}

	/**
	* 方法说明：  更新测试表B记录
	*/
	@org.springframework.transaction.annotation.Transactional
	public int update(BbTest bbTest) {
		return this.bbTestDao.update(bbTest); 
	}

	 /**
    * 方法说明：更新测试表B记录,不为空的都更新掉
    */
    @org.springframework.transaction.annotation.Transactional
    public int updateNotNull(BbTest bbTest) {
		return this.bbTestDao.updateNotNull(bbTest); 
    }

	/**
	* 方法说明： 按条件查询分页测试表B列表
	*/
	public org.alvin.code.gen.utils.Page<BbTest> queryPage(BbTestCond bbTestCond) {
		bbTestCond.getOrder().put("t.id" , "desc");
		return this.bbTestDao.queryPage(bbTestCond);
	}

	/**
	* 方法说明： 按条件查询不分页测试表B列表(使用范型)
	*/
	public java.util.List<BbTest> queryList(BbTestCond bbTestCond) {
		bbTestCond.getOrder().put("t.id" , "desc");
		return this.bbTestDao.queryList(bbTestCond);
	}
	
	/**
	* 方法说明： 按条件查询一个 测试表B 对象
	*/
	public BbTest findOne(BbTestCond bbTestCond) {
		return this.bbTestDao.findOne(bbTestCond);
	}

	/**
	* 方法说明： 按ID查找单个测试表B记录
	*/
	public BbTest findById(Long id) {
		return this.bbTestDao.findById(id);
	}

	/**
	* 方法说明： 按条件查询测试表B记录个数
	*/
	public long queryCount(BbTestCond bbTestCond) {
		return this.bbTestDao.queryCount(bbTestCond);
	}


	/**
	 * 方法说明: 查询数据包含外键对象和显示标签
	 *
	 * @param id
	 * @return
	 */
	public BbTestFk findFkById(Long id) {
		return this.bbTestDao.findFkById(id);
	}

	/**
	 * 方法说明: 查询数据包含外键对象和显示标签
	 *
	 * @param bbTestFkCond
	 * @return
	 */
	public BbTestFk findFkOne(BbTestFkCond bbTestFkCond) {
		return this.bbTestDao.findFkOne(bbTestFkCond);
	}

	/**
	 * 方法说明: 只显示标签和id的列表
	 *
	 * @param bbTestFkCond
	 * @return
	 */
	public java.util.List<org.alvin.code.gen.beans.SelectOption> queryLabelList(BbTestFkCond bbTestFkCond) {
		bbTestFkCond.getOrder().put("t.id" , "desc");
		return this.bbTestDao.queryLabelList(bbTestFkCond);
	}

	/**
	 * 方法说明：按条件查询不分页测试表B列表 (带关系表标签)
	 */
	public java.util.List<BbTestFk> queryFkList(BbTestFkCond bbTestFkCond) {
		bbTestFkCond.getOrder().put("t.id" , "desc");
		return this.bbTestDao.queryFkList(bbTestFkCond);
	}

	/**
	 * 方法说明：按条件查询分页测试表B列表
	 */
	public org.alvin.code.gen.utils.Page<BbTestFk> queryFkPage(BbTestFkCond bbTestFkCond) {
		bbTestFkCond.getOrder().put("t.id" , "desc");
		return this.bbTestDao.queryFkPage(bbTestFkCond);
	}

}