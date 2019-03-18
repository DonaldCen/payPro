package yx.pay.system.dao;

import org.springframework.stereotype.Component;

import yx.pay.common.config.MyMapper;
import yx.pay.system.domain.Menu;

import java.util.List;

@Component
public interface MenuMapper extends MyMapper<Menu> {

    List<Menu> findUserPermissions(String userName);

    List<Menu> findUserMenus(String userName);

    /**
     * 查找当前菜单/按钮关联的用户 ID
     *
     * @param menuId menuId
     * @return 用户 ID集合
     */
    List<String> findUserIdsByMenuId(String menuId);

    /**
     * 递归删除菜单/按钮
     *
     * @param menuId menuId
     */
    void deleteMenus(String menuId);
}