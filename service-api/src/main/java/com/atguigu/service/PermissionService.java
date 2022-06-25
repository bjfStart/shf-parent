package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Permission;

import java.util.List;
import java.util.Map;

/**
 * @author feng
 * @create 2022-06-17 18:32
 */
public interface PermissionService extends BaseService<Permission> {
    List<Map<String, Object>> findPermissionByRoleId(Long roleId);

    void saveRolePermission(Long roleId, List<Long> permissionIds);

    /**
     * 查询当前用户的动态菜单
     * @param adminId
     * @return
     */
    List<Permission> findMenuPermissionByAdminId(Long adminId);

    /**
     * 查询所有的菜单
     * @return
     */
    List<Permission> findAllMenu();

    /**
     * 根据adminId查询用户的权限
     * @param adminId
     * @return
     */
    List<String> findCodePermissionListByAdminId(Long adminId);
}
