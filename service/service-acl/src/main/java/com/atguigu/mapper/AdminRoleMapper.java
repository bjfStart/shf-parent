package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.AdminRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author feng
 * @create 2022-06-17 15:44
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {
    List<Long> findRoleIdListByAdminId(Long id);

    void deleteByAdminIdAndRoleIds(@Param("adminId")Long adminId,@Param("deleteRoleIdList") List<Long> deleteRoleIdList);

    AdminRole findByAdminIdAndRoleId(@Param("adminId") Long adminId, @Param("roleId") Long roleId);
}
