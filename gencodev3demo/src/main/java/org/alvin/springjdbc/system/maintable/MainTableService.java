package org.alvin.springjdbc.system.maintable;

/**
* 类说明: [主表]--数据逻辑层
* @author 唐植超
* 生成日期 2020-02-19 23:05:35
**/
@lombok.extern.slf4j.Slf4j
@org.springframework.stereotype.Service
public class MainTableService {

	@org.springframework.beans.factory.annotation.Autowired
	private MainTableDao mainTableDao; //注入收寄信息数据访问层

	/**
	* 方法说明：  新增[主表]记录
	*/
	@org.springframework.transaction.annotation.Transactional
	public int save(MainTable mainTable) {
		return this.mainTableDao.save(mainTable);
	}

	/**
	* 方法说明：  删除主表记录(多条)
	*/
	@org.springframework.transaction.annotation.Transactional
	public int delete(Long id) {
		//return this.mainTableDao.deleteLogic(id);//逻辑删除
		return this.mainTableDao.delete(id);//物理删除
	}

	/**
	* 方法说明：  更新主表记录
	*/
	@org.springframework.transaction.annotation.Transactional
	public int update(MainTable mainTable) {
		return this.mainTableDao.update(mainTable); 
	}

	 /**
    * 方法说明：更新主表记录,不为空的都更新掉
    */
    @org.springframework.transaction.annotation.Transactional
    public int updateNotNull(MainTable mainTable) {
		return this.mainTableDao.updateNotNull(mainTable); 
    }

	/**
	* 方法说明： 按条件查询分页主表列表
	*/
	public org.alvin.code.gen.utils.Page<MainTable> queryPage(MainTableCond mainTableCond) {
		mainTableCond.getOrder().put("t.id" , "desc");
		return this.mainTableDao.queryPage(mainTableCond);
	}

	/**
	* 方法说明： 按条件查询不分页主表列表(使用范型)
	*/
	public java.util.List<MainTable> queryList(MainTableCond mainTableCond) {
		mainTableCond.getOrder().put("t.id" , "desc");
		return this.mainTableDao.queryList(mainTableCond);
	}
	
	/**
	* 方法说明： 按条件查询一个 主表 对象
	*/
	public MainTable findOne(MainTableCond mainTableCond) {
		return this.mainTableDao.findOne(mainTableCond);
	}

	/**
	* 方法说明： 按ID查找单个主表记录
	*/
	public MainTable findById(Long id) {
		return this.mainTableDao.findById(id);
	}

	/**
	* 方法说明： 按条件查询主表记录个数
	*/
	public long queryCount(MainTableCond mainTableCond) {
		return this.mainTableDao.queryCount(mainTableCond);
	}


	/**
	 * 方法说明: 查询数据包含外键对象和显示标签
	 *
	 * @param id
	 * @return
	 */
	public MainTableFk findFkById(Long id) {
		return this.mainTableDao.findFkById(id);
	}

	/**
	 * 方法说明: 查询数据包含外键对象和显示标签
	 *
	 * @param mainTableFkCond
	 * @return
	 */
	public MainTableFk findFkOne(MainTableFkCond mainTableFkCond) {
		return this.mainTableDao.findFkOne(mainTableFkCond);
	}

	/**
	 * 方法说明: 只显示标签和id的列表
	 *
	 * @param mainTableFkCond
	 * @return
	 */
	public java.util.List<org.alvin.code.gen.beans.SelectOption> queryLabelList(MainTableFkCond mainTableFkCond) {
		mainTableFkCond.getOrder().put("t.id" , "desc");
		return this.mainTableDao.queryLabelList(mainTableFkCond);
	}

	/**
	 * 方法说明：按条件查询不分页主表列表 (带关系表标签)
	 */
	public java.util.List<MainTableFk> queryFkList(MainTableFkCond mainTableFkCond) {
		mainTableFkCond.getOrder().put("t.id" , "desc");
		return this.mainTableDao.queryFkList(mainTableFkCond);
	}

	/**
	 * 方法说明：按条件查询分页主表列表
	 */
	public org.alvin.code.gen.utils.Page<MainTableFk> queryFkPage(MainTableFkCond mainTableFkCond) {
		mainTableFkCond.getOrder().put("t.id" , "desc");
		return this.mainTableDao.queryFkPage(mainTableFkCond);
	}

}