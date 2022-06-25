package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author feng
 * @create 2022-06-17 18:40
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    List<Long> findPermissionByRoleId(Long roleId);

    void deletePermissionByRoleIdAndPermissionId(@Param("roleId") Long roleId,@Param("deletePermissionIdList") List<Long> deletePermissionIdList);

    RolePermission findPermissionByRoleIdAndPermissionId(@Param("roleId") Long roleId,@Param("permissionId") Long permissionId);
}
