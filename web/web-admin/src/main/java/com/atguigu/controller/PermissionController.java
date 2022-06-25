package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Permission;
import com.atguigu.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author feng
 * @create 2022-06-17 23:30
 */
@Controller
@RequestMapping("/permission")
public class PermissionController extends BaseController {
    @Reference
    private PermissionService permissionService;

    private static final String PAGE_INDEX = "permission/index";
    private static final String PAGE_CREATE = "permission/create";
    private static final String LIST_ACTION = "redirect:/permission";
    private static final String PAGE_EDIT = "permission/edit";

    @RequestMapping
    public String index(Model model) {
        //查询出所有的菜单，（构建父子关系）
        List<Permission> permissionList = permissionService.findAllMenu();
        model.addAttribute("list", permissionList);
        return PAGE_INDEX;
    }

    @GetMapping("/create")
    public String create(Permission permission ,Model model) {
        model.addAttribute("permission", permission);
        return PAGE_CREATE;
    }

    @PostMapping("/save")
    public String save(Permission permission,Model model) {
        permissionService.insert(permission);
        return successPage(model,"菜单添加成功");
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id")Long id,Model model){
        try {
            permissionService.delete(id);
        } catch (Exception e) {
            return errorPage(model,e.getMessage());
        }
        return LIST_ACTION;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id")Long id,Model model){
        Permission permission = permissionService.getById(id);
        model.addAttribute("permission",permission);
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(Permission permission,Model model){
        permissionService.update(permission);
        return successPage(model,"修改菜单信息成功");
    }
}
