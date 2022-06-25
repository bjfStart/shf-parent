package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.AdminService;
import com.atguigu.service.HouseBrokerService;
import org.omg.SendingContext.RunTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author feng
 * @create 2022-06-15 18:02
 */
@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController extends BaseController {
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private AdminService adminService;
    private static final String PAGE_CREATE = "houseBroker/create";
    private static final String PAGE_EDIT = "houseBroker/edit";
    private static final String SHOW_ACTION = "redirect:/house/";

    @GetMapping("/create")
    public String create(HouseBroker houseBroker, Model model) {
        model.addAttribute("houseBroker", houseBroker);
        saveAdminListToModel(model);
        return PAGE_CREATE;
    }

    /**
     * 查询所有管理员，并保存到model中
     * @param model
     */
    private void saveAdminListToModel(Model model) {
        List<Admin> adminList = adminService.findAll();
        model.addAttribute("adminList", adminList);
    }

    @PostMapping("/save")
    public String save(HouseBroker houseBroker,Model model){
        //查询当前管理员是否为当前房源的经济人
        HouseBroker dbhouseBroker = houseBrokerService.getByHouseIdAndBrokerId(houseBroker.getHouseId(),houseBroker.getBrokerId());
        if(dbhouseBroker != null){
            //说明当前管理员已经是当前房源的经纪人，不能重复添加
//            throw new RuntimeException("管理员已经是当前房源的经纪人，不能重复添加");
            return errorPage(model,"管理员已经是当前房源的经纪人，不能重复添加");
        }
        //当前管理员不是当前房源的经济人
        setHouseBrokerOtherFields(houseBroker);
        houseBrokerService.insert(houseBroker);
        return successPage(model,"增加经纪人成功");
    }

    /**
     * 设置房源经纪人其他字段
     * @param houseBroker
     */
    private void setHouseBrokerOtherFields(HouseBroker houseBroker) {
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id")Long id,Model model){
        //id就是房源经纪人的id
        //调用业务层的方法，根据id查询房源经纪人
        HouseBroker houseBroker = houseBrokerService.getById(id);

        //查询处所有的管理员
        model.addAttribute("houseBroker",houseBroker);
        saveAdminListToModel(model);
        return PAGE_EDIT;
    }

    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId")Long houseId,@PathVariable("id") Long id,Model model){
        houseBrokerService.delete(id);
        return SHOW_ACTION+houseId;
    }

    @PostMapping("/update")
    public String update(HouseBroker houseBroker,Model model){
        //根据id，查询房源经纪人信息
        HouseBroker dbhouseBroker = houseBrokerService.getById(houseBroker.getId());
        //判断当前要修改的经纪人是否已经是当前房源的经纪人
        if (houseBrokerService.getByHouseIdAndBrokerId(dbhouseBroker.getHouseId(), houseBroker.getBrokerId()) != null) {
            return errorPage(model,"管理员已经是当前房源的经纪人，不能重复添加");
        }

        //当前管理员不是当前房源的经济人,则可以修改
        //传来两个数据 id(hse_house_broker的id),brokerId(要修改成的管理员id)
        //1. 根据管理员id（brokerId）查询管理员
        setHouseBrokerOtherFields(houseBroker);


        //2. 修改
        houseBrokerService.update(houseBroker);
        return successPage(model,"经纪人信息修改成功");
    }
}
