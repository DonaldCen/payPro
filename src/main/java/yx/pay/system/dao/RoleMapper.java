package yx.pay.system.dao;

import org.springframework.stereotype.Component;

import yx.pay.common.config.MyMapper;
import yx.pay.system.domain.Role;

import java.util.List;

@Component
public interface RoleMapper extends MyMapper<Role> {
	
	List<Role> findUserRole(String userName);
	
}