package yx.pay.system.service;

import yx.pay.common.service.IService;
import yx.pay.system.domain.RoleMenu;

import java.util.List;

public interface RoleMenuServie extends IService<RoleMenu> {

    void deleteRoleMenusByRoleId(String[] roleIds);

    void deleteRoleMenusByMenuId(String[] menuIds);

    List<RoleMenu> getRoleMenusByRoleId(String roleId);
}
