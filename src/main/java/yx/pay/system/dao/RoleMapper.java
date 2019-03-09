package yx.pay.system.dao;

import yx.pay.common.config.MyMapper;
import yx.pay.system.domain.Role;

import java.util.List;

public interface RoleMapper extends MyMapper<Role> {
	
	List<Role> findUserRole(String userName);
	
}