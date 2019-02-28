package org.alvin.code.gen.beans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.alvin.code.gen.utils.SqlUtil;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author gzz
 * @功能说明:拼加页面查询条件的基础类
 * @date 2015-12-12
 */

@Data
public abstract class BaseCondition {
	@ApiModelProperty( dataType = "List" , hidden = true)
	private List<Object> paramList = new ArrayList<Object>();// 参数值
	@ApiModelProperty(hidden = true)
	private StringBuffer condition = new StringBuffer();// 条件语句
	@ApiModelProperty(value = "每页记录个数", dataType = "Integer")
	private Integer size = 10;// 页大小(每页记录条)
	@ApiModelProperty(value = "当前页", dataType = "Integer")
	private Integer page = 0;// 当前页码
	@ApiModelProperty( dataType = "Map" , hidden = true)
//	private Map<String, String> orderMap = new HashMap<>();// 支持的排序字段
//	@ApiModelProperty( dataType = "Map" , hidden = true)
	private Map<String, String> order = new HashMap<>();// 当前使用的排序字段
	@ApiModelProperty( dataType = "List" , hidden = true)
	private List<String> selectedFields; //选查字段
	@ApiModelProperty( dataType = "StringBuilder" , hidden = true)
	private StringBuilder joinTables = new StringBuilder(); //选连表语句
	@ApiModelProperty( dataType = "Byte" , hidden = true)
	private Byte queryMode = QUERY_MODE_JOIN_ALL ;//查询模式 默认是全查

	public static  final byte QUERY_MODE_SINGLE_ALL = 0; //单表全查
	public static final byte QUERY_MODE_SINGLE_SELECT = 1 ;//单表选查
	public static  final byte QUERY_MODE_JOIN_ALL = 2; //全连全查
	public static final byte QUERY_MODE_JOIN_ALL_QUERY_SELECT = 3; //全连选查
	public static final byte QUERY_MODE_JOIN_SELECT_QUERY_SELECT = 4; //选连选查

	protected void addOrder(String field,String sortType) {
		this.order.put(field, sortType);
	}

	public String getOrderSql() {
		String sql = "";
		if (order.size() > 0) {
			sql += " ORDER BY ";
			for (Map.Entry<String, String> entry : order.entrySet()) {
				if (entry.getKey() != null && !entry.getKey().equals("")) {
					sql +=  entry.getKey() + " " + entry.getValue() + ",";
				}
			}
			sql = sql.substring(0, sql.length() - 1);
		}
		return sql;
	}

	/**
	 * @功能: 拼加条件使用等于大于小于....运算符(String类型)
	 */
	protected void add(String value, String strSQL) {
		if (null != strSQL && null != value && !"".equals(strSQL) && !"".equals(value)) {
			condition.append(" " + strSQL);
			paramList.add(value);
		}
	}

	/**
	 * @功能: 拼加条件使用等于大于小于....运算符(Short类型)
	 */
	protected void add(Short value, String strSQL) {
		if (null != strSQL && null != value && !"".equals(strSQL)) {
			condition.append(" " + strSQL);
			paramList.add(value);
		}
	}

	protected void add(Byte value, String strSQL) {
		if (null != strSQL && null != value && !"".equals(strSQL)) {
			condition.append(" " + strSQL);
			paramList.add(value);
		}
	}

	/**
	 * @功能: 拼加条件使用等于大于小于....运算符(String类型)
	 */
	protected void add(Float value, String strSQL) {
		if (null != strSQL && null != value && !"".equals(strSQL)) {
			condition.append(" " + strSQL);
			paramList.add(value);
		}
	}

	/**
	 * @功能: 拼加条件使用等于大于小于....运算符(Long类型)
	 */

	protected void add(Long value, String strSQL) {
		if (null != strSQL && null != value && !"".equals(strSQL)) {
			condition.append(" " + strSQL);
			paramList.add(value);
		}
	}

	/**
	 * @功能: 拼加条件使用等于大于小于....运算符(Boolean类型)
	 */
	protected void add(Boolean value, String strSQL) {
		if (null != strSQL && null != value && !"".equals(strSQL)) {
			condition.append(" " + strSQL);
			paramList.add(value);
		}
	}

	/**
	 * @功能: 拼加条件使用等于大于小于....运算符(BigDecimal类型)
	 */
	protected void add(BigDecimal value, String strSQL) {
		if (null != strSQL && null != value && !"".equals(strSQL)) {
			condition.append(" " + strSQL);
			paramList.add(value);
		}
	}

	/**
	 * @功能: 拼加条件使用等于大于小于....运算符(Double)
	 */
	protected void add(Double value, String strSQL) {
		if (null != strSQL && null != value && !"".equals(strSQL)) {
			condition.append(" " + strSQL);
			paramList.add(value);
		}
	}
	/**
	 * @功能: 拼加条件使用等于大于小于....运算符(Integer类型)
	 */
	protected void add(Integer value, String strSQL) {
		if (null != value && !"".equals(strSQL) && null != strSQL) {
			condition.append(" " + strSQL);
			paramList.add(value);
		}
	}

	/**
	 * @功能: 拼加条件使用等于大于小于....运算符(Date类型)
	 */
	protected void add(Date value, String strSQL) {
		if (null != value && !"".equals(strSQL) && null != strSQL) {
			condition.append(" " + strSQL);
			paramList.add(value);
		}
	}

	/**
	 * @功能: 拼加条件
	 */
	protected void add(String strSQL) {
		if (null != strSQL && !"".equals(strSQL)) {
			condition.append(" " + strSQL);
		}
	}

	/**
	 * @功能: 拼加条件
	 */
	protected void add(List<?> ids, String strSQL) {
		if (null != strSQL && !"".equals(strSQL) && ids != null && ids.size() > 0) {
			condition.append(" " + strSQL + SqlUtil.ArrayToIn(ids));
		}
	}

	/**
	 * @param value
	 *            :属性名称
	 * @param strSQL
	 *            :参数SQL字符
	 * @param pos
	 *            :字句中百分号出现位置
	 * @return strSQL:拼加后SQL字符包括占位符
	 * @功能: 拼加条件使用like关键字模糊查询时
	 */
	protected void add(String value, String strSQL, int pos) {
		if (null != strSQL && null != value && !"".equals(strSQL) && !"".equals(value)) {
			condition.append(" " + strSQL);
			if (pos == 1) {
				paramList.add("%" + value);
			} else if (pos == 2) {
				paramList.add(value + "%");
			} else if (pos == 3) {
				paramList.add("%" + value + "%");
			}
		}
	}

	/**
	 * @功能: 将List转为数组
	 */
	public Object[] getArray() {
		return paramList.toArray();
	}

	/**
	 * @功能: 取条件字符串
	 */
	public String getCondition() {
		// 清除查询条件
		condition.setLength(0);
		paramList.clear();

		addCondition();
		return condition.toString();
	}

	/**
	 * @功能: 拼加条件方法
	 */
	public abstract void addCondition();
}
