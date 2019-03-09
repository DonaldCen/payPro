package yx.pay.system.service;

import yx.pay.common.domain.QueryRequest;
import yx.pay.common.service.IService;
import yx.pay.system.domain.Role;

import java.util.List;

public interface RoleService extends IService<Role> {

    List<Role> findRoles(Role role, QueryRequest request);

    List<Role> findUserRole(String userName);

    Role findByName(String roleName);

    void createRole(Role role);

    void deleteRoles(String[] roleIds) throws Exception;

    void updateRole(Role role) throws Exception;
}
