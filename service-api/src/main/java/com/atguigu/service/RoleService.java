package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Role;

import java.util.List;
import java.util.Map;

/**
 * @author feng
 * @create 2022-06-11 10:37
 */
public interface RoleService extends BaseService<Role> {
    /**
     * 查询所有
     * @return
     */
    List<Role> findAll();

    Map<String, List<Role>> findRolesByAdminId(Long id);

    void saveAdminRoles(Long adminId, List<Long> roleIds);
}
