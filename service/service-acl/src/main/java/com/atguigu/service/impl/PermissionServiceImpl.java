package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Permission;
import com.atguigu.entity.RolePermission;
import com.atguigu.helper.PermissionHelper;
import com.atguigu.mapper.PermissionMapper;
import com.atguigu.mapper.RolePermissionMapper;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author feng
 * @create 2022-06-17 18:32
 */
@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Override
    protected BaseMapper<Permission> getEntityMapper() {
        return permissionMapper;
    }

    @Override
    public List<Map<String, Object>> findPermissionByRoleId(Long roleId) {
        //根据角色Id获取acl_role_permission表中对应的权限列表
        List<Long> assignPermissionList = rolePermissionMapper.findPermissionByRoleId(roleId);

        //获取权限表中的所有权限
        List<Permission> permissionList = permissionMapper.findAll();

        //设置一个List<Map<String,Object>>,用于返回数据
        List<Map<String,Object>> resultMapList = new ArrayList<>();

        //将permissionList存储到Map中
        /*resultMapList = permissionList.stream()
                .map(permission -> {
                    //1.创建一个对象
                    Map<String, Object> map = new HashMap<>();
                    //设置id
                    map.put("id", permission.getId());
                    //设置pid
                    map.put("pId", permission.getParentId());
                    //设置name
                    map.put("name", permission.getName());
                    //设置open，默认设置为true
                    map.put("open", true);
                    //设置为checked:如果当前权限已分配给当前角色，设置为true
                    map.put("checked", assignPermissionList.contains(permission.getId()));
                    return map;
                })
                .collect(Collectors.toList());*/

        //存储键值对为 id， pId, name, checked, open
        for (Permission permission : permissionList) {
            Map<String,Object> resultMap = new HashMap<>();
            //遍历所有权限，区分出已分配和未分配的权限，并将信息存储到map中
            if(assignPermissionList.contains(permission.getId())) {
                //已分配的权限
                resultMap.put("checked",true);
            }else{
                resultMap.put("checked",false);
            }
            resultMap.put("id",permission.getId());
            resultMap.put("pId",permission.getParentId());
            resultMap.put("name",permission.getName());
            resultMap.put("open",true);
            resultMapList.add(resultMap);
        }
        return resultMapList;
    }

    @Override
    public void saveRolePermission(Long roleId, List<Long> permissionIds) {
        //获取角色id对应的所有已分配的权限id列表
        List<Long> assignPermissionIdList = rolePermissionMapper.findPermissionByRoleId(roleId);
        //删除角色id对应的权限在 permissionIds中不存在的权限
        List<Long> deletePermissionIdList = new ArrayList<>();
        for (Long permissionId : assignPermissionIdList) {
            if(!permissionIds.contains(permissionId)){
                deletePermissionIdList.add(permissionId);
            }
        }
        if(deletePermissionIdList != null && deletePermissionIdList.size() > 0){
            rolePermissionMapper.deletePermissionByRoleIdAndPermissionId(roleId,deletePermissionIdList);
        }

        //给需要分配的权限（permissionIds）进行分配
        /*permissionIds.forEach(permissionId -> {
                    RolePermission rolePermission = rolePermissionMapper.findPermissionByRoleIdAndPermissionId(roleId, permissionId);
                    if(rolePermission == null){
                        //之前从未被分配过
                        rolePermission = new RolePermission();
                        rolePermission.setRoleId(roleId);
                        rolePermission.setPermissionId(permissionId);
                        rolePermissionMapper.insert(rolePermission);
                    }else{
                        //说明之前已经分配过，判断这次需不需要重新分配
                        if(rolePermission.getIsDeleted() == 1){
                            //需要重新分配：修改isDeleted为0
                            rolePermission.setIsDeleted(0);
                            rolePermissionMapper.update(rolePermission);
                        }
                    }

                });*/

        //给角色分配权限，
        for (Long permissionId : permissionIds) {
            //如果根据角色id和权限id
            RolePermission rolePermission = rolePermissionMapper.findPermissionByRoleIdAndPermissionId(roleId,permissionId);
            if(rolePermission == null){
                //查询不到记录，则插入一条记录
                rolePermission = new RolePermission();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(permissionId);
                rolePermissionMapper.insert(rolePermission);
            }else{
                //查询到记录了，如果is_deleted=1,则将is_deleted=0
                if(rolePermission.getIsDeleted() == 1){
                    rolePermission.setIsDeleted(0);
                    rolePermissionMapper.update(rolePermission);
                }
            }
        }

    }

    /**
     * 根据用户Id获取权限列表
     * @param adminId
     * @return
     */
    @Override
    public List<Permission> findMenuPermissionByAdminId(Long adminId) {
        List<Permission> permissionList = null;
        //判断是否为系统管理员
        if(adminId == 1){
            //如果是超级管理员，直接查询所有的菜单
            permissionList = permissionMapper.findAll();
        }else{
            //如果不是超级管理员，查询根据用户id，查询出用户的菜单那
            permissionList = permissionMapper.findPermissionByAdminId(adminId);
        }
        /*//2.将权限列表构建父子关系
        List<Permission> menu = new ArrayList<>();
        for (Permission permission : permissionList) {
            //2.1 判断当前菜单是否是一级菜单
            if (permission.getParentId() == 0) {
                //一级菜单
                //2.1 设置一级菜单的子菜单列表
                permission.setChildren(getChildren(permission,permissionList));
                menu.add(permission);
            }
        }
        return menu;*/
        return PermissionHelper.build(permissionList);
    }

   /* *//**
     * 从原始菜单中获取某个权限的子菜单列表
     * @param permission
     * @param originalPermissionList
     * @return
     *//*
    private List<Permission> getChildren(Permission permission, List<Permission> originalPermissionList) {
        List<Permission> children = new ArrayList<>();
        //1.遍历出原始菜单的每一个权限
        for (Permission child : originalPermissionList) {
            //1.1如果 originalPermissionList的父id等于permission的id
            if (permission.getId().equals(child.getParentId())) {
                //originalPermission是permission的子菜单，则将originalPermission添加到chilren中
                //子菜单还有子菜单
                child.setChildren(getChildren(child,originalPermissionList));
                children.add(child);
            }
        }
        return children;
    }*/


    @Override
    public List<Permission> findAllMenu() {
        List<Permission> allPermissionList = permissionMapper.findAll();
        return PermissionHelper.build(allPermissionList);
    }

    @Override
    public List<String> findCodePermissionListByAdminId(Long adminId) {
        //判断是否是超级管理员
        if(adminId == 1){
            //超级管理员，查询出所有的Code
            return permissionMapper.findAllCodePermission();
        }
        //普通用户：根据amindId查询code的集合
        return permissionMapper.findCodePermissionListByAdminId(adminId);
    }

    @Override
    public void delete(Long id) {
        //判断当前菜单是否有子菜单，如果有子菜单，则不能删除，以当前菜单id作为parentId查询子菜单,
        //如果查询到子菜单，则不能删除
        Integer count  = permissionMapper.findCountByParentId(id);
        if(count > 0){
            //说明有子菜单，不能删除
            throw  new RuntimeException("当前菜单有子菜单，不能删除");
        }
        super.delete(id);

    }
}
