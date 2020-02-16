package org.alvin.springjdbc.system.address;
import java.util.List;

import com.google.common.base.Joiner;
import org.alvin.code.gen.beans.SelectOption;
import org.alvin.code.gen.utils.Page;
import lombok.extern.slf4j.Slf4j;
import org.alvin.code.gen.utils.SqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @类说明: 收寄信息--数据逻辑层
* @author: 唐植超
* @date : 2019-08-09 19:18:43
**/
@Slf4j
@Service
public class AddressService {

	@Autowired
	private AddressDao dao; //注入收寄信息数据访问层

	/**
	* 方法说明：  新增[收寄信息]记录
	*/
	@Transactional
	public int save(Address address) {
		return dao.save(address);
	}

	/**
	* 方法说明：  删除收寄信息记录(多条)
	*/
	@Transactional
	public int delete(Long ids[]) {
		return dao.deleteLogic(ids);//逻辑删除
		//return dao.delete(ids);//物理删除
	}

	/**
	* 方法说明：  更新收寄信息记录
	*/
	@Transactional
	public int update(Address address) {
		return dao.update(address); 
	}

	 /**
    * 方法说明：更新收寄信息记录,不为空的都更新掉
    */
    @Transactional
    public int updateNotNull(Address address) {
		return dao.updateNotNull(address); 
    }

	/**
	* 方法说明： 按条件查询分页收寄信息列表
	*/
	public Page<Address> queryPage(AddressCond cond) {
		cond.getOrder().put("t.id" , "desc");
		return dao.queryPage(cond);
	}

	/**
	* 方法说明： 按条件查询不分页收寄信息列表(使用范型)
	*/
	public List<Address> queryList(AddressCond cond) {
		cond.getOrder().put("t.id" , "desc");
		return dao.queryList(cond);
	}
	
	/**
	* 方法说明： 按条件查询一个 收寄信息 对象
	*/
	public Address findOne(AddressCond cond) {
		return dao.findOne(cond);
	}

	/**
	* 方法说明： 按ID查找单个收寄信息记录
	*/
	public Address findById(Long id) {
		return dao.findById(id);
	}

	/**
	* 方法说明： 按条件查询收寄信息记录个数
	*/
	public long queryCount(AddressCond cond) {
		return dao.queryCount(cond);
	}


	/**
	 * 方法说明: 查询数据包含外键对象和显示标签
	 *
	 * @param id
	 * @return
	 */
	public AddressFk findFkById(Long id) {
		return this.dao.findFkById(id);
	}

	/**
	 * 方法说明: 查询数据包含外键对象和显示标签
	 *
	 * @param cond
	 * @return
	 */
	public AddressFk findFkOne(AddressFkCond cond) {
		return this.dao.findFkOne(cond);
	}

	/**
	 * 方法说明: 只显示标签和id的列表
	 *
	 * @param cond
	 * @return
	 */
	public List<SelectOption> queryLabelList(AddressCond cond) {
		cond.getOrder().put("t.id" , "desc");
		return this.dao.queryLabelList(cond);
	}

	/**
	 * 方法说明：按条件查询不分页收寄信息列表 (带关系表标签)
	 */
	public List<AddressFk> queryFkList(AddressCond cond) {
		cond.getOrder().put("t.id" , "desc");
		return this.dao.queryFkList(cond);
	}

	/**
	 * 方法说明：按条件查询分页收寄信息列表
	 */
	public Page<AddressFk> queryFkPage(AddressCond cond) {
		cond.getOrder().put("t.id" , "desc");
		return this.dao.queryFkPage(cond);
	}

}