package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.en.DictCode;
import com.atguigu.en.HouseStatus;
import com.atguigu.entity.*;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author feng
 * @create 2022-06-15 10:00
 */
@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {
    @Reference
    private HouseService houseService;
    @Reference
    private DictService dictService;
    @Reference
    private CommunityService communityService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseUserService houseUserService;
    private static final String PAGE_INDEX = "house/index";
    private static final String PAGE_CRATE = "house/create";
    private static final String PAGE_EDIT = "house/edit";
    private static final String LIST_ACTION = "redirect:/house";
    private static final String PAGE_SHOW = "house/show";

    @RequestMapping
    public String index(@RequestParam Map<String, Object> filters, Model model) {
        if (filters.get("pageNum") == null && "".equals(filters.get("pageNum"))) {
            filters.put("pageNum", 1);
        }
        if (filters.get("pageSize") == null && "".equals(filters.get("pageSize"))) {
            filters.put("pageSize", 5);
        }
        PageInfo<House> page = houseService.findPage(filters);
        model.addAttribute("page", page);
        model.addAttribute("filters", filters);

        saveAllDictToRequestScope(model);

        return PAGE_INDEX;
    }

    private void saveAllDictToRequestScope(Model model) {
        List<Community> communityList = communityService.findAll();
        List<Dict> houseTypeList = dictService.findDictListByParentDictCode(DictCode.HOUSETYPE.getMessage());
        List<Dict> floorList = dictService.findDictListByParentDictCode(DictCode.FLOOR.getMessage());
        List<Dict> buildStructureList = dictService.findDictListByParentDictCode(DictCode.BUILDSTRUCTURE.getMessage());
        List<Dict> directionList = dictService.findDictListByParentDictCode(DictCode.DIRECTION.getMessage());
        List<Dict> decorationList = dictService.findDictListByParentDictCode(DictCode.DECORATION.getMessage());
        List<Dict> houseUseList = dictService.findDictListByParentDictCode(DictCode.HOUSEUSE.getMessage());
        model.addAttribute("communityList", communityList);
        model.addAttribute("houseTypeList", houseTypeList);
        model.addAttribute("floorList", floorList);
        model.addAttribute("buildStructureList", buildStructureList);
        model.addAttribute("directionList", directionList);
        model.addAttribute("decorationList", decorationList);
        model.addAttribute("houseUseList", houseUseList);
    }

    @RequestMapping("/create")
    public String create(Model model) {
        saveAllDictToRequestScope(model);
        return PAGE_CRATE;
    }

    @PostMapping("/save")
    public String save(House house, Model model) {
        //设置房源的状态：0表示未发布，1已发布
        house.setStatus(HouseStatus.UNPUBLISHED.code);
        houseService.insert(house);
        return successPage(model, "房源添加成功");
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        House house = houseService.getById(id);
        model.addAttribute("house", house);
        saveAllDictToRequestScope(model);
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(House house, Model model) {
        houseService.update(house);
        return successPage(model, "房源信息修改成功");
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        houseService.delete(id);
        return LIST_ACTION;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        //1.查询当前房源自己的信息，存储到请求域
        House house = houseService.getById(id);
        model.addAttribute("house", house);
        //2.查询当前房源所属的小区信息，存储到请求域
        Community community = communityService.getById(house.getCommunityId());
        model.addAttribute("community", community);
        //3.查询当前房源的房源图片列表（type=1），存储到请求域
        List<HouseImage> houseImage1List = houseImageService.findHouseImageList(id,1);
        model.addAttribute("houseImage1List", houseImage1List);
        //4.查询当前房源的房产图片列表(type=2)，存储到请求域
        List<HouseImage> houseImage2List = houseImageService.findHouseImageList(id,2);
        model.addAttribute("houseImage2List", houseImage2List);
        //5.查询当前房源的经纪人列表，存储到请求域
        List<HouseBroker> houseBrokerList = houseBrokerService.findHouseBrokerListByHouseId(id);
        model.addAttribute("houseBrokerList", houseBrokerList);
        //6.查询当前房源的房东列表，存储到请求域
        List<HouseUser> houseUserList = houseUserService.findHouseUserListByHouseId(id);
        model.addAttribute("houseUserList", houseUserList);
        return PAGE_SHOW;
    }

    @GetMapping("/publish/{id}/{status}")
    public String publish(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        //发布房源或者取消发布房源:其实就是修改房源信息的状态（status）
        House house = new House();
        house.setId(id);
        house.setStatus(status);
        houseService.update(house);
        //重定向访问首页
        return LIST_ACTION;
    }

}
