package yx.pay.system.service.impl;

import yx.pay.common.service.impl.BaseService;
import yx.pay.system.domain.UserRole;
import yx.pay.system.service.UserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("userRoleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserRoleServiceImpl extends BaseService<UserRole> implements UserRoleService {

	@Override
	@Transactional
	public void deleteUserRolesByRoleId(String[] roleIds) {
		List<String> list = Arrays.asList(roleIds);
		this.batchDelete(list, "roleId", UserRole.class);
	}

	@Override
	@Transactional
	public void deleteUserRolesByUserId(String[] userIds) {
		List<String> list = Arrays.asList(userIds);
		this.batchDelete(list, "userId", UserRole.class);
	}

	@Override
	public List<String> findUserIdsByRoleId(String[] roleIds) {
		Example example = new Example(UserRole.class);
		example.createCriteria().andIn("roleId", Arrays.asList(roleIds));
		List<UserRole> list = this.selectByExample(example);

		return list.stream().map(userRole -> String.valueOf(userRole.getUserId())).collect(Collectors.toList());
	}

}
