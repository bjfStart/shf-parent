package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Permission;
import com.atguigu.entity.Role;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author feng
 * @create 2022-06-11 10:39
 */
@RequestMapping("/role")
@Controller
public class RoleController extends BaseController {
    @Reference
    private RoleService roleService;
    @Reference
    private PermissionService permissionService;

    private static  final String PAGE_INDEX="role/index";
    private static  final String PAGE_EDIT="role/edit";
    private static final String LIST_ACTION = "redirect:/role";
    private static final String PAGE_ASSIGN = "role/assignShow";
    private static final String PAGE_CREATE = "role/create";
    @PreAuthorize("hasAnyAuthority('role.show')")
    @RequestMapping
    public String index(@RequestParam Map<String,Object> filters, Model model){
        //filters获取到的客户端的请求参数，里面可能包含pageNum,pageSize,roleName
        //判断请求参数是否传入pageNum和pageSize，
        if(filters.get("pageNum")==null || "".equals(filters.get("pageNum"))){
            filters.put("pageNum",1);
        }
        if(filters.get("pageSize")==null || "".equals(filters.get("pageSize"))){
            filters.put("pageSize",10);
        }
        //调用业务层查询分页信息
        PageInfo<Role> pageInfo = roleService.findPage(filters);

        //将分页数据存储到请求域
        model.addAttribute("page",pageInfo);
        //将搜索条件存储到请求域
        model.addAttribute("filters",filters);
        return PAGE_INDEX;
    }

    @PreAuthorize("hasAnyAuthority('role.create')")
    @GetMapping("/create")
    public String create(){
        return PAGE_CREATE;
    }
    @PreAuthorize("hasAnyAuthority('role.create')")
    @RequestMapping("/save")
    public String save(Role role,Model model){
        //1.调用业务层的方法保存角色信息
        roleService.insert(role);
        /*//2.往请求域添加数据
        model.addAttribute("messagePage","新增角色成功");
        //2.显示common/successPage.html
        return PAGE_SUCCESS;*/
        return successPage(model,"新增角色成功");
    }

    @PreAuthorize("hasAnyAuthority('role.edit')")
    @GetMapping("/edit/{id}")
    public String method(@PathVariable("id")Long id,Model model){
        //id代表要查询的角色的id
        //1.调用业务层的方法根据id查询角色信息
        Role role = roleService.getById(id);
        //2.将查询到的role存储到请求域中
        model.addAttribute("role",role);

        return PAGE_EDIT;
    }
    @PreAuthorize("hasAnyAuthority('role.edit')")
    @PostMapping("/update")
    public String update(Role role,Model model){
        //1.调用业务层的方法修改角色信息
        roleService.update(role);
        /*model.addAttribute("messagePage","修改角色信息成功");
        return PAGE_SUCCESS;*/
        return successPage(model,"修改角色信息成功");
    }
    @PreAuthorize("hasAnyAuthority('role.delete')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id")Long id){
        //1.调用业务层的方法根据id删除角色信息
        roleService.delete(id);
        //2.重新查询所有角色
        return LIST_ACTION;
    }
    @PreAuthorize("hasAnyAuthority('role.assgin')")
    @RequestMapping("/assignShow/{id}")
    public String assignShow(@PathVariable("id") Long id,Model model){
        //id是要分配的那个角色的id
        //将角色id存入请求域中
        model.addAttribute("roleId",id);
        //获取当前角色的全部的权限列表，存储到请求域中
        //客户端需要Json数据（id:当前节点的id pid:当前节点的父节点 name:当前节点的属性 open:当前节点是否展开，checked:是否勾选）
        List<Map<String,Object>> zNodes = permissionService.findPermissionByRoleId(id);
        //将zNodes转成json字符串，存储到请求域
        model.addAttribute("zNodes", JSON.toJSONString(zNodes));
        return PAGE_ASSIGN;
    }
    @PreAuthorize("hasAnyAuthority('role.assgin')")
    @PostMapping("/assignPermission")
    public String assignPermission(@RequestParam("roleId")Long roleId,
                                   @RequestParam("permissionIds")List<Long> permissionIds,
                                   Model model){
        //调用业务层，在acl_role_permission中添加角色和权限的关联关系
        permissionService.saveRolePermission(roleId,permissionIds);
        return successPage(model,"角色分配权限成功");
    }
}
