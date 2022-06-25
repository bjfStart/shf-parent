package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author feng
 * @create 2022-06-17 20:45
 */
@Controller
public class IndexController {
    private static final String PAGE_INDEX = "frame/index";
    @Reference
    private AdminService adminService;
    @Reference
    private PermissionService permissionService;
    @GetMapping("/")
    public String index(Model model){
        //查询出动态菜单，并且存储到请求域
        //获取当前登录的用户，动态菜单其实就要对应当前登录的用户权限
        //因为当前还未做后台管理系统的登录，默认当前用户为admin
        /*//尚未引入权限认证框架，没有登录，先把用户id写死，登录之后再改
        Long adminId = 1L;
        Admin admin = adminService.getById(adminId);
        //查询用户的权限列表
        List<Permission> permissionList = permissionService.findMenuPermissionByAdminId(adminId);
        model.addAttribute("admin",admin);
        model.addAttribute("permissionList",permissionList);
*/
        //获取当前登录的用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Admin admin = adminService.getByUsername(user.getUsername());
        //查询用户的权限列表
        List<Permission> permission = permissionService.findMenuPermissionByAdminId(admin.getId());
        model.addAttribute("admin",admin);
        model.addAttribute("permissionList",permission);
        return PAGE_INDEX;
    }
}
