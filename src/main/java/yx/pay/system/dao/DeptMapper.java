package yx.pay.system.dao;

import org.springframework.stereotype.Component;

import yx.pay.common.config.MyMapper;
import yx.pay.system.domain.Dept;

@Component
public interface DeptMapper extends MyMapper<Dept> {

	/**
	 * 递归删除部门
	 *
	 * @param deptId deptId
	 */
	void deleteDepts(String deptId);
}