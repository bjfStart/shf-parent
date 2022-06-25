package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.HouseUser;
import com.atguigu.service.HouseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jws.WebParam;

/**
 * @author feng
 * @create 2022-06-15 19:48
 */
@Controller
@RequestMapping("/houseUser")
public class HouseUserController extends BaseController {
    @Reference
    private HouseUserService houseUserService;

    private static final String PAGE_CREATE = "houseUser/create";
    private static final String PAGE_EDIT = "houseUser/edit";
    private static final String SHOW_ACTION = "redirect:/house/";

    @GetMapping("/create")
    public String create(HouseUser houseUser, Model model){
        model.addAttribute("houseUser",houseUser);
        return PAGE_CREATE;
    }

    @PostMapping("/save")
    public String save(HouseUser houseUser,Model model){
        houseUserService.insert(houseUser);
        return successPage(model,"房东信息添加成功");
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        HouseUser houseUser = houseUserService.getById(id);
        model.addAttribute("houseUser",houseUser);
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(HouseUser houseUser,Model model){
        houseUserService.update(houseUser);
        return successPage(model,"房东信息修改成功");
    }

    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId,@PathVariable("id")Long id, Model model){
        houseUserService.delete(id);
        return SHOW_ACTION+houseId;
    }
}
