package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Permission;

import java.util.List;

/**
 * @author feng
 * @create 2022-06-17 18:34
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    List<Permission> findAll();

    List<Permission> findPermissionByAdminId(Long adminId);

    List<Long> getPermissionByParentId(Long id);

    /**
     * 查询所有权限的code
     * @return
     */
    List<String> findAllCodePermission();

    /**
     * 根据用户的adminId查询权限的code
     * @param adminId
     * @return
     */
    List<String> findCodePermissionListByAdminId(Long adminId);

    Integer findCountByParentId(Long id);
}
