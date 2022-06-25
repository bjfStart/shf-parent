package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Role;
import com.atguigu.mapper.AdminMapper;
import com.atguigu.mapper.AdminRoleMapper;
import com.atguigu.mapper.RoleMapper;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author feng
 * @create 2022-06-11 10:37
 */
@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Override
    protected BaseMapper<Role> getEntityMapper() {
        return roleMapper;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Role> findAll() {
        return roleMapper.findAll();
    }

    @Override
    public Map<String, List<Role>> findRolesByAdminId(Long id) {
        //1.查询所有的角色
        List<Role> allRoleList = roleMapper.findAll();
        //2.查询所有的已经分配的角色列表
        List<Long> assignRoleIdList = adminRoleMapper.findRoleIdListByAdminId(id);

        //存储已分配和未分配角色列表
        List<Role> assignRoleList = new ArrayList<>();
        List<Role> unAssignRoleList = new ArrayList<>();

        //3.将已分配的角色存入map assignRoleList 和未分配角色放入map中 unassignRoleList
        for (Role role : allRoleList) {
            if(assignRoleIdList.contains(role.getId())){
                assignRoleList.add(role);
            }else{
                unAssignRoleList.add(role);
            }
        }
        //从allRoleList中找出已分配的角色和未分配的角色
        /*List<Role> assign = allRoleList.stream()
                .filter(role -> assignRoleIdList.contains(role.getId()))
                .collect(Collectors.toList());
        List<Role> unassign = allRoleList.stream()
                .filter(role -> !assignRoleIdList.contains(role.getId()))
                .collect(Collectors.toList());*/

        //3.将map返回
        Map<String,List<Role>> roleListMap = new HashMap<>();
        roleListMap.put("assignRoleList",assignRoleList);
        roleListMap.put("unAssignRoleList",unAssignRoleList);
        return roleListMap;
    }

    @Override
    public void saveAdminRoles(Long adminId, List<Long> roleIds) {
        //1.查询所有adminId的对应的已分配的角色id
        List<Long> roleIdListByAdminId = adminRoleMapper.findRoleIdListByAdminId(adminId);
        //2.将已分配的角色id中筛选出roleIds中不存在的角色id
        List<Long> deleteRoleIdList = new ArrayList<>();
        for (Long roleId : roleIdListByAdminId) {
            if(!roleIds.contains(roleId)){
                deleteRoleIdList.add(roleId);
            }
        }
        //3.将筛选出的roleIds中不存的角色id，从acl_amdin_role中删除
        //CollectionUtils.isEmpty(deleteRoleIdList)
        if(deleteRoleIdList != null && deleteRoleIdList.size() > 0){
            adminRoleMapper.deleteByAdminIdAndRoleIds(adminId,deleteRoleIdList);
        }
        //4.遍历roleIds ,判断roleid是否已经分配的用户，
        for (Long roleId : roleIds) {
            AdminRole adminRole = adminRoleMapper.findByAdminIdAndRoleId(adminId,roleId);
            // 4.1若未分配则，将roleId和adminId插入到 acl_admin_role中
            if(adminRole == null){
                adminRole = new AdminRole();
                adminRole.setAdminId(adminId);
                adminRole.setRoleId(roleId);
                adminRoleMapper.insert(adminRole);
            }else{
                //4.2若已分配，判断is_deleted是否为1，若为1，则将is_deleted置为0
                if(adminRole.getIsDeleted() == 1){
                    adminRole.setIsDeleted(0);
                    adminRoleMapper.update(adminRole);
                }
            }
        }
    }

}
