package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Role;
import com.atguigu.result.Result;
import com.atguigu.service.AdminService;
import com.atguigu.service.RoleService;
import com.atguigu.util.FileUtil;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author feng
 * @create 2022-06-11 15:41
 */
@RequestMapping("/admin")
@Controller
public class AdminController extends BaseController {
    @Reference
    private AdminService adminService;
    @Reference
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String ADMIN_INDEX = "admin/index";
    private static final String PAGE_UPLOAD = "admin/upload";
    private static final String PAGE_ASSIGN = "admin/assignShow";
    @RequestMapping
    public String index(@RequestParam Map<String,Object> filters, Model model){
        if(filters.get("pageNum")==null || "".equals(filters.get("pageNum"))){
            filters.put("pageNum",1);
        }
        if(filters.get("pageSize")==null || "".equals(filters.get("pageSize"))){
            filters.put("pageSize",10);
        }
        PageInfo<Admin> pageInfo = adminService.findPage(filters);
        model.addAttribute("page",pageInfo);
        model.addAttribute("filters",filters);
        return ADMIN_INDEX;
    }
    @PostMapping("/save")
    public String save(Admin admin,Model model){
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        try {
            adminService.insert(admin);
        } catch (Exception e) {
            return errorPage(model,e.getMessage());
        }
        return successPage(model,"用户添加成功");
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id")Long id,Model model){
        Admin admin = adminService.getById(id);
        model.addAttribute("admin",admin);
        return "admin/edit";
    }

    @PostMapping("/update")
    public String update(Admin admin,Model model){
        adminService.update(admin);
        return successPage(model,"用户信息修改成功");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id")Long id){
        adminService.delete(id);
        return "redirect:/admin";
    }

    @RequestMapping("/uploadShow/{id}")
    public String uploadShow(@PathVariable("id")Long id,Model model){
        model.addAttribute("id",id);
        return PAGE_UPLOAD;
    }
    @PostMapping("/upload/{id}")
    public String upload(@PathVariable("id")Long id,
                         @RequestParam("file")MultipartFile[] multipartFiles,
                         Model model) throws IOException {
        for (MultipartFile multipartFile : multipartFiles) {
            //从七牛上传头像
            //1.1获取文件名
            String filename = multipartFile.getOriginalFilename();
            //1.2生成唯一文件名
            String uuidName = FileUtil.getUUIDName(filename);
            //1.3上传到七牛云
            QiniuUtils.upload2Qiniu(multipartFile.getBytes(),uuidName);

            //将文件名保存到数据库中
            Admin admin = new Admin();
            admin.setId(id);
            admin.setHeadUrl(QiniuUtils.getUrl(uuidName));
            adminService.update(admin);
        }
        return successPage(model,"头像上传成功");
    }

    /**
     * 给用户分配角色
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/assignShow/{id}")
    public String assignShow(@PathVariable("id")Long id,Model model){

        //1.调用业务层获取用户已分配和未分配角色列表
        //2.将查询到的数据存储到请求域中，请求域中的key分别是assignRoleList和unassignRoleList
        Map<String, List<Role>> roleListMap =  roleService.findRolesByAdminId(id);
//        model.addAttribute("assignRoleList",roleListMap.get("assignRoleList"));
//        model.addAttribute("unassignRoleList",roleListMap.get("unAssignRoleList"));
        model.addAllAttributes(roleListMap);
        //3.将id存储到请求域中
        model.addAttribute("adminId",id);
        return PAGE_ASSIGN;
    }

    @PostMapping("/assignRole")
    public String assignRole(@RequestParam("adminId") Long adminId,
                             @RequestParam("roleIds") List<Long> roleIds,
                             Model model){
        //1.调用业务层分配角色
        roleService.saveAdminRoles(adminId,roleIds);
        return successPage(model,"角色分配成功");
    }


}
